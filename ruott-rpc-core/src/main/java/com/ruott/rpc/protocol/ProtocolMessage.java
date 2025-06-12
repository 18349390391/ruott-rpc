package com.ruott.rpc.protocol;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 自定义协议
 * @param <T>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolMessage<T> {

    //请求头
    private Header header;

    //请求体
    private T body;

    /**
     * 请求头：共18字节 144bit
     */
    @Data
    public static class Header{
        //魔数 2字节 16bit
        private byte[] magic;

        //版本号 1字节 8bit
        private byte version;

        //序列化器 1字节 8bit
        private byte serializer;

        //状态 1字节 8bit
        private byte status;

        //请求id 8字节 64bit
        private Long requestId;

        //请求类型 1字节 8bit
        private byte type;

        //数据长度 4字节 32bit
        private Integer bodyLength;
    }
}
