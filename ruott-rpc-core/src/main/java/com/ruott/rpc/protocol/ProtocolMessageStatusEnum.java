package com.ruott.rpc.protocol;


import lombok.Getter;

/**
 * 状态枚举类
 */
@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok", 20);

    private final String text;
    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

}
