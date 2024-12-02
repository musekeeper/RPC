package com.musekeeper.starter.server.starter;



import com.musekeeper.rpc.core.common.anno.RpcService;
import com.musekeeper.rpc.core.server.registry.ServerRegistry;
import com.musekeeper.rpc.core.server.registry.factory.ServerRegistryFactory;
import com.musekeeper.rpc.core.server.starter.RpcServerStarter;
import com.musekeeper.starter.server.config.ServerConfigProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author musekeeper
 * bootstrap启动类
 */
@Slf4j
public class BootServerStarter implements CommandLineRunner {

    private RpcServerStarter rpcServerStarter;

    private final ServerConfigProperties properties;

    private final ApplicationContext applicationContext;

    private final String host;

    private final int port;


    public BootServerStarter(ServerConfigProperties properties,
                             ApplicationContext applicationContext
    ) {
        this.applicationContext = applicationContext;
        this.host = properties.getConfig().getHost();
        this.port = properties.getConfig().getPort();
        this.properties = properties;
    }

    /**
     * 结束时清理资源
     */
    @PreDestroy
    public void destroy() {
        rpcServerStarter.stop();
    }

    /**
     * 启动netty服务
     */
    @PostConstruct
    public void run() {

        rpcServerStarter = new RpcServerStarter(host, port);
        new Thread(
                () -> {
                    log.info("RpcServer started on {}:{}", rpcServerStarter.getHost(), rpcServerStarter.getPort());
                    rpcServerStarter.start();
                }, "RpcServer"
        ).start();
    }

    /**
     * 容器启动完成后,获取被@RpcService注解的bean并缓存到map中,并注册到注册中心
     */
    @Override
    public void run(String... args) throws Exception {

        // 获取标有@RpcService注解的bean
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);

        for (Map.Entry<String, Object> entry : serviceBeanMap.entrySet()) {

            Object serviceBean = entry.getValue();
            // 判断是否实现了接口
            Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
            if (interfaces.length == 0) {
                throw new RuntimeException("对外暴露的服务必须实现接口");
            }
            String serviceName = interfaces[0].getName();
            // 将bean缓存到map中
            rpcServerStarter.getServiceInstanceMap().put(serviceName, serviceBean);
        }

        if(properties.getConfig().getName()==null|| properties.getConfig().getName().isEmpty()){
            properties.getConfig().setName(applicationContext.getApplicationName());
        }

        //获取注册中心
        ServerRegistry serverRegistry = ServerRegistryFactory.getServerRegistry(properties.getRegistry());
        //注册到注册中心
        serverRegistry.serverRegister(properties.getConfig());

    }
}
