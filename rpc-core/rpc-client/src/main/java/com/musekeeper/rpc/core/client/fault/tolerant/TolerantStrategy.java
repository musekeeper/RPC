package com.musekeeper.rpc.core.client.fault.tolerant;

import com.musekeeper.rpc.core.common.res.RpcResponse;

import java.util.Map;

/**
 * @author musekeeper
 * 容错策略
 */
public interface TolerantStrategy {
    /**
     * 容错处理
     * @param context 请求上下文
     * @param e 异常
     * @return RpcResponse
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e);
}
