package com.ruott.rpc.bootstarter;

import com.ruott.rpc.RpcApplication;
import com.ruott.rpc.config.RpcConfig;
import com.ruott.rpc.model.ServiceMetaInfo;
import com.ruott.rpc.model.ServiceRegistryInfo;
import com.ruott.rpc.registry.LocalRegistry;
import com.ruott.rpc.registry.Registry;
import com.ruott.rpc.registry.RegistryFactory;
import com.ruott.rpc.server.Server;
import com.ruott.rpc.server.VertxTcpServer;

import java.util.List;


/**
 * 服务提供者初始化
 */
public class ProvideBootStarter {

    public static void init(List<ServiceRegistryInfo<?>> registryInfos) {
        //初始化
        RpcApplication.init();

        //获取全局配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //服务注册
        registryInfos.forEach(registryInfo -> {
            LocalRegistry.register(registryInfo.getServiceName(), registryInfo.getImplClass());
            //获取注册中心
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistrar());

            ServiceMetaInfo metaInfo = ServiceMetaInfo.builder()
                    .serviceName(registryInfo.getServiceName())
                    .serviceHost(rpcConfig.getHost())
                    .servicePort(rpcConfig.getPort())
                    .build();
            //服务注册
            try {
                registry.register(metaInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        //启动服务
        Server service = new VertxTcpServer();
        service.doService(rpcConfig.getPort());
    }
}
