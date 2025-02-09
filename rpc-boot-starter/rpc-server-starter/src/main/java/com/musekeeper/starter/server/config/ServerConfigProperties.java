package com.musekeeper.starter.server.config;


import com.musekeeper.rpc.core.common.enums.registry.RegistryEnum;
import com.musekeeper.rpc.core.server.registry.config.ServerConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author musekeeper
 * 服务端配置类
 */
@Data
@ConfigurationProperties("rpc.server")
public class ServerConfigProperties {
    /**
     * 注册中心类型
     */
    private RegistryEnum registry;
    /**
     * rpc服务配置
     */
    @NestedConfigurationProperty
    private ServerConfig config;

}


