package com.ruott.rpc.serializer;

import com.ruott.rpc.spi.SpiLoader;

/**
 * 序列化工厂
 */
public class SerializerFactory {

    //spi加载序列化
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 获取序列化器
     *
     * @param serializerName
     * @return
     */
    public static Serializer getSerializer(String serializerName) {
        return SpiLoader.getInstance(serializerName, Serializer.class);
    }

}
