package com.musekeeper.rpc.core.client.registry.impl.nacos.config;

import lombok.Data;

@Data
public class NacosConfig {
    /**
     * nacos的ip地址
     */
    String serverAddr;
    /**
     * nacos的命名空间
     */
    private String nameSpaceId;

    /**
     * nacos server group name
     */
    String groupName = "DEFAULT_GROUP";

}
