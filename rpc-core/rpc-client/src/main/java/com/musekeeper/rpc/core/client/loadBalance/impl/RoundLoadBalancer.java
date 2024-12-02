package com.musekeeper.rpc.core.client.loadBalance.impl;

import com.musekeeper.rpc.core.client.loadBalance.LoadBalancer;
import com.musekeeper.rpc.core.client.registry.instance.ServiceInstance;
import com.musekeeper.rpc.core.common.enums.loadBalance.LoadBalanceEnum;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author musekeeper
 * round robin 负载均衡算法
 */
public class RoundLoadBalancer implements LoadBalancer {
    private final LongAdder currentIndex = new LongAdder();

    @Override
    public ServiceInstance select(Object requestParams, Map<String, ServiceInstance> instancesMap) {

        if(instancesMap == null || instancesMap.isEmpty()){
            return null;
        }
        int size = instancesMap.size();
        if(size == 1){
            return instancesMap.values().iterator().next();
        }
        // 取模轮询
        currentIndex.increment();
        int index = currentIndex.intValue()%size;
        return instancesMap.values().toArray(new ServiceInstance[0])[index];
    }

    /**
     * 负载均衡器类型
     * @return 负载均衡器类型
     */
    @Override
    public LoadBalanceEnum getLoadBalanceEnum() {
        return LoadBalanceEnum.ROUND;
    }

}
