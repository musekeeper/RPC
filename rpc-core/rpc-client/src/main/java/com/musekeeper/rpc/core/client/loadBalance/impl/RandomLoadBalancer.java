package com.musekeeper.rpc.core.client.loadBalance.impl;

import com.musekeeper.rpc.core.client.loadBalance.LoadBalancer;
import com.musekeeper.rpc.core.client.registry.instance.ServiceInstance;
import com.musekeeper.rpc.core.common.enums.loadBalance.LoadBalanceEnum;


import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author musekeeper
 * random load balancer
 */
public class RandomLoadBalancer implements LoadBalancer {
    ThreadLocalRandom current = ThreadLocalRandom.current();
    /**
     * Random Load Balancer
     * @param requestParams request parameters
     * @param instancesMap  service instances map
     * @return selected service instance
     */
    @Override
    public ServiceInstance select(Object requestParams, Map<String, ServiceInstance> instancesMap) {
        int size = instancesMap.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return instancesMap.values().iterator().next();
        }
        return instancesMap.values().toArray(new ServiceInstance[0])[current.nextInt(size)];
    }

    /**
     * 获取负载均衡器类型
     * @return 负载负载均衡器类型
     */
    @Override
    public LoadBalanceEnum getLoadBalanceEnum() {
        return LoadBalanceEnum.RANDOM;
    }
}
