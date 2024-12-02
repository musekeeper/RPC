package com.musekeeper.starter.client.config;

import com.musekeeper.rpc.core.client.config.RpcClientConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Import;

/**
 * @author musekeeper
 * RPC客户端配置属性类
 */
@Data
@ConfigurationProperties(prefix = "rpc.client")

public class RpcClientConfigProperties {
//    /**
//     * 是否开启RPC客户端
//     */
//    private boolean Enable = false;


    /**
     * RPC客户端配置
     */
    @NestedConfigurationProperty
    private RpcClientConfig config;

}
