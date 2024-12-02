package com.musekeeper.rpc.core.client.loadBalance.impl;

import com.musekeeper.rpc.core.client.loadBalance.LoadBalancer;
import com.musekeeper.rpc.core.client.registry.instance.ServiceInstance;
import com.musekeeper.rpc.core.common.enums.loadBalance.LoadBalanceEnum;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author musekeeper
 * weight随机负载均衡算法
 */
public class WeightRandomLoadBalancer implements LoadBalancer {

    @Override
    public ServiceInstance select(Object requestParams, Map<String, ServiceInstance> instancesMap) {
        int totalWeight = 0;
        for (ServiceInstance instance : instancesMap.values()) {
            totalWeight += (int) instance.getWeight(); // 获取实例的权重
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomWeight = random.nextInt(totalWeight); // 生成一个随机数在0到totalWeight之间

        for (ServiceInstance instance : instancesMap.values()) {
            randomWeight -= (int) instance.getWeight(); // 减去实例的权重
            if (randomWeight < 0) {
                return instance; // 当随机值小于0时，返回当前实例
            }
        }
        return null; // 这行代码理论上不会执行，但为了编译没有问题加上
    }

    /**
     * 负载均衡器类型
     * @return 负载均衡器类型
     */
    @Override
    public LoadBalanceEnum getLoadBalanceEnum() {
        return LoadBalanceEnum.WEIGHT_RANDOM;
    }
}
