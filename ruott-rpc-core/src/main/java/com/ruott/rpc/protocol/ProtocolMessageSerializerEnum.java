package com.ruott.rpc.protocol;

import com.ruott.rpc.serializer.SerializerKeys;
import lombok.Getter;

/**
 * 序列化器枚举类
 */
@Getter
public enum ProtocolMessageSerializerEnum {

    JDK(0, SerializerKeys.JDK),
    JSON(1,SerializerKeys.JSON),
    HESSIAN(2, SerializerKeys.HESSIAN),
    KRYO(3, SerializerKeys.KRYO),
    SPI(4,SerializerKeys.SPI);


    private final int key;
    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据key获取枚举
     * @param key
     * @return
     */
    public static ProtocolMessageSerializerEnum getByKey(int key) {
        for (ProtocolMessageSerializerEnum protocolMessageSerializerEnum : ProtocolMessageSerializerEnum.values()) {
            if (protocolMessageSerializerEnum.getKey() == key) {
                return protocolMessageSerializerEnum;
            }
        }
        return null;
    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static ProtocolMessageSerializerEnum getByValue(String value) {
        for (ProtocolMessageSerializerEnum protocolMessageSerializerEnum : ProtocolMessageSerializerEnum.values()) {
            if (protocolMessageSerializerEnum.getValue().equals(value)) {
                return protocolMessageSerializerEnum;
            }
        }
        return null;
    }
}
