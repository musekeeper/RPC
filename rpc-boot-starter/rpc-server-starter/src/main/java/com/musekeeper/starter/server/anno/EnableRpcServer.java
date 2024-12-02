package com.musekeeper.starter.server.anno;

import com.musekeeper.starter.server.selector.ServerAnnoImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author musekeeper
 * EnableRpcServer注解，表示启用RPC提供服务功能
 */
@Target(ElementType.TYPE)//注解可以用在类上
@Retention(RetentionPolicy.RUNTIME)//注解在运行时有效
@Import(ServerAnnoImportSelector.class)
public @interface EnableRpcServer {
    boolean enable() default true;
}
