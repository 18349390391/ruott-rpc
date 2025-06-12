package com.ruott.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC-请求实体类
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    //服务名称
    private String serviceName;

    //方法名称
    private String methodName;

    //请求参数类型列表
    private Class<?>[] parameterTypes;

    //请求参数
    private Object[] args;

}
