package com.musekeeper.rpc.core.client.registry.impl.nacos;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.listener.AbstractNamingChangeListener;
import com.alibaba.nacos.client.naming.listener.NamingChangeEvent;
import com.musekeeper.rpc.core.client.registry.instance.ServiceInstance;
import com.musekeeper.rpc.core.client.registry.config.RegistryConfig;
import com.musekeeper.rpc.core.client.registry.impl.AbstractRegistry;
import com.musekeeper.rpc.core.client.registry.impl.nacos.config.NacosConfig;

import com.musekeeper.rpc.core.common.enums.registry.RegistryEnum;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Properties;

/**
 * @author musekeeper
 * nacos注册中心实现
 */
@Log4j2
public class NacosRpcRegistry extends AbstractRegistry {


    public NacosRpcRegistry(RegistryConfig registryConfig) throws NacosException {
        // 获取nacos配置
        NacosConfig nacosConfig = registryConfig.getNacos();
        String serverAddr = nacosConfig.getServerAddr();
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, nacosConfig.getNameSpaceId());

        // 创建nacos客户端
        NamingService namingService = NamingFactory.createNamingService(properties);

        // 订阅服务
        namingService.subscribe(registryConfig.getName(),serviceListener);
    }



    /**
     * 服务监听器
     */
    EventListener serviceListener = new AbstractNamingChangeListener() {
        // 监听服务变化
        @Override
        public void onChange(NamingChangeEvent namingChangeEvent) {
            // 处理服务变化
            if(namingChangeEvent.isAdded()){
                // 新增服务
                List<Instance> addedInstances = namingChangeEvent.getAddedInstances();
                for (Instance addedInstance : addedInstances) {
                    log.info("新增服务："+addedInstance.getInstanceId());
                    // 新增服务实例
                    putInstance(addedInstance.getInstanceId(),new ServiceInstance(addedInstance));
                }

            }
            if(namingChangeEvent.isRemoved()){
                // 删除服务
                List<Instance> removedInstances = namingChangeEvent.getRemovedInstances();
                for (Instance removedInstance : removedInstances) {
                    log.info("删除服务："+removedInstance.getInstanceId());
                    // 删除服务实例
                    removeInstance(removedInstance.getInstanceId());
                }

            }
            if(namingChangeEvent.isModified()){
                // 修改服务
                List<Instance> modifiedInstances = namingChangeEvent.getModifiedInstances();
                for (Instance modifiedInstance : modifiedInstances) {
                    log.info("修改服务："+modifiedInstance.getInstanceId());
                    // 修改服务实例
                    putInstance(modifiedInstance.getInstanceId(),new ServiceInstance(modifiedInstance));
                }
            }
        }


    };


    @Override
    public RegistryEnum getRegistryType() {
        return RegistryEnum.NACOS;
    }
}
