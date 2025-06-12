package com.ruott.rpc.serializer;

import java.io.IOException;

/**
 * 序列化接口
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param obj
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes, Class<?> type) throws IOException;
}
