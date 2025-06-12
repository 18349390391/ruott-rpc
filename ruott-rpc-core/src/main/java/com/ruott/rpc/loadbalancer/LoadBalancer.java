package com.ruott.rpc.loadbalancer;


import com.ruott.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器
 */
public interface LoadBalancer {


    /**
     * 选择服务调用
     * @param param 一致性哈希算法参数
     * @param serviceMetaInfos 获取的服务列表集合
     * @return 服务
     */
    ServiceMetaInfo select(Map<String,Object> param, List<ServiceMetaInfo> serviceMetaInfos);
}
