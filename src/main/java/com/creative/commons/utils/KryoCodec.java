package com.creative.commons.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.UnsafeInput;
import com.esotericsoftware.kryo.io.UnsafeOutput;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @author von gosling
 */
public abstract class KryoCodec {
    private static final ThreadLocal<Wrapper> kryos = new ThreadLocal<Wrapper>() {
        protected Wrapper initialValue() {
            Wrapper wrapper = new Wrapper();
            wrapper.kryo = new Kryo();
            wrapper.input = new UnsafeInput();
            wrapper.output = new UnsafeOutput(4096);
            wrapper.kryo.register(byte[].class);
            wrapper.kryo.register(char[].class);
            wrapper.kryo.register(short[].class);
            wrapper.kryo.register(int[].class);
            wrapper.kryo.register(long[].class);
            wrapper.kryo.register(float[].class);
            wrapper.kryo.register(double[].class);
            wrapper.kryo.register(boolean[].class);
            wrapper.kryo.register(String[].class);
            wrapper.kryo.register(Object[].class);
            wrapper.kryo.register(KryoSerializable.class);
            wrapper.kryo.register(BigInteger.class);
            wrapper.kryo.register(BigDecimal.class);
            wrapper.kryo.register(Class.class);
            wrapper.kryo.register(Date.class);
            //kryo.register(Enum.class);
            wrapper.kryo.register(EnumSet.class);
            wrapper.kryo.register(Currency.class);
            wrapper.kryo.register(StringBuffer.class);
            wrapper.kryo.register(StringBuilder.class);
            wrapper.kryo.register(Collections.EMPTY_LIST.getClass());
            wrapper.kryo.register(Collections.EMPTY_MAP.getClass());
            wrapper.kryo.register(Collections.EMPTY_SET.getClass());
            wrapper.kryo.register(Collections.singletonList(null).getClass());
            wrapper.kryo.register(Collections.singletonMap(null, null).getClass());
            wrapper.kryo.register(Collections.singleton(null).getClass());
            wrapper.kryo.register(TreeSet.class);
            wrapper.kryo.register(Collection.class);
            wrapper.kryo.register(TreeMap.class);
            wrapper.kryo.register(Map.class);
            try {
                wrapper.kryo.register(Class.forName("sun.util.calendar.ZoneInfo"));
            } catch (ClassNotFoundException e) {
                //Noop
            }
            wrapper.kryo.register(Calendar.class);
            wrapper.kryo.register(Locale.class);

            wrapper.kryo.register(BitSet.class);
            wrapper.kryo.register(HashMap.class);
            wrapper.kryo.register(Timestamp.class);
            wrapper.kryo.register(ArrayList.class);

            wrapper.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
            return wrapper;
        }
    };

    public static Wrapper getWrapper() {
        return kryos.get();
    }

    public static Object decode(byte[] bytes) throws Exception {
        return getWrapper().decode(bytes);
    }

    public static byte[] encode(Object object) throws Exception {
        return getWrapper().encode(object);
    }

    private static class Wrapper {
        private Kryo kryo;
        private Input input;
        private Output output;

        public Object decode(byte[] bytes) throws Exception {
            input.setBuffer(bytes);
            return kryo.readClassAndObject(input);
        }

        public byte[] encode(Object object) throws Exception {
            //4K
            output.clear();
            kryo.writeClassAndObject(output, object);
            return output.toBytes();
        }
    }
}
