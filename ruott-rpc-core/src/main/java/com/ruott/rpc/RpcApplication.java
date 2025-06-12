package com.ruott.rpc;

import com.ruott.rpc.config.RpcConfig;
import com.ruott.rpc.config.RpcConstant;
import com.ruott.rpc.exception.RuottRpcException;
import com.ruott.rpc.registry.Registry;
import com.ruott.rpc.registry.RegistryFactory;
import com.ruott.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 框架配置初始化
 */

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    //私有化构造方法
    private RpcApplication() {
    }

    /**
     * 自定义配置初始化
     *
     * @param config 配置类
     */
    public static void init(RpcConfig config) {
        rpcConfig = config;
        if (rpcConfig == null)
            throw new RuottRpcException("rpcConfig is null");
        log.info("rpc init config: {}", rpcConfig);
        //获取注册中心
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistrar());
        //注册中心初始化
        registry.init(rpcConfig.getRegistryConfig());
        log.info("rpc init registry: {}", registry);

        //JVM关闭时执行方法，释放注册中心资源
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    /**
     * 框架初始化
     */
    public static void init() {
        try {
            rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            //配置类加载失败使用默认配置
            rpcConfig = new RpcConfig();
        }
        init(rpcConfig);
    }

    /**
     * DCL单例设计模式 - 获取配置
     *
     * @return rpc config
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }

}
