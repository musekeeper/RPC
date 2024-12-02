package com.musekeeper.rpc.core.client.config;

import com.musekeeper.rpc.core.client.registry.config.RegistryConfig;


import com.musekeeper.rpc.core.common.enums.fault.retry.RetryStrategyEnum;
import com.musekeeper.rpc.core.common.enums.fault.tolerant.TolerantStrategyEnum;
import com.musekeeper.rpc.core.common.enums.loadBalance.LoadBalanceEnum;
import com.musekeeper.rpc.core.common.enums.serialization.SerializationEnum;
import lombok.Data;

/**
 * @author musekeeper
 * rpc客户端配置类
 */
@Data
public class RpcClientConfig {
    /**
     * 容错策略
     */
    private TolerantStrategyEnum tolerantStrategy;
    /**
     * 序列化类型
     */
    private SerializationEnum serialization;
    /**
     * 负载均衡类型
     */
    private LoadBalanceEnum loadBalance;
    /**
     * 重试策略
     */
    private RetryStrategyEnum retryStrategy;
    /**
     * 注册中心配置
     */
    private RegistryConfig registry;
}
