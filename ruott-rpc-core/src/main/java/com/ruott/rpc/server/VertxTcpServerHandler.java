package com.ruott.rpc.server;

import com.ruott.rpc.exception.RuottRpcException;
import com.ruott.rpc.model.HttpStatus;
import com.ruott.rpc.model.RpcRequest;
import com.ruott.rpc.model.RpcResponse;
import com.ruott.rpc.protocol.ProtocolMessage;
import com.ruott.rpc.protocol.ProtocolMessageDecoder;
import com.ruott.rpc.protocol.ProtocolMessageEncoder;
import com.ruott.rpc.protocol.ProtocolMessageTypeEnum;
import com.ruott.rpc.registry.LocalRegistry;
import com.ruott.rpc.server.processor.VertxTcpPacketHandler;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.lang.reflect.Method;


/**
 * 处理请求
 */
public class VertxTcpServerHandler implements Handler<NetSocket> {

    @Override
    public void handle(NetSocket socket) {
        Handler<Buffer> packetHandler = new VertxTcpPacketHandler(buffer -> {
            try {
                //进行解码
                ProtocolMessage<RpcRequest> pmsRequest = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);

                //获取请求参数
                RpcRequest rpcRequest = pmsRequest.getBody();
                //构建响应体
                RpcResponse response = new RpcResponse();

                Class<?> clazz = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = clazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                method.setAccessible(true);
                Object resultObj = method.invoke(clazz.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());
                //构建响应体
                response.setData(resultObj);
                response.setDataType(method.getReturnType());
                response.setCode(HttpStatus.OK.getCode());
                response.setMessage(HttpStatus.OK.getMessage());

                //构建tcp协议传输对象
                ProtocolMessage.Header header = pmsRequest.getHeader();
                header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());

                Buffer responseBuffer = ProtocolMessageEncoder.encode(new ProtocolMessage<>(header, response));
                //响应
                socket.write(responseBuffer);
            } catch (Exception e) {
                throw new RuottRpcException(e);
            }
        });
        //处理连接
        socket.handler(packetHandler);
    }

}
