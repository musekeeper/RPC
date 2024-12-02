package com.example.provider;


import com.musekeeper.starter.server.anno.EnableRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRpcServer
@SpringBootApplication
public class ProviderApplication  {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
