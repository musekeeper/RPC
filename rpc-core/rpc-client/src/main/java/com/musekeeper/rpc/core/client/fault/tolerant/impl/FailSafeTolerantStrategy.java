package com.musekeeper.rpc.core.client.fault.tolerant.impl;

import com.musekeeper.rpc.core.client.fault.tolerant.TolerantStrategy;
import com.musekeeper.rpc.core.common.res.RpcResponse;
import lombok.extern.log4j.Log4j2;
import java.util.Map;

/**
 * @author musekeeper
 * 静默处理异常策略
 */
@Log4j2
public class FailSafeTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理异常", e);
        return new RpcResponse();
    }

}
