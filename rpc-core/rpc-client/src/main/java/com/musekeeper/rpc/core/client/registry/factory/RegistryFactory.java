package com.musekeeper.rpc.core.client.registry.factory;

import com.alibaba.nacos.api.exception.NacosException;
import com.musekeeper.rpc.core.client.registry.Registry;
import com.musekeeper.rpc.core.client.registry.config.RegistryConfig;
import com.musekeeper.rpc.core.client.registry.impl.AbstractRegistry;
import com.musekeeper.rpc.core.client.registry.impl.nacos.NacosRpcRegistry;
import com.musekeeper.rpc.core.common.enums.registry.RegistryEnum;

/**
 * @author musekeeper
 * registry工厂类
 */
public class RegistryFactory {
    private RegistryFactory() {

    }

    /**
     * 使用单例模式创建registry实例
     */
    private static volatile AbstractRegistry rpcRegistry;

    public static Registry getInstance(RegistryConfig registryConfig)  {
        if (registryConfig == null) {
            throw new IllegalArgumentException("Registry config cannot be null");
        }
        switch (registryConfig.getRegistry()) {
            case NACOS -> {
                try {
                    // 双重检查锁定创建单例
                    if (rpcRegistry == null) {
                        synchronized (RegistryFactory.class) {
                            if (rpcRegistry == null) {
                                rpcRegistry = new NacosRpcRegistry(registryConfig);
                            }
                        }
                    }
                    return rpcRegistry;
                } catch (NacosException e) {
                    throw new RuntimeException(e);
                }
            }


            default -> throw new IllegalArgumentException("Unsupported registry type: " + registryConfig.getRegistry());
        }
    }
}
