package com.musekeeper.starter.client.selector;

import com.musekeeper.starter.client.config.RpcClientConfigProperties;
import com.musekeeper.starter.client.proxy.BootAutoProxy;
import com.musekeeper.starter.client.anno.EnableRpcClient;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;


import java.util.function.Predicate;

/**
 * @author musekeeper
 * 注解 {@link EnableRpcClient} 存在并且 enable 属性为 true 时，自动配置 {@link BootAutoProxy} 和 {@link RpcClientConfigProperties}
 */
public class ClientAnnoImportSelector implements ImportSelector {
    @Override
    @NonNull
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRpcClient.class.getName()));

        if (annotationAttributes != null && annotationAttributes.getBoolean("enable")) {
            // 自动配置
            return new String[]{BootAutoProxy.class.getName()};
        }
        return new String[0];
    }

    @Override
    public Predicate<String> getExclusionFilter() {
        return ImportSelector.super.getExclusionFilter();
    }
}
