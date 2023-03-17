


# Sky-User-Center项目介绍

## 项目简介

一个简单的用户管理系统，实现了用户注册、登录、查询等基础功能。

>  本项目前后端均使用Docker容器部署

**项目在线访问链接：**http://skyuc.twintea.top



[登录]

（管理员：账号：onlineadmin；密码：12345678）

![image-20230312171048928](https://img.twintea.top/Projects_Notes/sky-user-center-readme/202303121800164.png)



[注册] （注册码为后续优化部分，目前在注册时可以随便填）

默认注册成功之后为普通用户

![image-20230312171306695](https://img.twintea.top/Projects_Notes/sky-user-center-readme/202303121800165.png)



普通用户的登录成功后的展示页面：

（没有管理页面）

![image-20230312171834233](https://img.twintea.top/Projects_Notes/sky-user-center-readme/202303121800166.png)



当普通用户尝试访问管理页面时候：

![image-20230312171940333](https://img.twintea.top/Projects_Notes/sky-user-center-readme/202303121800167.png)





具有管理员权限的用户登录：

![image-20230312172121805](https://img.twintea.top/Projects_Notes/sky-user-center-readme/202303121800168.png)





## 技术选型

### 前端🐜

主要运用阿里 Ant Design 生态：



- HTML + CSS + JavaScript 三件套
- React 开发框架
- Ant Design Pro 项目模板
- Ant Design 端组件库
- Umi 开发框架
- Umi Request 请求库



### 后端🍃

- Java 编程语言
- Spring + SpringMVC + SpringBoot 框架
- MyBatis + MyBatis Plus 数据访问框架
- MySQL 数据库
- jUnit 单元测试库



### 部署

详见
[项目部署和上线](https://blog.twintea.top/2023/03/08/%E9%A1%B9%E7%9B%AE%E9%83%A8%E7%BD%B2%E5%92%8C%E4%B8%8A%E7%BA%BF/)


- 单机部署

    - 前端：

      ```bash
      //安装依赖
      yarn
      //项目启动
      npm run start:no-mock
      ```



- 后端

  修改application.yml的数据库配置

  然后在idea里面找到启动类启动就行了



- Nginx

- 容器



### 开发笔记🤔

[Sky-User-Center开发笔记](https://blog.twintea.top/2023/03/08/%E7%94%A8%E6%88%B7%E4%B8%AD%E5%BF%83%E5%BC%80%E5%8F%91%E7%AC%94%E8%AE%B0/)



### 后续优化🤭

(~~看情况，当后续项目用到管理系统再以此为模板更新~~)

1. 功能扩充
    1. 管理员创建用户、修改用户信息、删除用户
    2. 上传头像
    3. 按照更多的条件去查询用户
    4. 更改权限
2. 修改 Bug
3. 项目登录改为分布式 session（单点登录 - redis）
4. 通用性
    1. set-cookie domain 域名更通用，比如改为 *.xxx.com
    2. 把用户管理系统 => 用户中心（之后所有的服务都请求这个后端）
5. 后台添加全局请求拦截器（统一去判断用户权限、统一记录请求日志）