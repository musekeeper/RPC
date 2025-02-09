package com.musekeeper.starter.client.proxy;


import com.musekeeper.rpc.core.client.config.RpcClientConfig;
import com.musekeeper.rpc.core.client.proxy.ProxyFactory;
import com.musekeeper.rpc.core.common.anno.RpcReference;
import com.musekeeper.starter.client.config.RpcClientConfigProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.Objects;


/**
 * @author musekeeper
 * 启动时获取代理
 */
@Log4j2
@EnableConfigurationProperties(RpcClientConfigProperties.class)
public class BootAutoProxy implements CommandLineRunner {
    /**
     * rpc客户端配置
     */
    private final RpcClientConfig rpcClientConfig;

    /**
     * spring上下文
     */
    private final ApplicationContext applicationContext;

    /**
     * 构造器注入
     * @param rpcClientConfigProperties 配置参数
     * @param applicationContext spring上下文
     */
    public BootAutoProxy(RpcClientConfigProperties rpcClientConfigProperties,
                         ApplicationContext applicationContext
    ) {
        rpcClientConfig = rpcClientConfigProperties.getConfig();
        this.applicationContext = applicationContext;
    }

    /**
     * 遍历所有字段,获取被RpcReference注解标识的字段并进行代理
     * @param args run方法参数
     */
    @Override
    public void run(String... args) {

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        //遍历
        for (String beanDefinitionName : beanDefinitionNames) {
            //获取bean
            Object bean = applicationContext.getBean(beanDefinitionName);

            Class<?> clazz = bean.getClass();

            if (AopUtils.isCglibProxy(bean)) {
                clazz = clazz.getSuperclass();
            } else if (AopUtils.isJdkDynamicProxy(bean)) {
                clazz = clazz.getSuperclass();
            }

            Field[] fields = clazz.getDeclaredFields();
            //遍历属性
            for (Field declaredField : fields) {
                RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
                if (rpcReference != null) {
                    //设置服务名
                    if (!Objects.equals(rpcReference.serviceName(), "")) {
                        rpcClientConfig.getRegistry().setName(rpcReference.serviceName());
                    }

                    //获取代理对象
                    Object proxy = ProxyFactory.createProxy(declaredField.getType(),rpcClientConfig, rpcReference);
                    try {
                        // 给属性赋值
                        declaredField.setAccessible(true);
                        declaredField.set(bean, proxy);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
