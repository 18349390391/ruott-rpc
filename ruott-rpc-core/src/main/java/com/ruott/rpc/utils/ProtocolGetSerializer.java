package com.ruott.rpc.utils;

import com.ruott.rpc.RpcApplication;
import com.ruott.rpc.exception.RuottRpcException;
import com.ruott.rpc.protocol.ProtocolMessageSerializerEnum;
import com.ruott.rpc.serializer.Serializer;
import com.ruott.rpc.serializer.SerializerFactory;

public class ProtocolGetSerializer {

    public static Serializer getSerializer(int serializerEnum) {
        ProtocolMessageSerializerEnum protocolEnum = ProtocolMessageSerializerEnum.getByKey(serializerEnum);
        if (protocolEnum == null) {
            throw new RuntimeException("serializerEnum is null");
        }
        //查看是否等于自定义的序列化器SPI
        String serializerName = null;
        //如果是SPI则获取配置中心的序列化器
        if (protocolEnum.getKey() == serializerEnum) {
            serializerName = RpcApplication.getRpcConfig().getSerializer();
        } else {
            serializerName = protocolEnum.getValue();
        }

        Serializer serializer = SerializerFactory.getSerializer(serializerName);
        if(serializer == null)
            throw new RuottRpcException("This type of sequencer is not supported");
        return serializer;
    }

}
