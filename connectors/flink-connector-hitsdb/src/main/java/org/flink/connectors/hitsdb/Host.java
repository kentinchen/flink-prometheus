package org.flink.connectors.hitsdb;

import org.flink.connectors.hitsdb.config.NotEnoughParamsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Host implements Serializable {
    private final String name;
    private final int port;

    public Host(String name, int port) {
        this.name = name;
        this.port = port;
    }

    public static List<Host> parseHosts(String hostString) {
        String[] hostPairs = hostString.split(",");
        List<Host> hosts = new ArrayList<>(hostPairs.length);
        for (String hostPair : hostPairs) {
            String[] strings = hostPair.split(":");
            if (strings.length != 2)
                throw new NotEnoughParamsException("host string must formatted as 'host:port', but is " + hostPair);
            hosts.add(new Host(strings[0].trim(), Integer.parseInt(strings[1].trim())));
        }
        return hosts;
    }

    public String getName() {
        return this.name;
    }

    public int getPort() {
        return this.port;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Host))
            return false;
        Host host = (Host) o;
        return (getPort() == host.getPort() &&
                Objects.equals(getName(), host.getName()));
    }

    public int hashCode() {
        return Objects.hash(getName(), getPort());
    }

    public String toString() {
        return "Host{name='" + this.name + '\'' + ", port=" + this.port + '}';
    }
}
