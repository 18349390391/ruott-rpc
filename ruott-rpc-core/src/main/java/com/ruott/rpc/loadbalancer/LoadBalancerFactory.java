package com.ruott.rpc.loadbalancer;

import com.ruott.rpc.spi.SpiLoader;

/**
 * 轮询工厂
 */
public class LoadBalancerFactory {

    static{
        SpiLoader.load(LoadBalancer.class);
    }

    public static LoadBalancer getLoadBalancer(String loadBalancerName) {
        return SpiLoader.getInstance(loadBalancerName,LoadBalancer.class);
    }
}
