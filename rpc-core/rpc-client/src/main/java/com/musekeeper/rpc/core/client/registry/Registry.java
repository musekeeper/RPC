package com.musekeeper.rpc.core.client.registry;

import com.musekeeper.rpc.core.client.registry.instance.ServiceInstance;
import com.musekeeper.rpc.core.common.enums.registry.RegistryEnum;


import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author musekeeper
 * 负责与注册中心通信，缓存服务实例
 */

public interface Registry {

     /**
      * 获取服务实例缓存
      * @return 服务实例缓存
      */
     Map<String, ServiceInstance> getServiceInstanceMap();

     /**
      * 服务是否可用
      * @return true：可用，false：不可用
      */
     Boolean isAvailable();

     /**
      * 等待服务可用
      */
     void await(int timeout, TimeUnit unit) throws InterruptedException;

     /**
      * 获取注册中心类型
      * @return 注册中心类型
      */
     RegistryEnum getRegistryType();
}
