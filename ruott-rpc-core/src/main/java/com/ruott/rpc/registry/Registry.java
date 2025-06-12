package com.ruott.rpc.registry;


import com.ruott.rpc.config.RegistryConfig;
import com.ruott.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心 - 接口
 */
public interface Registry {

    //初始化
    void init(RegistryConfig registryConfig);

    //服务注册
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    //服务发现
    List<ServiceMetaInfo> discovery(String serviceName);

    //服务销毁
    void destroy();

    //心跳检测
    void heartbeatCheck();

    //服务监听
    void listen(String nodeKey);
}
