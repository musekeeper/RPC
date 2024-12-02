package com.musekeeper.rpc.core.client.fault.retry.factory;

import com.musekeeper.rpc.core.common.enums.fault.retry.RetryStrategyEnum;
import com.musekeeper.rpc.core.client.fault.retry.RetryStrategy;
import com.musekeeper.rpc.core.client.fault.retry.impl.NoRetryStrategy;

/**
 * @author musekeeper
 * 重试策略工厂
 */
public class RetryStrategyFactory {
    public static RetryStrategy getRetryStrategy(RetryStrategyEnum retryStrategy) {
        switch (retryStrategy) {
            case NO_RETRY -> {
                return new NoRetryStrategy();
            }
            default -> {
                throw new IllegalArgumentException("Invalid retry strategy:"+retryStrategy.name());
            }
        }

    }
}
