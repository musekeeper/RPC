package com.musekeeper.rpc.core.client.loadBalance.factory;


import com.musekeeper.rpc.core.client.loadBalance.LoadBalancer;

import com.musekeeper.rpc.core.client.loadBalance.impl.*;
import com.musekeeper.rpc.core.common.enums.loadBalance.LoadBalanceEnum;


/**
 * @author musekeeper
 * 构建负载均衡器工厂类
 */
public class LoadBalancerFactory {
    private LoadBalancerFactory() {
    }

    /**
     * 根据负载均衡类型获取负载均衡器
     * @param loadBalancerType 负载均衡类型
     * @return 负载均衡器
     */
    public static LoadBalancer getLoadBalancer(LoadBalanceEnum loadBalancerType) {
        switch (loadBalancerType) {
            case ROUND -> {
                return new RoundLoadBalancer();
            }
            case WEIGHT_ROUND -> {
                return new WeightRoundLoadBalancer();
            }
            case RANDOM -> {
                return new RandomLoadBalancer();
            }
            case WEIGHT_RANDOM -> {
                return new WeightRandomLoadBalancer();
            }
            case CONSISTENT_HASH -> {
                return new ConsistentHashLoadBalancer();
            }
            default -> {
                return new WeightRoundLoadBalancer();
            }

        }
    }
}
