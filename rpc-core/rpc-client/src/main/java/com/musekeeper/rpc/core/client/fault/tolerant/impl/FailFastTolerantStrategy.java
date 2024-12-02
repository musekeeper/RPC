package com.musekeeper.rpc.core.client.fault.tolerant.impl;

import com.musekeeper.rpc.core.common.exception.RpcException;
import com.musekeeper.rpc.core.client.fault.tolerant.TolerantStrategy;
import com.musekeeper.rpc.core.common.res.RpcResponse;

import java.util.Map;

/**
 * @author musekeeper
 * 快速失败策略，即遇到异常立即报错
 */
public class FailFastTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RpcException("服务报错", e);
    }
}
