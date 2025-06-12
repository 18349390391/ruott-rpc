package com.ruott.rpc.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC响应实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {

    //响应数据
    private Object data;

    //响应数据类型
    private Class<?> dataType;

    //响应状态码
    private Integer code;

    //响应信息
    private String message;

    //异常异常
    private Exception exception;
}
