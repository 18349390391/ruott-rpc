package com.ruott.rpc.protocol;


import com.ruott.rpc.serializer.Serializer;
import com.ruott.rpc.utils.ProtocolGetSerializer;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * TCP自定义协议 - 编码器
 */
public class ProtocolMessageEncoder {

    public static Buffer encode(ProtocolMessage<?> message) throws IOException {
        if (message == null || message.getHeader() == null)
            return Buffer.buffer();
        Buffer buffer = Buffer.buffer();
        //获取请求头
        ProtocolMessage.Header header = message.getHeader();
        buffer.appendBytes(header.getMagic()); //魔数
        buffer.appendByte(header.getVersion()); //版本号
        buffer.appendByte(header.getSerializer());//序列化器
        buffer.appendByte( header.getStatus()); //状态
        buffer.appendLong(header.getRequestId());
        buffer.appendByte(header.getType());

        //获取序列化器
        Serializer serializer = ProtocolGetSerializer.getSerializer(header.getSerializer());

        byte[] body = serializer.serialize(message.getBody());
        buffer.appendInt(body.length); //请求体长度
        buffer.appendBytes(body);

        return buffer;
    }
}
