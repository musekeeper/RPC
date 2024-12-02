package com.musekeeper.rpc.core.client.loadBalance;

import com.musekeeper.rpc.core.client.registry.instance.ServiceInstance;
import com.musekeeper.rpc.core.common.enums.loadBalance.LoadBalanceEnum;


import java.util.*;

/**
 * @author musekeeper
 * 接收一个缓存实例，根据负载均衡算法，选择一个实例进行调用
 */
public interface LoadBalancer {
    /**
     * 根据负载均衡算法，选择一个实例进行调用
     * @param requestParams 请求参数.哈希算法的key
     * @param instancesMap 缓存实例
     * @return 选择的实例
     */
    ServiceInstance select(Object requestParams,Map<String, ServiceInstance> instancesMap);

    /**
     * 获取负载均衡算法类型
     * @return 负载均衡算法类型
     */
    LoadBalanceEnum getLoadBalanceEnum();
}
