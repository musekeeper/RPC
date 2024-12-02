package com.musekeeper.starter.client.config;

import com.musekeeper.starter.client.proxy.BootAutoProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

///**
// * @author musekeeper
// * 自动配置类
// */
//@EnableConfigurationProperties(RpcClientConfigProperties.class)
//@AutoConfiguration
//@Slf4j
//public class RpcClientAutoConfig {
//    @Bean
//    public static BootAutoProxy rpcAutoProxy(RpcClientConfigProperties properties, ApplicationContext applicationContext) {
//        return new BootAutoProxy(properties, applicationContext);
//    }
//}
