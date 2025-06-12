package com.ruott.rpc.server;

import cn.hutool.core.util.IdUtil;
import com.ruott.rpc.RpcApplication;
import com.ruott.rpc.exception.RuottRpcException;
import com.ruott.rpc.model.RpcRequest;
import com.ruott.rpc.model.RpcResponse;
import com.ruott.rpc.model.ServiceMetaInfo;
import com.ruott.rpc.protocol.*;
import com.ruott.rpc.serializer.SerializerKeys;
import com.ruott.rpc.server.processor.VertxTcpPacketHandler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * vert.x tcp 客户端 发送请求
 */
public class VertxTcpClient {

    public static RpcResponse doRequest(RpcRequest request, ServiceMetaInfo metaInfo) throws ExecutionException, InterruptedException {
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();

        CompletableFuture<RpcResponse> future = new CompletableFuture<>();

        netClient.connect(metaInfo.getServicePort(),
                metaInfo.getServiceHost(),
                result -> {
                    if (result.succeeded()) {
                        //构造请求头
                        ProtocolMessage.Header header = getHeader();
                        try {
                            Buffer encode = ProtocolMessageEncoder.encode(new ProtocolMessage<>(header, request));
                            NetSocket socket = result.result();
                            //发起请求
                            socket.write(encode);

                            //处理响应
                            socket.handler(new VertxTcpPacketHandler(buffer -> {
                                try {
                                    //异步转同步处理
                                    ProtocolMessage<RpcResponse> protocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                    future.complete(protocolMessage.getBody());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        return future.get();
    }

    /**
     * 构造请求头
     *
     * @return
     */
    private static ProtocolMessage.Header getHeader() {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer(getSerializerKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        return header;
    }

    /**
     * 获取序列化器 key
     *
     * @return
     */
    private static byte getSerializerKey() {
        final String serializerKey = SerializerKeys.getSerializer(RpcApplication.getRpcConfig().getSerializer());
        final ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getByValue(serializerKey);
        if (serializerEnum == null)
            throw new RuottRpcException("There are no serializers that match");
        return (byte) serializerEnum.getKey();
    }

}
