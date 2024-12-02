package com.musekeeper.rpc.core.server.registry.Impl.nacos.config;

import lombok.Data;

/**
 * @author musekeeper
 * nacos的配置类
 */
@Data
public class NacosRegistryConfig {

    /**
     * nacos的服务地址,示例: "127.0.0.1:8848,127.0.0.1:8849"
     */
    String serverAddr;
    /**
     * nacos的namespace id
     */
    private String nameSpaceId;

}
