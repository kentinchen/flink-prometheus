package org.flink.connectors.hitsdb.sink;

import com.alibaba.blink.connectors.hitsdb.HiTSDBOutputFormat;
import com.alibaba.blink.streaming.connectors.common.output.TupleOutputFormatAdapterSink;
import com.alibaba.blink.streaming.connectors.common.source.SourceUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.table.api.RichTableSchema;
import org.apache.flink.table.connector.DefinedDistribution;
import org.apache.flink.table.sinks.BatchCompatibleStreamTableSink;
import org.apache.flink.table.sinks.TableSink;
import org.apache.flink.table.sinks.TableSinkBase;
import org.apache.flink.table.sinks.UpsertStreamTableSink;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.DataTypes;
import org.apache.flink.table.types.InternalType;
import org.apache.flink.types.Row;
import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\006\001\005]f\001B\001\003\001=\021q\002S5U'\022\023E+\0312mKNKgn\033\006\003\007\021\tAa]5oW*\021QAB\001\007Q&$8\017\0322\013\005\035A\021AC2p]:,7\r^8sg*\021\021BC\001\006E2Lgn\033\006\003\0271\tq!\0317jE\006\024\027MC\001\016\003\r\031w.\\\002\001'\031\001\001CF\036?\003B\021\021\003F\007\002%)\t1#A\003tG\006d\027-\003\002\026%\t1\021I\\=SK\032\0042a\006\022%\033\005A\"BA\r\033\003\025\031\030N\\6t\025\tYB$A\003uC\ndWM\003\002\036=\005)a\r\\5oW*\021q\004I\001\007CB\f7\r[3\013\003\005\n1a\034:h\023\t\031\003DA\007UC\ndWmU5oW\n\0137/\032\t\005K1rS'D\001'\025\t9\003&A\003ukBdWM\003\002*U\005!!.\031<b\025\tYC$A\002ba&L!!\f\024\003\rQ+\b\017\\33!\ty3'D\0011\025\t\t$'\001\003mC:<'\"A\025\n\005Q\002$a\002\"p_2,\027M\034\t\003mej\021a\016\006\003qq\tQ\001^=qKNL!AO\034\003\007I{w\017E\002\030y\021J!!\020\r\003=\t\013Go\0315D_6\004\030\r^5cY\026\034FO]3b[R\013'\r\\3TS:\\\007cA\f@k%\021\001\t\007\002\026+B\034XM\035;TiJ,\027-\034+bE2,7+\0338l!\t\021U)D\001D\025\t!%$A\005d_:tWm\031;pe&\021ai\021\002\024\t\0264\027N\\3e\t&\034HO]5ckRLwN\034\005\t\021\002\021)\031!C\001\023\006\031r.\036;qkR4uN]7bi\n+\030\016\0343feV\t!\n\005\002L\037:\021A*T\007\002\t%\021a\nB\001\023\021&$6\013\022\"PkR\004X\017\036$pe6\fG/\003\002Q#\n9!)^5mI\026\024(B\001(\005\021!\031\006A!A!\002\023Q\025\001F8viB,HOR8s[\006$()^5mI\026\024\b\005\003\005V\001\t\025\r\021\"\001W\003\031\0318\r[3nCV\tq\013\005\002Y56\t\021L\003\002,5%\0211,\027\002\020%&\034\007\016V1cY\026\0346\r[3nC\"AQ\f\001B\001B\003%q+A\004tG\",W.\031\021\t\013}\003A\021\0011\002\rqJg.\033;?)\r\t7\r\032\t\003E\002i\021A\001\005\006\021z\003\rA\023\005\b+z\003\n\0211\001X\021\0251\007\001\"\025h\003\021\031w\016]=\026\003YAq!\033\001A\002\023%!.\001\tqCJ$\030\016^5p]\026$g)[3mIV\t1\016\005\002m_:\021\021#\\\005\003]J\ta\001\025:fI\0264\027B\0019r\005\031\031FO]5oO*\021aN\005\005\bg\002\001\r\021\"\003u\003Q\001\030M\035;ji&|g.\0323GS\026dGm\030\023fcR\021Q\017\037\t\003#YL!a\036\n\003\tUs\027\016\036\005\bsJ\f\t\0211\001l\003\rAH%\r\005\007w\002\001\013\025B6\002#A\f'\017^5uS>tW\r\032$jK2$\007\005C\004~\001\001\007I\021\002@\002!}\033\b.\0364gY\026,U\016\035;z\027\026LX#A@\021\007E\t\t!\003\0025%!I\021Q\001\001A\002\023%\021qA\001\025?NDWO\0324mK\026k\007\017^=LKf|F%Z9\025\007U\fI\001\003\005z\003\007\t\t\0211\001\000\021\035\ti\001\001Q!\n}\f\021cX:ik\0324G.Z#naRL8*Z=!\021\035\t\t\002\001C\001\003'\t1c]3u!\006\024H/\033;j_:,GMR5fY\022$2!^A\013\021\031I\027q\002a\001W\"9\021\021\004\001\005\002\005m\021AE:fiNCWO\0324mK\026k\007\017^=LKf$2!^A\017\021\035\ty\"a\006A\002}\fqb\0355vM\032dW-R7qif\\U-\037\005\b\003G\001A\021IA\023\003I9W\r\036)beRLG/[8o\r&,G\016Z:\025\005\005\035\002\003B\t\002*-L1!a\013\023\005\025\t%O]1z\021\035\ty\002\001C!\003_!\022a \005\b\003g\001A\021IA\033\0031\031X\r^&fs\032KW\r\0343t)\r)\030q\007\005\t\003s\t\t\0041\001\002(\005!1.Z=t\021\035\ti\004\001C!\003\tqb]3u\023N\f\005\017]3oI>sG.\037\013\004k\006\005\003bBA\"\003w\001\rAL\001\rSN\f\005\017]3oI>sG.\037\005\b\003\017\002A\021IA%\00359W\r\036*fG>\024H\rV=qKV\021\0211\n\t\005\003\033\n\t&\004\002\002P)\021\001HG\005\005\003'\nyE\001\005ECR\fG+\0379f\021\035\t9\006\001C!\0033\na\"Z7ji\022\013G/Y*ue\026\fW\016\006\003\002\\\0055\004#BA/\003S\"SBAA0\025\021\t\t'a\031\002\025\021\fG/Y:ue\026\fWNC\002,\003KR1!a\032\035\003%\031HO]3b[&tw-\003\003\002l\005}#A\004#bi\006\034FO]3b[NKgn\033\005\t\003_\n)\0061\001\002r\005QA-\031;b'R\024X-Y7\021\013\005u\0231\017\023\n\t\005U\024q\f\002\013\t\006$\030m\025;sK\006l\007bBA=\001\021\005\0231P\001\022K6LGOQ8v]\022,Gm\025;sK\006lG\003BA.\003{B\001\"a \002x\001\007\021\021O\001\016E>,h\016Z3e'R\024X-Y7\t\017\005\r\005\001\"\021\002\006\006iq-\032;GS\026dGMT1nKN,\"!a\n\t\017\005%\005\001\"\021\002\f\006iq-\032;GS\026dG\rV=qKN,\"!!$\021\013E\tI#a\023\b\023\005E%!!A\t\002\005M\025a\004%j)N#%\tV1cY\026\034\026N\\6\021\007\t\f)J\002\005\002\005\005\005\t\022AAL'\r\t)\n\005\005\b?\006UE\021AAN)\t\t\031\n\003\006\002 \006U\025\023!C\001\003C\0131\004\n7fgNLg.\033;%OJ,\027\r^3sI\021,g-Y;mi\022\022TCAARU\r9\026QU\026\003\003O\003B!!+\00246\021\0211\026\006\005\003[\013y+A\005v]\016DWmY6fI*\031\021\021\027\n\002\025\005tgn\034;bi&|g.\003\003\0026\006-&!E;oG\",7m[3e-\006\024\030.\0318dK\002")
public class HiTSDBTableSink implements TableSinkBase<Tuple2<Boolean, Row>>,
        BatchCompatibleStreamTableSink<Tuple2<Boolean, Row>>,
        UpsertStreamTableSink<Row>, DefinedDistribution {
  private HiTSDBOutputFormat.Builder outputFormatBuilder;
  
  private RichTableSchema schema;
  
  private String partitionedField;
  
  private boolean _shuffleEmptyKey;
  
  private Option<String[]> org$apache$flink$table$sinks$TableSinkBase$$fieldNames;
  
  private Option<DataType[]> org$apache$flink$table$sinks$TableSinkBase$$fieldTypes;
  
  public DataType getOutputType() {
    return getOutputType();
  }
  
  public Option<String[]> org$apache$flink$table$sinks$TableSinkBase$$fieldNames() {
    return this.org$apache$flink$table$sinks$TableSinkBase$$fieldNames;
  }
  
  public void org$apache$flink$table$sinks$TableSinkBase$$fieldNames_$eq(Option<String[]> x$1) {
    this.org$apache$flink$table$sinks$TableSinkBase$$fieldNames = x$1;
  }
  
  public Option<DataType[]> org$apache$flink$table$sinks$TableSinkBase$$fieldTypes() {
    return this.org$apache$flink$table$sinks$TableSinkBase$$fieldTypes;
  }
  
  public void org$apache$flink$table$sinks$TableSinkBase$$fieldTypes_$eq(Option<DataType[]> x$1) {
    this.org$apache$flink$table$sinks$TableSinkBase$$fieldTypes = x$1;
  }
  
  public InternalType[] getFieldInternalTypes() {
    return getFieldInternalTypes();
  }
  
  public final TableSink<Tuple2<Boolean, Row>> configure(String[] fieldNames, DataType[] fieldTypes) {
    return configure(fieldNames, fieldTypes);
  }
  
  public HiTSDBOutputFormat.Builder outputFormatBuilder() {
    return this.outputFormatBuilder;
  }
  
  public HiTSDBTableSink(HiTSDBOutputFormat.Builder outputFormatBuilder, RichTableSchema schema) {
    //TableSinkBase.class.$init$(this);
    //UpsertStreamTableSink.class.$init$(this);
    this.outputFormatBuilder = outputFormatBuilder;
    this.schema = schema;
    this.partitionedField = null;
    this._shuffleEmptyKey = true;
  }
  
  public RichTableSchema schema() {
    return this.schema;
  }
  
  public TableSinkBase<Tuple2<Boolean, Row>> copy() {
    HiTSDBTableSink sink = new HiTSDBTableSink(outputFormatBuilder(), schema());
    sink.partitionedField_$eq(partitionedField());
    sink._shuffleEmptyKey_$eq(_shuffleEmptyKey());
    return sink;
  }
  
  private String partitionedField() {
    return this.partitionedField;
  }
  
  private void partitionedField_$eq(String x$1) {
    this.partitionedField = x$1;
  }
  
  private boolean _shuffleEmptyKey() {
    return this._shuffleEmptyKey;
  }
  
  private void _shuffleEmptyKey_$eq(boolean x$1) {
    this._shuffleEmptyKey = x$1;
  }
  
  public void setPartitionedField(String partitionedField) {
    partitionedField_$eq(partitionedField);
  }
  
  public void setShuffleEmptyKey(boolean shuffleEmptyKey) {
    _shuffleEmptyKey_$eq(shuffleEmptyKey);
  }
  
  public String[] getPartitionFields() {
    (new String[1])[0] = partitionedField();
    return (partitionedField() == null) ? null : new String[1];
  }
  
  public boolean shuffleEmptyKey() {
    return _shuffleEmptyKey();
  }
  
  public void setKeyFields(String[] keys) {}
  
  public void setIsAppendOnly(Boolean isAppendOnly) {}
  
  public DataType getRecordType() {
    return (DataType)DataTypes.createRowTypeV2(getFieldTypes(), getFieldNames());
  }
  
  public DataStreamSink<Tuple2<Boolean, Row>> emitDataStream(DataStream dataStream) {
    outputFormatBuilder().setRowTypeInfo(SourceUtils.toRowTypeInfo(getRecordType()));
    TupleOutputFormatAdapterSink sink = new TupleOutputFormatAdapterSink(outputFormatBuilder().build());
    return dataStream.addSink(sink).name(sink.toString());
  }
  
  public DataStreamSink<Tuple2<Boolean, Row>> emitBoundedStream(DataStream boundedStream) {
    outputFormatBuilder().setRowTypeInfo(SourceUtils.toRowTypeInfo(getRecordType()));
    HiTSDBOutputFormat outputFormat = outputFormatBuilder().build();
    return boundedStream.writeUsingOutputFormat(outputFormat).name(String.format("%s-%s", outputFormat.toString(), "batch"));
  }
  
  public String[] getFieldNames() {
    return (schema() == null) ?       getFieldNames() : schema().getColumnNames();
  }
  
  public DataType[] getFieldTypes() {
    return (schema() == null) ?       getFieldTypes() : schema().getColumnTypes();
  }
  
  public static RichTableSchema $lessinit$greater$default$2() {
    return HiTSDBTableSink$.MODULE$.$lessinit$greater$default$2();
  }
}

public final class HiTSDBTableSink$ {
  public static final HiTSDBTableSink$ MODULE$ = new HiTSDBTableSink$();

  private HiTSDBTableSink$() {
  }

  public RichTableSchema $lessinit$greater$default$2() {
    return null;
  }
}