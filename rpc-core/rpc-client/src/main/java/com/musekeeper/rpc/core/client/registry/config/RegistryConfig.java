package com.musekeeper.rpc.core.client.registry.config;



import com.musekeeper.rpc.core.client.registry.impl.nacos.config.NacosConfig;
import com.musekeeper.rpc.core.common.enums.registry.RegistryEnum;
import lombok.Data;

/**
 * @author musekeeper
 * 整合所有注册中心配置类
 */
@Data
public class RegistryConfig {
    /**
     * 服务名
     */
    String name;
    /**
     * 注册中心类型
     */
    RegistryEnum registry;
    /**
     * nacos配置
     */
    NacosConfig nacos;

    // TODO: 其他注册中心配置
}
