package com.ruott.rpc.loadbalancer;


import com.ruott.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    //tree map
    private final TreeMap<Integer, ServiceMetaInfo> treeMap = new TreeMap<>();

    //虚拟节点
    private static final int VIRTUAL_NODE = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> param, List<ServiceMetaInfo> serviceMetaInfos) {
        if (serviceMetaInfos == null || serviceMetaInfos.isEmpty())
            return null;

        //每个创建虚拟节点
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfos) {
            for (int i = 0; i < VIRTUAL_NODE; i++) {
                int hashCode = this.hashCode(serviceMetaInfo.getRequestPath() + "#" + i);
                treeMap.put(hashCode, serviceMetaInfo);
            }
        }

        //根据参数计算hash值
        int hash = this.hashCode(param);

        //选择最接近大于等于的hash值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> node = treeMap.ceilingEntry(hash);

        if (node == null) {
            //返回顶部节点
            node = treeMap.firstEntry();
        }
        return node.getValue();
    }

    /**
     * 计算hash值
     *
     * @param param
     * @return
     */
    private int hashCode(Object param) {
        if (param == null) {
            return 0;
        }
        // 将 key 转为字符串（根据实际情况，也可以处理其他类型，这里假设常用字符串作为键 ）
        String keyStr = param.toString();
        final int offsetBasis = 0x811C9DC5;
        final int prime = 0x01000193;
        int hash = offsetBasis;
        byte[] bytes = keyStr.getBytes();
        for (byte b : bytes) {
            hash = (hash * prime) ^ (b & 0xFF);
        }
        // 处理哈希值可能为负数的情况，转为正数（根据需求决定是否需要 ）
        return hash & 0x7FFFFFFF;
    }
}
