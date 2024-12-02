package com.musekeeper.rpc.core.client.loadBalance.impl;

import com.musekeeper.rpc.core.client.loadBalance.LoadBalancer;
import com.musekeeper.rpc.core.client.registry.instance.ServiceInstance;
import com.musekeeper.rpc.core.common.enums.loadBalance.LoadBalanceEnum;


import java.util.Map;
import java.util.TreeMap;

/**
 * @author musekeeper
 * consistent hash load balancer
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {
    /**
     * 一致性Hash环，存放虚拟节点
     */
    private final TreeMap<Integer, ServiceInstance> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceInstance select(Object requestParams, Map<String, ServiceInstance> instancesMap) {
        if (instancesMap.isEmpty()) {
            return null;
        }
        // 构建虚拟节点环 每次都重新构建，因为服务列表可能会变化 为了能够及时感知到服务的变化
        for (ServiceInstance instance : instancesMap.values()) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(instance.getIp()+":"+instance.getPort() + "#" + i);
                virtualNodes.put(hash, instance);
            }
        }
        int hash = getHash(requestParams);

        // 选择最接近且大于hash的节点
        Map.Entry<Integer, ServiceInstance> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();


    }

    /**
     * 获取负载均衡算法类型
     * @return 负载均衡算法类型
     */
    @Override
    public LoadBalanceEnum getLoadBalanceEnum() {
        return LoadBalanceEnum.CONSISTENT_HASH;
    }

    /**
     * Hash算法
     *
     * @param key 键
     * @return hash值
     */
    private int getHash(Object key) {
        return key.hashCode();
    }


}
