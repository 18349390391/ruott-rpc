package com.ruott.rpc.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruott.rpc.model.RpcRequest;
import com.ruott.rpc.model.RpcResponse;

import java.io.IOException;


/**
 * json序列化器
 */

public class JsonSerializer implements Serializer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 序列化
     *
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }


    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<?> type) throws IOException {
        T obj = (T) OBJECT_MAPPER.readValue(bytes, type);
        if (obj instanceof RpcRequest) {
            return (T) handleRequest((RpcRequest) obj, type);
        }
        if (obj instanceof RpcResponse) {
            return (T) handleResponse((RpcResponse) obj, type);
        }
        return obj;
    }


    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException {
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        //循环每一个参数的类型
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazz = parameterTypes[i];
            //如果类型不同，则重新处理一下类型
            if (!clazz.isAssignableFrom(args[i].getClass())) {
                byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(bytes, clazz);
            }
        }
        return type.cast(rpcRequest);
    }

    /**
     * 由于object的原始对象会被擦除，导致反序列化时会被作为LinkedHasMap 无法转换为原始对象，因此这里做类特殊处理
     *
     * @param obj
     * @param type
     * @param <T>
     * @return
     */
    private <T> T handleResponse(RpcResponse obj, Class<T> type) throws IOException {
        byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(obj.getData());
        obj.setData(OBJECT_MAPPER.readValue(bytes, obj.getDataType()));
        return type.cast(obj);
    }
}
