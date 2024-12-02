package com.musekeeper.rpc.core.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author musekeeper
 * 引用代理
 */
@Target(ElementType.FIELD)//注解可以用在成员变量上
@Retention(RetentionPolicy.RUNTIME)//注解在运行时有效
public @interface RpcReference {
    /**
     * 服务名称
     */
    String serviceName()default "";

    /**
     * 哈希算法参数
     */
    int hashArguments() default 0;

}
