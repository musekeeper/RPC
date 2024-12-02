package com.musekeeper.rpc.core.server.registry.Impl.nacos;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import com.musekeeper.rpc.core.common.enums.registry.RegistryEnum;
import com.musekeeper.rpc.core.server.registry.Impl.nacos.config.NacosRegistryConfig;

import com.musekeeper.rpc.core.server.registry.ServerRegistry;
import com.musekeeper.rpc.core.server.registry.config.ServerConfig;

import java.util.Properties;

/**
 * @author musekeeper
 *
 */
public class NacosRegistry implements ServerRegistry {
    /**
     * @author musekeeper
     * 注册到nacos
     * @throws NacosException nacos exception
     */
    @Override
    public void serverRegister(ServerConfig serverConfig) throws NacosException {
        NacosRegistryConfig nacosRegistryConfig = serverConfig.getNacos();
        //获取rpc服务信息
        String serverHost = serverConfig.getHost();
        int serverPort = serverConfig.getPort();
        String serverName = serverConfig.getName();

        //获取nacos配置信息
        String nacosRegistryServerAddr = nacosRegistryConfig.getServerAddr();
        String nameSpaceId = nacosRegistryConfig.getNameSpaceId();


        if(nacosRegistryServerAddr == null){
            throw new NacosException(100, "nacos register ip is null");
        }
        // nacos naming
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosRegistryServerAddr);
        properties.put(PropertyKeyConst.NAMESPACE, nameSpaceId);
        NamingService naming = NamingFactory.createNamingService(properties);
        // nacos config
        if(serverHost == null || serverHost.isEmpty() || serverPort == 0){
            throw new NacosException(100, "server host or port is null");
        }
        Instance instance = new Instance();
        instance.setServiceName(serverName);
        instance.setIp(serverHost);
        instance.setPort(serverPort);
        //注册到nacos
        naming.registerInstance(serverName, instance);
    }

    @Override
    public RegistryEnum getRegistryType() {
        return RegistryEnum.NACOS;
    }

}
