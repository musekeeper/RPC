package com.musekeeper.rpc.core.server.registry;


import com.musekeeper.rpc.core.common.enums.registry.RegistryEnum;
import com.musekeeper.rpc.core.server.registry.config.ServerConfig;

/**
 * @author musekeeper
 * 将服务注册到注册中心
 */
public interface ServerRegistry {
    /**
     * 服务注册
     * @param serverConfig 服务配置
     * @throws Exception 异常
     */
    void serverRegister(ServerConfig serverConfig) throws Exception;

    /**
     * 获取注册中心类型
     * @return 注册中心类型
     */
    RegistryEnum getRegistryType();
}
