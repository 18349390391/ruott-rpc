package com.ruott.rpc.registry;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.ruott.rpc.config.RegistryConfig;
import com.ruott.rpc.exception.RuottRpcException;
import com.ruott.rpc.model.ServiceMetaInfo;
import com.ruott.rpc.utils.StrCapture;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * etcd 注册中心
 */

@Slf4j
public class RegistryEtcd implements Registry {

    private Client client;

    private KV kvClient;

    //路径
    private static final String ETCD_ROOT_PATH = "/ruott/rpc/";

    //本地节点key集合
    private final Set<String> loadNodeRegistryKeys = new ConcurrentHashSet<>();

    //监听key集合
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();


    /**
     * 初始化
     *
     * @param registryConfig
     */
    @Override
    public void init(RegistryConfig registryConfig) {
        //获取client
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        //获取kvClient
        kvClient = client.getKVClient();
        //开起续约定时任务
        this.heartbeatCheck();
    }

    /**
     * 服务注册
     *
     * @param serviceMetaInfo
     * @return
     */
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //创建lease客户端
        Lease leaseClient = client.getLeaseClient();
        try {
            //获取租约id
            long leaseId = leaseClient.grant(30L).get().getID();

            //key
            String keyStr = ETCD_ROOT_PATH + serviceMetaInfo.getServiceKey();
            ByteSequence etcdKey = ByteSequence.from(keyStr, StandardCharsets.UTF_8);

            //value
            ByteSequence etcdValue = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

            PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();

            kvClient.put(etcdKey, etcdValue, putOption).get();
            loadNodeRegistryKeys.add(keyStr);
        } catch (Exception e) {
            throw new RuottRpcException(e);
        }
    }

    /**
     * 服务发现
     *
     * @param serviceName
     * @return
     */
    @Override
    public List<ServiceMetaInfo> discovery(String serviceName) {
        //优先读取缓存
        List<ServiceMetaInfo> serviceMetaInfos = RegistryCache.readCache(serviceName);
        if (serviceMetaInfos != null)
            return serviceMetaInfos;

        ByteSequence key = ByteSequence.from(ETCD_ROOT_PATH + serviceName, StandardCharsets.UTF_8);
        //按照前缀查询
        GetOption getOption = GetOption.builder().isPrefix(true).build();
        try {
            //查询出所有符合前缀的key value
            List<KeyValue> kvs = kvClient.get(key, getOption).get().getKvs();
            if (kvs == null || kvs.isEmpty()) return null;
            //将value取出返回一个list集合
            List<ServiceMetaInfo> list = kvs.stream().map(kv -> {
                //调用服务监听
                this.listen(kv.getKey().toString(StandardCharsets.UTF_8));
                String value = kv.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).toList();

            //写入缓存
            RegistryCache.writeCache(serviceName, list);
            return list;
        } catch (Exception e) {
            throw new RuottRpcException(e);
        }
    }

    /**
     * 服务销毁
     */
    @Override
    public void destroy() {
        log.info("etcd service discovery closed");
        if (loadNodeRegistryKeys.isEmpty()) return;
        loadNodeRegistryKeys.forEach(loadNode -> {
            try {
                kvClient.delete(ByteSequence.from(loadNode, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuottRpcException(e);
            }
        });
        //释放资源
        if (client != null) client.close();
        if (kvClient != null) kvClient.close();

    }

    /**
     * 心跳检测
     */
    @Override
    public void heartbeatCheck() {
        CronUtil.schedule("*/10 *  * * * *", (Task) () -> {
            //遍历本地所有节点
            for (String loadNodeKey : loadNodeRegistryKeys) {
                try {
                    List<KeyValue> kvs = kvClient.get(ByteSequence.from(loadNodeKey, StandardCharsets.UTF_8)).get().getKvs();
                    //本地注册缓存为null 不需要续约
                    if (kvs == null || kvs.isEmpty()) continue;
                    //续签
                    ByteSequence value = kvs.get(0).getValue();
                    ServiceMetaInfo metaInfo = JSONUtil.toBean(value.toString(StandardCharsets.UTF_8), ServiceMetaInfo.class);
                    this.register(metaInfo);
                } catch (Exception e) {
                    log.info("etcd service heartbeat check error");
                }
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * 服务监听
     */
    @Override
    public void listen(String nodeKey) {
        Watch watchClient = client.getWatchClient();
        if (watchingKeySet.add(nodeKey)) {
            watchClient.watch(ByteSequence.from(nodeKey, StandardCharsets.UTF_8), watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()) {
                    if (event.getEventType() == WatchEvent.EventType.DELETE) {
                        KeyValue kv = event.getKeyValue();
                        if (kv == null || kv.getValue() == null) return;
                        String capture = StrCapture.capture(nodeKey);
                        RegistryCache.clearCache(capture);
                        log.info("etcd service listen delete serviceName: {}", capture);
                    }
                }
            });
        }
    }


}
