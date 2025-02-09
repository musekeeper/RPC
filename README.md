# 基于SpringBoot + Netty的Rpc框架  

## 使用方法

1. 服务提供者则在启动类加上 **@EnableRpcServer**,消费者则在启动类加 **@EnableRpcClient**
2. 服务提供者需要实现一个被 **@RpcService**代理的公共接口,服务消费者则在需要被代理的类加上 **@RpcReference**并配置服务提供者的服务名
3. 写配置文件
4. 启动注册中心
5. 启动消费者和提供者
# RPC 客户端配置说明

## 核心配置项
```yaml
rpc:
  client:
    config:
      serialization: PROTOBUF
      load-balance: WEIGHT_RANDOM
      retry-strategy: NO_RETRY
      tolerant-strategy: FAIL_SAFE
      registry:
        name: RpcProvider
        registry: NACOS
        nacos:
          server-addr: localhost:8848
          name-space-id: 906c8dfb-09a6-418c-8800-a89552c24252  
```  
## 配置选项
1. <font size=4>**序列化方式 (serialization)**</font>
   <br>
   + <font size=3>**PROTOBUF**</font>
     + Google 提供的高效二进制序列化协议。
    + <font size=3>**JDK**</font>
        - Java 原生序列化方式，兼容性好但性能较低。
    + <font size=3>**JSON**</font>
        - 基于文本的序列化方式，可读性强，适用于需要人类可读数据的场景。
    + <font size=3>**KRYO**</font>
        - 高性能的二进制序列化框架。
    + <font size=3>**HESSIAN**</font>
        - 基于二进制的轻量级序列化协议。  
          <br>
2. <font size=4>**负载均衡策略 (load-balance)**</font>
   <br>
    + <font size=3>**RANDOM**</font>
        - 随机选择服务实例，适用于服务实例性能相近的场景。
    + <font size=3>**WEIGHT_RANDOM**</font>
        - 根据权重随机选择服务实例，适用于服务实例性能不均衡的场景。
    + <font size=3>**ROUND**</font>
        - 轮询选择服务实例，适用于服务实例性能相近且需要均匀分配流量的场景。
    + <font size=3>**WEIGHT_ROUND**</font>
        - 根据权重轮询选择服务实例，适用于服务实例性能不均衡且需要按权重分配流量的场景。
    + <font size=3>**CONSISTENT_HASH**</font>
        - 一致性哈希算法，适用于需要保证相同请求总是路由到同一服务实例的场景。  
<br>
3. <font size=4>**重试策略 (retry-strategy)**</font>
    + <font size=3>**NO_RETRY**</font>
        - 不进行重试，适用于非幂等性操作或需要快速失败的场景。  
          <br>
4. <font size=4>**容错策略 (tolerant-strategy)**</font>
    + <font size=3>**FAIL_FAST**</font>
        - 快速失败，适用于需要立即感知错误的场景。
    + <font size=3>**FAIL_SAFE**</font>
        - 故障安全策略，适用于允许部分失败且需要保证系统整体可用性的场景。    
<br>
5. <font size=4>**注册中心配置 (registry)**</font>  
   
    | 配置项      |值|说明
    |----------|---|---|
    | name     |RpcProvider|服务提供者注册名称|
    | registry |NACOS|使用 Nacos 注册中心| 
# RPC 服务端配置说明

## 核心配置项
```yaml
rpc:
   server:
      registry: NACOS
      config:
         port: 9966
         host: localhost
         name: RpcProvider
         nacos:
            server-addr: localhost:8848
            name-space-id: 906c8dfb-09a6-418c-8800-a89552c24252
``` 
## 配置选项
1. <font size=4>**注册中心(registry)**</font>
    + <font size=3>**NACOS**</font>
        - 使用 Nacos 注册中心。
        <br>  
2. <font size=4>**服务端配置(config)**</font>
    + <font size=3>**port**</font>
        - 服务端开启端口。
    + <font size=3>**host**</font>
        - 服务端开启地址。
    + <font size=3>**name**</font>
        - 服务端注册名称。



