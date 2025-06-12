package com.ruott.rpc.loadbalancer;

/**
 * 负载均衡keys
 */
public interface LoadBalancerKeys {

    String RANDOM = "random";

    String ROUND_ROBIN = "roundRobin";

    String CONSISTENT_HASH = "consistentHash";
}
