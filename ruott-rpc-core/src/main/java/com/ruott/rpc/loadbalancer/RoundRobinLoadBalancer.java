package com.ruott.rpc.loadbalancer;

import com.ruott.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final AtomicInteger atomicIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> param, List<ServiceMetaInfo> serviceMetaInfos) {
        if(serviceMetaInfos == null || serviceMetaInfos.isEmpty())
            return null;

        //如果只有一个节点不需要负载均衡
        int size = serviceMetaInfos.size();
        if (size == 1)
            return serviceMetaInfos.get(0);

        //取模轮询算法
        int index = atomicIndex.getAndIncrement() % size;
        return serviceMetaInfos.get(index);
    }
}
