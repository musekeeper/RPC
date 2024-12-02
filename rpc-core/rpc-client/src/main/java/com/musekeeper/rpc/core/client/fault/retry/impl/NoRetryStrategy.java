package com.musekeeper.rpc.core.client.fault.retry.impl;

import com.musekeeper.rpc.core.client.fault.retry.RetryStrategy;
import com.musekeeper.rpc.core.common.res.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author musekeeper
 * 不重试策略
 */
public class NoRetryStrategy implements RetryStrategy {
    /**
     * 不重试策略
     * @param callable callable
     * @return null
     * @throws Exception 异常
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {

        return callable.call();
    }
}

