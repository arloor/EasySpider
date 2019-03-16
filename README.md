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