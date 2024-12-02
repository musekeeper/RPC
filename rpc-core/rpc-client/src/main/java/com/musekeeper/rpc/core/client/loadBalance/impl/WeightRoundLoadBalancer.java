package com.musekeeper.rpc.core.client.loadBalance.impl;

import com.musekeeper.rpc.core.client.loadBalance.LoadBalancer;
import com.musekeeper.rpc.core.client.registry.instance.ServiceInstance;
import com.musekeeper.rpc.core.common.enums.loadBalance.LoadBalanceEnum;

import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author musekeeper
 * weightRound负载均衡算法
 */
public class WeightRoundLoadBalancer implements LoadBalancer {
    private final LongAdder counter = new LongAdder();

    @Override
    public ServiceInstance select(Object requestParams, Map<String, ServiceInstance> instancesMap) {
        int totalWeight = 0;
        for (ServiceInstance instance : instancesMap.values()) {
            totalWeight += (int) instance.getWeight(); // 获取实例的权重
        }
        counter.increment();
        int number = Math.abs(counter.intValue()) % totalWeight;
        for (ServiceInstance instance : instancesMap.values()) {
            if(instance.getWeight() > number){
                return instance; // 当随机值小于0时，返回当前实例
            }
            number -= (int) instance.getWeight();
        }
        return null; // 这行代码理论上不会执行，但为了编译没有问题加上
    }

    /**
     * 负载均衡器类型
     * @return 负载均衡器类型
     */
    @Override
    public LoadBalanceEnum getLoadBalanceEnum() {
        return LoadBalanceEnum.WEIGHT_ROUND;
    }


}
