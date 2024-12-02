package com.musekeeper.rpc.core.client.proxy;


import com.musekeeper.rpc.core.client.RpcClient;
import com.musekeeper.rpc.core.client.config.RpcClientConfig;
import com.musekeeper.rpc.core.client.loadBalance.LoadBalancer;
import com.musekeeper.rpc.core.client.loadBalance.factory.LoadBalancerFactory;
import com.musekeeper.rpc.core.client.registry.Registry;
import com.musekeeper.rpc.core.client.registry.factory.RegistryFactory;
import com.musekeeper.rpc.core.client.registry.instance.ServiceInstance;
import com.musekeeper.rpc.core.common.anno.RpcReference;
import com.musekeeper.rpc.core.common.constant.TolerantStrategyContextKey;
import com.musekeeper.rpc.core.common.enums.fault.tolerant.TolerantStrategyEnum;
import com.musekeeper.rpc.core.common.exception.RpcException;
import com.musekeeper.rpc.core.client.fault.retry.RetryStrategy;
import com.musekeeper.rpc.core.client.fault.retry.factory.RetryStrategyFactory;
import com.musekeeper.rpc.core.client.fault.tolerant.TolerantStrategy;
import com.musekeeper.rpc.core.client.fault.tolerant.factory.TolerantStrategyFactory;
import com.musekeeper.rpc.core.common.req.RpcRequest;
import com.musekeeper.rpc.core.common.res.RpcResponse;
import com.musekeeper.rpc.core.common.serialization.Serializer;
import com.musekeeper.rpc.core.common.serialization.SerializerFactory;
import lombok.extern.log4j.Log4j2;


import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author musekeeper
 * proxy工厂
 */
@Log4j2
public class ProxyFactory {
    /**
     * 代理对象缓存
     */
    private static final Map<Class<?>, Object> serviceMap = new ConcurrentHashMap<>();

    /**
     * 获取代理对象
     * @param clazz 被代理类
     * @param rpcClientConfig 客户端配置
     * @param rpcReference rpcReference注解
     * @return Object
     */
    public static Object createProxy(Class<?> clazz, RpcClientConfig rpcClientConfig, RpcReference rpcReference) {
        Object proxy;
        proxy = serviceMap.get(clazz);
        if (proxy == null) {
            //创建代理对象
            proxy = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy1, method, args) -> {
                //创建注册中心
                Registry registry = RegistryFactory.getInstance(rpcClientConfig.getRegistry());
                //创建负载均衡器
                LoadBalancer loadBalancer = LoadBalancerFactory.getLoadBalancer(rpcClientConfig.getLoadBalance());

                int hashArguments = rpcReference.hashArguments();

                if (args.length < hashArguments + 1) {
                    throw new IllegalArgumentException("参数个数不足");
                }
                // 获取哈希计算参数
                Object parameter = args[hashArguments];
                // 是否存在可用服务
                if (!registry.isAvailable()) {
                    registry.await(3, TimeUnit.SECONDS);
                    if (!registry.isAvailable()) {
                        throw new RpcException("无法获取到服务");
                    }
                }

                // 获取实例
                ServiceInstance instance = loadBalancer.select(parameter, registry.getServiceInstanceMap());
                // 序列化器
                Serializer serializer = SerializerFactory.getSerializer(rpcClientConfig.getSerialization());

                // 封装请求对象
                RpcRequest request = new RpcRequest();
                request.setRequestId(UUID.randomUUID().toString());
                request.setClassName(method.getDeclaringClass().getName());
                request.setMethodName(method.getName());
                request.setParameterTypes(method.getParameterTypes());
                request.setReturnType(method.getReturnType());
                request.setParameters(args);
                //序列化请求对象
                byte[] bytes = serializer.serialize(request);
                //返回结果
                byte[] result;
                RpcResponse rpcResponse = new RpcResponse();
                try {
                    //发送请求
                    result = RpcClient.send(bytes, instance.getAddress(), rpcClientConfig.getSerialization());
                    //反序列化响应对象
                    rpcResponse = serializer.deserialize(result, RpcResponse.class);
                } catch (Exception e) {
                    rpcResponse.setException(e.getMessage());
                }

                //判断是否有异常
                if (rpcResponse.getException() != null || rpcResponse.getResult() == null) {
                    //重试机制
                    try {
                        RetryStrategy retryStrategy = RetryStrategyFactory.getRetryStrategy(rpcClientConfig.getRetryStrategy());
                        RpcResponse retry = retryStrategy.doRetry(() -> {
                            //发送请求
                            byte[] retryResult = RpcClient.send(bytes, instance.getAddress(), rpcClientConfig.getSerialization());
                            //反序列化响应对象
                            RpcResponse retryRpcResponse = serializer.deserialize(retryResult, RpcResponse.class);
                            //判断是否有异常
                            if (retryRpcResponse.getResult() == null || retryRpcResponse.getException() != null) {
                                throw new RpcException(retryRpcResponse.getException());
                            }
                            //返回响应对象
                            return retryRpcResponse;
                        });
                        return retry.getResult();
                    } catch (Exception e) {
                        // 容错机制
                        TolerantStrategyEnum tolerantStrategyEnum = rpcClientConfig.getTolerantStrategy();
                        // 创建容错策略
                        TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getTolerantStrategy(tolerantStrategyEnum);
                        // 获取容错上下文
                        HashMap<String, Object> contextMap = new HashMap<>();
                        // 设置容错上下文
                        contextMap.put(TolerantStrategyContextKey.CONTEXT_BYTES, bytes);
                        contextMap.put(TolerantStrategyContextKey.CONTEXT_ADDRESS, instance.getAddress());
                        contextMap.put(TolerantStrategyContextKey.CONTEXT_SERIALIZATION, rpcClientConfig.getSerialization());
                        // 执行容错策略
                        RpcResponse doTolerantResponse = tolerantStrategy.doTolerant(contextMap, e);
                        // 判断是否有异常
                        if (doTolerantResponse.getResult() == null || doTolerantResponse.getException() != null) {
                            throw new RpcException(doTolerantResponse.getException());
                        }
                        // 返回响应对象
                        return doTolerantResponse.getResult();

                    }
                }

                return rpcResponse.getResult();
            });
            serviceMap.put(clazz, proxy);
            return proxy;
        } else {
            return proxy;
        }
    }



}
