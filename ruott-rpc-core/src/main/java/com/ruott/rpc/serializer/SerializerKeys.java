package com.ruott.rpc.serializer;

public interface SerializerKeys {

    String JDK = "jdk";
    String HESSIAN = "hessian";
    String JSON = "json";
    String KRYO = "kryo";

    //SPI自定义序列化器
    String SPI = "spi";

    static String getSerializer(String serializerKey) {
        String[] keys = new String[]{JDK, HESSIAN, JSON, KRYO};
        for (String key : keys) {
            if (serializerKey.equals(key)) {
                return key;
            }
        }
        return SPI;
    }
}
