package com.musekeeper.starter.client.config;

import com.musekeeper.rpc.core.client.config.RpcClientConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;


/**
 * @author musekeeper
 * RPC客户端配置属性类
 */
@Data
@ConfigurationProperties(prefix = "rpc.client")
public class RpcClientConfigProperties {

    /**
     * RPC客户端配置
     */
    @NestedConfigurationProperty
    private RpcClientConfig config;

}
