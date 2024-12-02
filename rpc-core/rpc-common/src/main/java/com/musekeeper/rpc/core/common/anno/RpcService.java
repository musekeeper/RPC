package com.musekeeper.rpc.core.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author musekeeper
 *
 */
@Target(ElementType.TYPE)//注解可以用在类上
@Retention(RetentionPolicy.RUNTIME)//注解在运行时有效
public @interface RpcService {

}
