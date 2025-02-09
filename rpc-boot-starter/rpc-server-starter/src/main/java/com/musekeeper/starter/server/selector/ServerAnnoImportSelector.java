package com.musekeeper.starter.server.selector;


import com.musekeeper.starter.server.anno.EnableRpcServer;
import com.musekeeper.starter.server.config.ServerConfigProperties;
import com.musekeeper.starter.server.starter.BootServerStarter;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * 注解 {@link EnableRpcServer} 存在并且 enable 属性为 true 时，自动配置 {@link BootServerStarter} 和 {@link ServerConfigProperties}
 */
public class ServerAnnoImportSelector implements ImportSelector {
    @Override
    @NonNull
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRpcServer.class.getName()));
        if (annotationAttributes != null && annotationAttributes.getBoolean("enable")) {
            // 自动配置
            return new String[]{BootServerStarter.class.getName()};
        }
        return new String[0];
    }


}
