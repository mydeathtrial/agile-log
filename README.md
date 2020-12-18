# agile-log ： 日志打印组件
[![spring-mvc](https://img.shields.io/badge/spring--mvc-LATEST-green)](https://img.shields.io/badge/spring--mvc-LATEST-green)
[![maven](https://img.shields.io/badge/build-maven-green)](https://img.shields.io/badge/build-maven-green)
## 它有什么作用

* **控制层执行过程日志打印**

* **无代码入侵**
通过嵌入过滤器方法实现日志打印

* **支持自定义扩展**
通过实现接口`cloud.agileframework.log.ExecutionObjectProvider`注入到spring容器，即可实现对控制层执行过程数据的扩展

-------
## 快速入门
开始你的第一个项目是非常容易的。

#### 步骤 1: 下载包
您可以从[最新稳定版本]下载包(https://github.com/mydeathtrial/agile-log/releases).
该包已上传至maven中央仓库，可在pom中直接声明引用

以版本agile-log-2.0.9.jar为例。
#### 步骤 2: 添加maven依赖
```xml
<!--声明中央仓库-->
<repositories>
    <repository>
        <id>cent</id>
        <url>https://repo1.maven.org/maven2/</url>
    </repository>
</repositories>
<!--声明依赖-->
<dependency>
    <groupId>cloud.agileframework</groupId>
    <artifactId>agile-log</artifactId>
    <version>2.0.9</version>
</dependency>
```
#### 步骤 3: 开箱即用
```properties
//日志开关
agile.log.enabled=true
//开启日志级别
logging.level.cloud.agileframework.log=DEBUG
```
#### 执行效果
```
2020-08-18 18:56:41.777  INFO 18604 --- [         task-1] com.agile.TestController                 : 访问了1
2020-08-18 18:56:41.777 ERROR 18604 --- [         task-1] com.agile.TestController                 : 我是错误日志
2020-08-18 18:56:41.803 DEBUG 18604 --- [o-auto-1-exec-2] c.agileframework.log.PrintLogProvider    : IP  地址: 192.168.101.42
2020-08-18 18:56:41.806 DEBUG 18604 --- [o-auto-1-exec-2] c.agileframework.log.PrintLogProvider    : URL 地址: GET:/test1
2020-08-18 18:56:41.806 DEBUG 18604 --- [o-auto-1-exec-2] c.agileframework.log.PrintLogProvider    : 帐    号: anonymous
2020-08-18 18:56:41.806 DEBUG 18604 --- [o-auto-1-exec-2] c.agileframework.log.PrintLogProvider    : 耗    时: 142ms
2020-08-18 18:56:41.808 DEBUG 18604 --- [o-auto-1-exec-2] c.agileframework.log.PrintLogProvider    : 入    参: {"a":["12"]}
2020-08-18 18:56:41.827 DEBUG 18604 --- [o-auto-1-exec-2] c.agileframework.log.PrintLogProvider    : 出    参: {"a":"输出"}
2020-08-18 18:56:41.828 DEBUG 18604 --- [o-auto-1-exec-2] c.agileframework.log.PrintLogProvider    : ----------------------------------------------------------
2020-08-18 18:56:41.868  INFO 18604 --- [o-auto-1-exec-3] com.agile.TestController                 : 访问了2
2020-08-18 18:56:41.869 ERROR 18604 --- [o-auto-1-exec-3] com.agile.TestController                 : 我是错误日志
2020-08-18 18:56:41.870 DEBUG 18604 --- [o-auto-1-exec-3] c.agileframework.log.PrintLogProvider    : IP  地址: 192.168.101.42
2020-08-18 18:56:41.870 DEBUG 18604 --- [o-auto-1-exec-3] c.agileframework.log.PrintLogProvider    : URL 地址: GET:/test2
2020-08-18 18:56:41.870 DEBUG 18604 --- [o-auto-1-exec-3] c.agileframework.log.PrintLogProvider    : 帐    号: anonymous
2020-08-18 18:56:41.870 DEBUG 18604 --- [o-auto-1-exec-3] c.agileframework.log.PrintLogProvider    : 耗    时: 2ms
2020-08-18 18:56:41.870 DEBUG 18604 --- [o-auto-1-exec-3] c.agileframework.log.PrintLogProvider    : 入    参: {"a":["12"]}
2020-08-18 18:56:41.870 DEBUG 18604 --- [o-auto-1-exec-3] c.agileframework.log.PrintLogProvider    : 出    参: {"a":"输出"}
2020-08-18 18:56:41.870 DEBUG 18604 --- [o-auto-1-exec-3] c.agileframework.log.PrintLogProvider    : ----------------------------------------------------------
2020-08-18 18:56:42.221  INFO 18604 --- [extShutdownHook] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'\
```
#### 步骤 4: 自定义扩展
可以通过声明`cloud.agileframework.log.ExecutionObjectProvider`Bean的方式，对运行过程日志进行扩展，接口仅有一个方法，非常简单，如下
```
    /**
     * 请求执行过程，可以用于记录操作日志
     * @param executionInfo 执行信息
     */
    void pass(ExecutionInfo executionInfo);
```
