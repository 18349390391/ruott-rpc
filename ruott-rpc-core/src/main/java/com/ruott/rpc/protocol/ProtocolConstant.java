package com.ruott.rpc.protocol;

/**
 * 请求消息常量
 */
public interface ProtocolConstant {

    //消息头长度
    int PROTOCOL_HEAD_LENGTH = 18;

    //固定请求头魔数
    byte[] PROTOCOL_MAGIC = {(byte) 0x72, (byte) 0x74};

    //版本号
    byte PROTOCOL_VERSION = 0x10;

    //描述数据长度的位置 第14个开始，后面4个字节
    byte DATA_LENGTH_ADDRESS = 14;
}
