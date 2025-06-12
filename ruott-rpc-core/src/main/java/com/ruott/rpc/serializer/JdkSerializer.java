package com.ruott.rpc.serializer;

import com.ruott.rpc.exception.RuottRpcException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * JDK序列化器
 */
@Slf4j
public class JdkSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(obj);
            return outputStream.toByteArray();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<?> type) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuottRpcException(e);
        }
    }
}
