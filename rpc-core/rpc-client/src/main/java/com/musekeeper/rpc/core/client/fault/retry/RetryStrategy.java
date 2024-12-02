package com.musekeeper.rpc.core.client.fault.retry;

import com.musekeeper.rpc.core.common.res.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author musekeeper
 * 重试策略接口
 */
public interface RetryStrategy {
    /**
     * 重试
     *
     * @param callable
     * @return RpcResponse
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;

}
