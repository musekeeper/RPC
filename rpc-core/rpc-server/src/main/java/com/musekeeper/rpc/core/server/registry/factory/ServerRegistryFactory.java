package com.musekeeper.rpc.core.server.registry.factory;


import com.musekeeper.rpc.core.common.enums.registry.RegistryEnum;
import com.musekeeper.rpc.core.server.registry.Impl.nacos.NacosRegistry;
import com.musekeeper.rpc.core.server.registry.ServerRegistry;


/**
 * @author musekeeper
 * 注册中心服务工厂
 */
public class ServerRegistryFactory {
    public static ServerRegistry getServerRegistry(RegistryEnum registryEnum) {
        switch (registryEnum) {
            case NACOS -> {
                return new NacosRegistry();
            }
            //TODO: 添加其他注册中心
            default -> {
                throw new IllegalArgumentException("Unsupported registry type: " + registryEnum.name());
            }
        }
    }
}
