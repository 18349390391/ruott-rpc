package com.ruott.rpc.registry;

import com.ruott.rpc.spi.SpiLoader;

/**
 * 注册中心工厂
 */
public class RegistryFactory {

    //初始化注册中心
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 获取注册中心
     *
     * @return
     */
    public static Registry getInstance(String registrarKey) {
        return SpiLoader.getInstance(registrarKey, Registry.class);
    }
}
