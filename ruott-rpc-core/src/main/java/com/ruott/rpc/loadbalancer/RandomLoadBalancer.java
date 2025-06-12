package com.ruott.rpc.loadbalancer;


import com.ruott.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机负载均衡器
 */
public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> param, List<ServiceMetaInfo> serviceMetaInfos) {
        if (serviceMetaInfos == null || serviceMetaInfos.isEmpty())
            return null;
        //如果只有一个节点不需要负载均衡
        int size = serviceMetaInfos.size();
        if (size == 1)
            return serviceMetaInfos.get(0);

        //随机算法
        return serviceMetaInfos.get(random.nextInt(size));
    }
}
