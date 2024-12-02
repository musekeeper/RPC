package com.musekeeper.rpc.core.client.registry.instance;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.Data;

import java.net.InetSocketAddress;

/**
 * @author musekeeper
 * 统一封装服务实例信息,适配不同的注册中心
 */
@Data
public class ServiceInstance {
    /**
     * 服务的唯一标识
     */
    private String id;
    /**
     * 服务的IP地址
     */
    private String ip;
    /**
     * 服务的端口号
     */
    private int port;
    /**
     * 服务的地址
     */
    private InetSocketAddress address;
    /**
     * 服务的权重
     */
    private double weight;

    /**
     * 构造函数,适配nacos的Instance对象
     * @param instance nacos的Instance对象
     */
    public ServiceInstance(Instance instance) {
        this.id = instance.getInstanceId();
        this.ip = instance.getIp();
        this.port = instance.getPort();
        this.weight = instance.getWeight();
        this.address = new InetSocketAddress(ip, port);
    }
}
