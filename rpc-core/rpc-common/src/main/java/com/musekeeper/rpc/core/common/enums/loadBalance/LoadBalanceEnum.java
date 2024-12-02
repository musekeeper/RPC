package com.musekeeper.rpc.core.common.enums.loadBalance;

/**
 * @author musekeeper
 * enum类，用于定义负载均衡算法
 */
public enum LoadBalanceEnum {
    RANDOM,
    WEIGHT_RANDOM,
    ROUND,
    WEIGHT_ROUND,
    CONSISTENT_HASH,
}

