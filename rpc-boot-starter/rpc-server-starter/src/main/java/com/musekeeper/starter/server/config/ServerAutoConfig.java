package com.musekeeper.starter.server.config;


import com.musekeeper.starter.server.starter.BootServerStarter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


///**
// * @author musekeeper
// * ServerAutoConfig
// */
//@EnableConfigurationProperties(ServerConfigProperties.class)
//@AutoConfiguration
//
//public class ServerAutoConfig {
//    @Bean
//    public BootServerStarter RpcServerStarter(ServerConfigProperties properties, ApplicationContext applicationContext) {
//        return new BootServerStarter(properties, applicationContext);
//    }
//
//}
