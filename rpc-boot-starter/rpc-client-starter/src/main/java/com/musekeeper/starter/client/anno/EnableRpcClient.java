package com.musekeeper.starter.client.anno;

import com.musekeeper.starter.client.selector.ClientAnnoImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  @author musekeeper
 *  EnableRpc注解，表示启用RPC客户端功能
 */
@Import(ClientAnnoImportSelector.class)
@Target(ElementType.TYPE)//注解可以用在类上
@Retention(RetentionPolicy.RUNTIME)//注解在运行时有效
public @interface EnableRpcClient {
    boolean enable() default true;
}
