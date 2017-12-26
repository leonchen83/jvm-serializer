package com.creative.commons.utils.benchmark;

import com.creative.commons.utils.JsonCodec;
import com.creative.model.Father;
import com.creative.model.Message;
import com.creative.model.Son;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import org.openjdk.jmh.annotations.Benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author xinyuzhou.zxy
 */
public class FastJsonBenchmark {

    protected static Message msg1 = new Message(
        "Most message-oriented middleware (MOM) products treat messages as lightweight entities that consist of a header and a body. The header contains fields used for message routing and identification; the body contains the application data being sent.");
    protected static Message msg2 = new Message(new Father());
    protected static Message msg3 = new Message(new Son());

    protected static Map<String, Object> props = Maps.newHashMap();

    protected static Date now = Calendar.getInstance().getTime();
    protected static BigDecimal money = BigDecimal.valueOf(110.13);
    protected static TimeZone tz = Calendar.getInstance().getTimeZone();
    protected static Timestamp tt = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());

    static {
        props.put("boolean", Boolean.TRUE);
        props.put("byte", Byte.MAX_VALUE);
        props.put("short", Short.MAX_VALUE);
        props.put("int", Integer.MAX_VALUE);
        props.put("long", Long.MAX_VALUE);
        props.put("float", Float.MAX_EXPONENT);
        props.put("double", Double.MAX_EXPONENT);
        props.put("char", Character.MAX_VALUE);
        props.put("String", String.valueOf("VonGosling"));
        props.put("trait", Lists.newArrayListWithCapacity(1000));
        props.put("case", Objects.toString(msg1));

        //Some particular types
        props.put("bigDecimal", money);
        props.put("date", now);
        //props.put("timesZone", tz);
        props.put("timestamp", tt);

        msg1.setProperties(props);
    }

    @Benchmark
    public void jsonCodecMultiTest() {
        byte[] obj1 = JsonCodec.encode(msg1);
        Message msg1 = JsonCodec.decode(obj1);

        byte[] obj2 = JsonCodec.encode(msg2);
        Message msg2 = JsonCodec.decode(obj2);

        byte[] obj3 = JsonCodec.encode(msg3);
        Message msg3 = JsonCodec.decode(obj3);
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
            .include(FastJsonBenchmark.class.getSimpleName())
            .warmupIterations(10)
            .measurementIterations(20)
            .forks(1)
            .build();

        new Runner(opt).run();
    }
}
