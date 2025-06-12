package com.ruott.customer.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ruott.rpc.serializer.Serializer;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MessagePackSerializer implements Serializer {

    private final ObjectMapper objectMapper;

    public MessagePackSerializer() {
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new SimpleModule())
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
    }

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if (obj == null) {
            return new byte[0];
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 将 MessagePacker 的创建移到 try 块内，确保它先被关闭
            try (MessagePacker packer = MessagePack.newDefaultPacker(baos)) {
                String json = objectMapper.writeValueAsString(obj);
                packer.packString(json);
                // 无需显式调用 flush()，因为 try-with-resources 会自动关闭 packer
            }
            // 此时 packer 已关闭，数据已写入 baos
            return baos.toByteArray();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<?> type) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(bais)) {
            String json = unpacker.unpackString();
            return (T)objectMapper.readValue(json, type);
        }
    }

}