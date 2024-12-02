# 基于SpringBoot + Netty的Rpc框架  

## 使用方法

1. 服务提供者则在启动类加上@EnableRpcServer,消费者则在启动类加@EnableRpcClient
2. 服务提供者需要实现一个被@RpcService代理的公共接口,服务消费者则在需要被代理的类加上@RpcReference并配置服务提供者的服务名
3. 写配置文件
4. 启动注册中心
5. 启动消费者和提供者

