package com.flink.connectors.pushgateway.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
public class HttpUtil {

    /**
     * 设置超时毫秒数
     */
    private static final int CONNECT_TIMEOUT = 10000;
    /**
     * 设置传输毫秒数
     */
    private static final int SOCKET_TIMEOUT = 15000;
    /**
     * 获取请求超时毫秒数
     */
    private static final int REQUEST_CONNECT_TIMEOUT = 10000;
    /**
     * 最大连接数
     */
    private static final int CONNECT_TOTAL = 200;
    /**
     * 设置每个路由的基础连接数
     */
    private static final int CONNECT_ROUTE = 20;
    /**
     * 设置重用连接时间
     */
    private static final int VALIDATE_TIME = 30000;
    private static PoolingHttpClientConnectionManager cm;
    /**
     * 连接重试次数
     */
    private final int RETRY_TIMES = 10;
    private final CloseableHttpClient client;

    public HttpUtil() {
        this(null, -1, null);
    }

    public HttpUtil(String proxyHost, int proxyPort, String proxyType) {
        init();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(REQUEST_CONNECT_TIMEOUT)
                .build();
        if (proxyHost != null && !proxyHost.trim().equals("")) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort, proxyType);
            client = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setProxy(proxy)
                    .setConnectionManager(cm)
                    .setRetryHandler((exception, count, context) -> {
                        if (count > RETRY_TIMES) {
                            return false;
                        }
                        if (exception instanceof UnknownHostException || exception instanceof ConnectTimeoutException ||
                                exception instanceof NoHttpResponseException) {
                            return true;
                        }
                        HttpClientContext clientContext = HttpClientContext.adapt(context);
                        HttpRequest request = clientContext.getRequest();
                        // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
                        return !(request instanceof HttpEntityEnclosingRequest);
                    })
                    .build();
        } else {
            client = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setConnectionManager(cm)
                    .setRetryHandler((exception, count, context) -> {
                        if (count > RETRY_TIMES) {
                            return false;
                        }
                        if (exception instanceof UnknownHostException || exception instanceof ConnectTimeoutException ||
                                exception instanceof NoHttpResponseException) {
                            return true;
                        }
                        HttpClientContext clientContext = HttpClientContext.adapt(context);
                        HttpRequest request = clientContext.getRequest();
                        // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
                        return !(request instanceof HttpEntityEnclosingRequest);
                    })
                    .build();
        }
    }

    private static void init() {
        if (null == cm) {
            cm = new PoolingHttpClientConnectionManager();
            //整个连接池最大连接数
            cm.setMaxTotal(CONNECT_TOTAL);
            // 路由最大连接数，默认值是2
            cm.setDefaultMaxPerRoute(CONNECT_ROUTE);
            cm.setValidateAfterInactivity(VALIDATE_TIME);
        }
    }

    /**
     * 使用httpclient发送post请求
     */
    public void postForHttpClient(String baseUrl, String apiUrl, String body) {
        try {
            log.info("curl地址为{}", baseUrl + apiUrl);
            String url = buildUrl(baseUrl, apiUrl);
            if (null == url) {
                log.error("curl地址错误");
                return;
            }
            HttpPost post = new HttpPost(url);
            post.addHeader(HttpHeaders.CONTENT_TYPE, "text/plain; version=0.0.4; charset=utf-8");
            post.setEntity(new StringEntity(body));
            HttpResponse response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 200) {
                log.error("curl返回错误码{},result:{}", response.getStatusLine().getStatusCode(),result);
            }
        } catch (Exception e) {
            log.error("curl请求错误", e);
        }
    }

    /**
     * 使用httpclient发送post请求
     */
    public String postForHttpClient(String baseUrls, String apiUrl, JSONObject jdata, Map<String, Object> params,
                                    String accessKeyId, String accessKeySecret) throws Exception {
        List<String> baseUrlList = Splitter.on(",").splitToList(baseUrls);
        Exception exception = null;
        for (String baseUrl : baseUrlList) {
            if (StringUtils.isBlank(baseUrl)) {
                continue;
            }
            try {
                String url = buildUrl(baseUrl, apiUrl);
                if (null == url) {
                    return null;
                }
                jdata = MapUtils.isNotEmpty(jdata) ? jdata : new JSONObject();
                String body = JSON.toJSONString(jdata);
                HttpPost post = new HttpPost(url);
                String md5Content = md5String(body);
                post.addHeader("Content-MD5", md5Content);
                post.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                String date = formatDate();
                String authorization = getAuthorization("POST", md5Content, "application/json", date, apiUrl, params,
                        accessKeyId, accessKeySecret);
                post.addHeader("Authorization", authorization);
                post.addHeader("Date", date);
                if (MapUtils.isNotEmpty(params)) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        post.addHeader(entry.getKey(), (String) entry.getValue());
                    }
                }
                post.setEntity(new StringEntity(body));
                HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException(result);
                }
                return result;
            } catch (Exception e) {
                exception = e;
            }
        }
        if (null != exception) {
            throw new Exception(exception);
        }
        throw new Exception("【POST】 {" + apiUrl + "} 请求出错！");
    }

    /**
     * 使用httpclient发送请求
     */
    public String getForHttpClient(String baseUrls, String apiUrl, String accessKeyId, String accessKeySecret)
            throws Exception {
        List<String> baseUrlList = Splitter.on(",").splitToList(baseUrls);
        Exception exception = null;
        for (String baseUrl : baseUrlList) {
            if (StringUtils.isBlank(baseUrl)) {
                continue;
            }
            try {
                String url = buildUrl(baseUrl, apiUrl);
                if (null == url) {
                    return null;
                }
                String date = formatDate();
                String authorization = getAuthorization("GET", "", "", date, apiUrl, null,
                        accessKeyId, accessKeySecret);
                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader("Authorization", authorization);
                httpGet.addHeader("Date", date);
                HttpResponse response = client.execute(httpGet);
                String result = EntityUtils.toString(response.getEntity());
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException(result);
                }
                return result;
            } catch (Exception e) {
                exception = e;
            }
        }
        if (null != exception) {
            throw new Exception(exception);
        }
        throw new Exception("【GET】 {" + apiUrl + "} 请求出错！");
    }

    /**
     * 获取签名
     */
    private String getAuthorization(String method, String md5Content, String contentType, String date, String apiUrl,
                                    Map<String, Object> params, String accessKeyId, String accessKeySecret) {
        try {
            StringBuilder canonString = new StringBuilder("\n");
            if (params != null && params.size() > 0) {
                params = sortMapByKey(params);
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    canonString.append(entry.getKey().toLowerCase()).append(':').append(entry.getValue()).append('\n');
                }
            }
            if (apiUrl.contains("?")) {
                String[] apiArray = apiUrl.split("\\?");
                apiUrl = apiArray[0];
                if (apiArray.length == 2) {
                    String[] apiItem = apiArray[1].split("&");
                    Arrays.sort(apiItem);
                    StringBuilder sb = new StringBuilder();
                    for (String item : apiItem) {
                        if (sb.length() == 0) {
                            sb.append(item);
                        } else {
                            sb.append("&").append(item);
                        }
                    }
                    apiUrl += '?' + sb.toString();
                }
            }
            String stringToSign = method + "\n" + md5Content + "\n" + contentType + "\n" + date
                    + canonString + apiUrl;
            byte[] keyBytes = accessKeySecret.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(stringToSign.getBytes());
            String signature = org.apache.commons.codec.binary.Base64.encodeBase64String(rawHmac);
            return "Tianji " + accessKeyId + ":" + signature;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * map按key排序
     */
    private Map<String, Object> sortMapByKey(Map<String, Object> params) {
        return new TreeMap<>(params);
    }

    /**
     * md5加密
     */
    private String md5String(String body) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(body.getBytes());
        return org.apache.commons.codec.binary.Base64.encodeBase64String(md.digest());
    }

    /**
     * 返回指定格式日期
     */
    private String formatDate() {
        return DateTime.now(DateTimeZone.UTC).toString("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
    }

    private String buildUrl(String baseUrl, String apiUrl) {
        String result = null;
        if (null == apiUrl || apiUrl.isEmpty()) {
            return null;
        }
        String[] apiResourceArr = apiUrl.split("\\?");
        StringBuilder sb = new StringBuilder(baseUrl);
        try {
            String[] apiPathArr = apiResourceArr[0].split("/");
            for (String apiPath : apiPathArr) {
                if (null != apiPath && !apiPath.isEmpty()) {
                    sb.append("/").append(URLEncoder.encode(apiPath, "UTF-8"));
                }
            }
            result = sb.toString();
            if (apiResourceArr.length > 1) {
                sb.append("?");
                String[] paramsArray = apiResourceArr[1].split("&");
                for (String param : paramsArray) {
                    String[] arrays = param.split("=");
                    if (arrays.length == 1) {
                        sb.append(arrays[0]).append("=").append("&");
                    } else {
                        sb.append(URLEncoder.encode(arrays[0], "UTF-8"))
                                .append("=").append(URLEncoder.encode(arrays[1], "UTF-8"))
                                .append("&");
                    }
                }
                result = sb.substring(0, sb.length() - 1);
            }
        } catch (UnsupportedEncodingException e) {

        }
        return result;
    }
}

