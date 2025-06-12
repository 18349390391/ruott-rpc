package com.ruott.rpc.config;

import com.ruott.rpc.config.retry.RetryConfig;
import com.ruott.rpc.loadbalancer.LoadBalancerKeys;
import com.ruott.rpc.retry.RetryStrategy;
import com.ruott.rpc.serializer.SerializerKeys;
import lombok.Data;


/**
 * RPC配置
 */

@Data
public class RpcConfig {

    //服务名称
    private String serverName = "ruott-rpc";

    //版本号
    private String version = RpcConstant.RUOTT_RPC_VERSION;

    //host
    private String host = "127.0.0.1";

    //端口号
    private int port = 28080;

    //mock
    private boolean mock = false;

    //序列化器 (默认是jdk的)
    private String serializer = SerializerKeys.KRYO;

    //负载均衡器 默认轮询
    private String loadBalance = LoadBalancerKeys.ROUND_ROBIN;

    //注册中心配置
    private RegistryConfig registryConfig = new RegistryConfig();

    //重试配置
    private RetryConfig retryConfig = new RetryConfig();
}
