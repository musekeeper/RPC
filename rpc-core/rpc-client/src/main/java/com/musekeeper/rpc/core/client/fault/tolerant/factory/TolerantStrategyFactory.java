package com.musekeeper.rpc.core.client.fault.tolerant.factory;

import com.musekeeper.rpc.core.common.enums.fault.tolerant.TolerantStrategyEnum;
import com.musekeeper.rpc.core.client.fault.tolerant.TolerantStrategy;
import com.musekeeper.rpc.core.client.fault.tolerant.impl.FailFastTolerantStrategy;
import com.musekeeper.rpc.core.client.fault.tolerant.impl.FailSafeTolerantStrategy;

/**
 * @author musekeeper
 * 容错策略工厂类
 */
public class TolerantStrategyFactory {
    public static TolerantStrategy getTolerantStrategy(TolerantStrategyEnum strategyName) {
        switch (strategyName) {
            case FAIL_FAST -> {
                return new FailFastTolerantStrategy();
            }
            case FAIL_SAFE -> {
                return new FailSafeTolerantStrategy();
            }
            default -> throw new IllegalArgumentException("Invalid strategy name: " + strategyName);
        }

    }
}
