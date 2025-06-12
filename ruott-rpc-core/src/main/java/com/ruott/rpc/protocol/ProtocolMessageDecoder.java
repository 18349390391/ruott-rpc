package com.ruott.rpc.protocol;


import com.ruott.rpc.exception.RuottRpcException;
import com.ruott.rpc.model.RpcRequest;
import com.ruott.rpc.model.RpcResponse;
import com.ruott.rpc.serializer.Serializer;
import com.ruott.rpc.utils.ProtocolGetSerializer;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;
import java.util.Arrays;

/**
 * TCP自定义协议 - 解码器
 */
public class ProtocolMessageDecoder {

    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte[] magic = buffer.getBytes(0, 2);
        if (!Arrays.equals(magic, ProtocolConstant.PROTOCOL_MAGIC)) {
            throw new RuottRpcException("illegal request");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(2));
        header.setSerializer(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setType(buffer.getByte(13));
        header.setBodyLength(buffer.getInt(ProtocolConstant.DATA_LENGTH_ADDRESS));

        //解决半包粘包问题
        byte[] body = buffer.getBytes(ProtocolConstant.PROTOCOL_HEAD_LENGTH, ProtocolConstant.PROTOCOL_HEAD_LENGTH + header.getBodyLength());

        //获取序列化器
        Serializer serializer = ProtocolGetSerializer.getSerializer(header.getSerializer());

        ProtocolMessageTypeEnum typeEnum = ProtocolMessageTypeEnum.getByKey(header.getType());
        if (typeEnum == null)
            throw new RuottRpcException("This type is not supported");

        return switch (typeEnum) {
            case REQUEST -> {
                RpcRequest request = serializer.deserialize(body, RpcRequest.class);
                yield new ProtocolMessage<RpcRequest>(header, request);
            }
            case RESPONSE -> {
                RpcResponse response = serializer.deserialize(body, RpcResponse.class);
                yield new ProtocolMessage<RpcResponse>(header, response);
            }
            default -> throw new RuottRpcException("不支持该类型");
        };

    }
}
