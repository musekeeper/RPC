package com.musekeeper.rpc.core.server.registry.config;

import com.musekeeper.rpc.core.server.registry.Impl.nacos.config.NacosRegistryConfig;
import lombok.Data;

/**
 * @author musekeeper
 * 服务配置类
 */
@Data
public class ServerConfig {
    /**
     * rpc 服务名
     */
    private String name;
    /**
     * rpc 服务开启端口
     */
    private Integer port;
    /**
     * rpc 服务地址
     */
    private String host;
    /**
     * nacos 注册中心配置
     */
    private NacosRegistryConfig nacos;

    // TODO: 其他注册中心配置

}
