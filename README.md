[![](https://travis-ci.org/arloor/EasySpider.svg?branch=master)](https://travis-ci.org/arloor/EasySpider)
[![](https://img.shields.io/github/last-commit/arloor/EasySpider.svg?style=flat)](https://github.com/arloor/EasySpider/commit/master)
![](https://img.shields.io/github/languages/code-size/arloor/EasySpider.svg?style=flat)

# EasySpider 一个（并不）简单的爬虫框架

![](http://arloor.com/img/%E7%88%AC%E8%99%AB%E7%B3%BB%E7%BB%9F%E9%83%A8%E7%BD%B2%E5%9B%BE.png)

# 目前进度

groovy执行引擎完成，spider雏形实现。

# proxy-local和proxy-server

使用自己实现的httpproxy。[项目详情](https://github.com/arloor/HttpProxy)

# 为什么能称之为框架？

因为做好了骨架，只需要修改`taskmanager\src\main\resources`下的groovy脚本就可以构建自己的爬虫任务。

目前还是雏形，需要改进的地方是：

1. 将保存任务的queue使用消息队列保存
2. 缺少一个网关来提交任务开始的标志

# Release v1.0

这是EasySpider的第一版。还很简单，源码读起来也轻松，要看代码，从这一版开始吧。之后就会引入分布式了亲。

！！！这里对groovy脚本的使用和“任务上下文”的定义是EasySpider的精髓。

## 代码结构


分为两个模块：

- spider：封装了一个httpclient，Main函数会开启一个Graber线程，循环从队列中取“任务上下文”，消费该上下文，并发送http请求、处理http响应。
- taskmanager： 这一模块是关于上面提到的“任务上下文”。所谓任务上下文就是当前任务需要执行什么操作、当前任务的数据、和当前任务的状态（cookie），这些都定义在stepContext类中。StepContainer类负责加载groovy脚本生成对象。groovy脚本都在resources下，groovy脚本就是用于定义“当前任务执行操作”的，实现了Step接口，定义如何创建http请求和如何解析http响应。

## 其他注意点

httpclient本身可以自己管理cookie。但我选择了自己去维护cookie，而httpclient开启了IGNORE_COOKIE。原因是，httpclient的cookie是绑定在httpclient上，在分布式环境中会存在多个httpclient。这样，一些httpclient执行了关键比如登陆操作，而其他的httpclient没有执行，这样就导致其他httpclient没有登陆之后拿到的cookie。这肯定不行，所以我将cookie信息保存在了“任务上下文”中，这个任务上下文可以在分布式环境中传递，保证关键cookie不丢失。

## 如何使用这个版本的框架

只需要创建自己的groovy脚本就好。groovy脚本实现Step接口，创建请求，解析响应。