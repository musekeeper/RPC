package com.musekeeper.rpc.core.client.registry.impl;

import com.musekeeper.rpc.core.client.registry.instance.ServiceInstance;
import com.musekeeper.rpc.core.client.registry.Registry;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author musekeeper
 * 抽象注册中心
 */
@Log4j2
public abstract class AbstractRegistry implements Registry {
    /**
     * 等待可用signal
     */
    protected CountDownLatch latch = new CountDownLatch(1);

    /**
     * 是否可用
     */
    protected volatile Boolean Active = false;
    /**
     * 服务实例缓存
     */
    protected volatile Map<String, ServiceInstance> instanceMap = new ConcurrentHashMap<>();

    /**
     * 获取服务实例缓存
     *
     * @return 实例缓存
     */
    @Override
    public Map<String, ServiceInstance> getServiceInstanceMap() {
        return instanceMap;
    }

    ;

    /**
     * 是否可用
     *
     * @return 是否可用
     */
    @Override
    public Boolean isAvailable() {
        return Active;
    }

    /**
     * 等待可用
     *
     * @param timeout 超时时间
     * @param unit    超时时间单位
     */
    @Override
    public void await(int timeout, TimeUnit unit) throws InterruptedException {
        boolean await = latch.await(timeout, unit);
        if (!await) {
            throw new InterruptedException("wait available timeout");
        }
    }

    /**
     * 打断等待, 通知可用
     */
    protected void Interrupt() {
        latch.countDown();
    }

    /**
     * 添加或更新实例
     * @param serviceId 服务唯一标识
     * @param instance  实例
     */
    protected void putInstance(String serviceId, ServiceInstance instance) {
        instanceMap.put(serviceId, instance);

        // 第一个实例添加,表示已经获取可用服务
        if (!Active && !instanceMap.isEmpty()) {
            synchronized (this) {
                if (!Active && !instanceMap.isEmpty()) {
                    Active = true;
                    //打断等待
                    Interrupt();
                }
            }
        }
    }

    /**
     * 移除实例
     *
     * @param serviceId 服务唯一标识
     */
    protected void removeInstance(String serviceId) {
        instanceMap.remove(serviceId);
        // 最后一个实例移除,表示已经失去可用服务
        if (instanceMap.isEmpty() && isAvailable()) {
            synchronized (this) {
                if (isAvailable() && instanceMap.isEmpty()) {
                    Active = false;
                    // 重新初始化等待
                    latch = new CountDownLatch(1);
                }
            }
        }

    }


}

