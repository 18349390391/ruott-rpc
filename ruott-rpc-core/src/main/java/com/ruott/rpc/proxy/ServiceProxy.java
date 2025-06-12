package com.ruott.rpc.proxy;

import com.ruott.rpc.RpcApplication;
import com.ruott.rpc.config.RpcConfig;
import com.ruott.rpc.loadbalancer.LoadBalancer;
import com.ruott.rpc.loadbalancer.LoadBalancerFactory;
import com.ruott.rpc.model.RpcRequest;
import com.ruott.rpc.model.RpcResponse;
import com.ruott.rpc.model.ServiceMetaInfo;
import com.ruott.rpc.registry.Registry;
import com.ruott.rpc.registry.RegistryFactory;
import com.ruott.rpc.retry.RetryStrategy;
import com.ruott.rpc.retry.RetryStrategyFactory;
import com.ruott.rpc.server.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * jdk动态代理
 */
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //构造请求
        RpcRequest request = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();


        //获取注册中心
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistrar());


        //创建轮询实例
        LoadBalancer loadBalancer = LoadBalancerFactory.getLoadBalancer(rpcConfig.getLoadBalance());
        //服务发现
        List<ServiceMetaInfo> discovery = registry.discovery(method.getDeclaringClass().getName());

        //获取服务
        ServiceMetaInfo metaInfo = loadBalancer.select(new HashMap<>() {{
            put("method", request.getMethodName());
        }}, discovery);

        //重试策略
        RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryConfig().getStrategy());
        //发起TCP请求 获取返回值
        RpcResponse response = retryStrategy.doRetry(() -> VertxTcpClient.doRequest(request, metaInfo));

        if (response == null)
            throw new RuntimeException("response is null");
        return response.getData();
    }
}
