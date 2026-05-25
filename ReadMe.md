# CodeSecOne - Java 代码审计学习靶场

一个基于 Spring Boot 的故意存在漏洞的 Java Web 应用，专为学习和练习 **Java 代码安全审计** 而设计。项目参考了 [java-sec-code](https://github.com/JoyChou93/java-sec-code) 和 [Hello-java-Sec](https://github.com/j3ers3/Hello-java-Sec) ，并在此基础上进行了扩展和完善。这次主要是 [宇子哥](https://github.com/aouar001) 帮我把 ReamMe 更新了一下。前端页面暂时未完善，主要还是直接看后端controller代码逻辑进行学习，前端页面以后再完善吧。

每个漏洞模块均提供 **漏洞触发接口** 和 **安全修复接口**，方便对比学习漏洞原理与防御方式。

---

## 目录

- [CodeSecOne - Java 代码审计学习靶场](#codesecone---java-代码审计学习靶场)
    - [目录](#目录)
    - [项目架构](#项目架构)
    - [技术栈](#技术栈)
        - [故意引入的漏洞依赖](#故意引入的漏洞依赖)
    - [前端界面](#前端界面)
    - [漏洞模块概览](#漏洞模块概览)
        - [漏洞分类](#漏洞分类)
    - [部署方式](#部署方式)
        - [前置要求](#前置要求)
        - [方式一：手动编译部署](#方式一手动编译部署)
        - [方式二：IDE 直接运行](#方式二ide-直接运行)
        - [数据库初始化](#数据库初始化)
    - [项目结构](#项目结构)
    - [安全工具类](#安全工具类)
    - [注意事项](#注意事项)
    - [致谢](#致谢)
    - [免责声明](#免责声明)

---

## 项目架构

```
┌─────────────────────────────────────────────────────┐
│                   Spring Boot 2.4.1                 │
├───────────┬──────────┬────────────┬──────────────────┤
│ Thymeleaf │  MyBatis │  Shiro     │   Swagger        │
│ (模板引擎) │  (ORM)   │ (认证鉴权)  │    (API文档)     │
├───────────┴──────────┴────────────┴──────────────────┤
│                        交互式前端                     │
│                     个别漏洞分类页面                   │
├─────────────────────────────────────────────────────┤
│                    Controller 层                    │
│   SQLi | XSS | RCE | SSTI | SpEL | XXE | SSRF      │
│   反序列化 | JNDI | 文件操作 | URL跳转 | CORS/JSONP    │
│   JWT | 越权 | XPath | IP伪造 | DoS | 未授权访问       │
├─────────────────────────────────────────────────────┤
│          Service 层 / Mapper 层 / Entity 层          │
├─────────────────────────────────────────────────────┤
│                      MySQL 数据库                    │
└─────────────────────────────────────────────────────┘
```

---

## 技术栈

| 类别 | 技术 | 版本 | 说明 |
|------|------|------|------|
| 核心框架 | Spring Boot | 2.4.1 | 故意降级以暴露 Thymeleaf SSTI 漏洞 |
| 模板引擎 | Thymeleaf | - | SSTI 漏洞演示 |
| ORM | MyBatis | 2.2.2 | SQL 注入演示 |
| 数据库 | MySQL | 8.0.28 | - |
| 认证 | Apache Shiro | 1.2.4 | Shiro-550 反序列化漏洞 (CVE-2016-4437) |
| 认证 | JWT | java-jwt 4.2.1 / jjwt 0.9.1 | JWT 安全演示 |
| API 文档 | Swagger (SpringFox) | 3.0.0 | 未授权 API 暴露演示 |
| 前端框架 | Bootstrap 5 | 5.3.0 (CDN) | 响应式 UI，无需构建 |

### 故意引入的漏洞依赖

| 依赖 | 版本 | 对应漏洞 |
|------|------|----------|
| Fastjson | 1.2.24 / 1.2.41 | 反序列化 RCE |
| Log4j | 2.8.2 | Log4Shell (CVE-2021-44228) |
| Jackson-databind | 2.11.0 | 反序列化漏洞 |
| Commons Collections | 3.2.1 | CC1-CC6 反序列化链 |
| Commons Collections4 | 4.0 | CC4 反序列化链 |
| XStream | 1.4.14 | XML 反序列化 RCE |
| Hessian | 4.0.63 | Hessian 反序列化 |
| ROME | 1.7.0 | ROME 反序列化链 |
| Groovy | 2.5.6 | GroovyShell 代码执行 |
| xmlbeam | 1.4.14 | XXE 漏洞 |
| Commons JXPath | 1.3 | CVE-2022-41852 |

---

## 前端界面

访问 `http://127.0.0.1:28888/` 可看到 `index.html 页面的` 漏洞展示;

访问 `http://127.0.0.1:28888/login`  默认账号：`admin` / `123456` 。

---


## 漏洞模块概览

### 漏洞分类

- **Injection 注入漏洞** -- SQLi、XSS、SpEL、XPath、JNDI、SSTI
- **Remote Code Execution** -- RCE、Reflection
- **Network & Protocol** -- XXE、SSRF、CORS/JSONP、URL Redirect、JWT
- **File Operations** -- 文件上传/下载/读取
- **Access Control** -- 越权、IP 伪造、信息泄露
- **Advanced Attacks** -- 反序列化、ReDoS


项目包含 **近 20 类漏洞**，每类漏洞均包含 `/vul` (漏洞) 和 `/safe` (安全) 两种实现：

| # | 漏洞类型 | Controller 路径 | 关键漏洞点 |
|---|----------|----------------|-----------|
| 1 | **SQL 注入** | `/SQLI/` | Statement 拼接、`${}` 占位符、ORDER BY 注入 |
| 2 | **XSS** | `/XSS/` | 反射型、存储型 (Cookie) |
| 3 | **RCE 远程命令执行** | `/RCE/` | Runtime.exec、ProcessBuilder、GroovyShell、ScriptEngine |
| 4 | **SSTI 模板注入** | `/SSTI/` | Thymeleaf 模板路径可控、@PathVariable 注入 |
| 5 | **SpEL 表达式注入** | `/SpEL/` | StandardEvaluationContext 任意代码执行 |
| 6 | **XXE 外部实体注入** | `/XXE/` | XMLReader、SAXParser、SAXReader、xmlbeam |
| 7 | **SSRF 服务端请求伪造** | `/SSRF/` | URLConnection、HttpURLConnection |
| 8 | **反序列化** | `/Deserialize/` | Fastjson、Jackson、Shiro-550、XStream、YAML、Hessian、ROME、CC 链 |
| 9 | **JNDI 注入** | `/JNDI/` | InitialContext.lookup()、LDAP/RMI |
| 10 | **文件操作** | `/filepath/` | 路径穿越、任意文件读取/上传/下载 |
| 11 | **URL 跳转** | `/Redirect/` | redirect:、sendRedirect、setHeader("Location") |
| 12 | **CORS/JSONP** | `/CORS/` `/jsonp/` | Origin 反射、通配符 `*`、JSONP 劫持 |
| 13 | **JWT** | `/jwt/` | 弱密钥、Cookie 存储 |
| 14 | **越权访问** | `/BAC/` | 水平/垂直越权、Tomcat URI 规范化绕过 |
| 15 | **XPath 注入** | `/XPath/` | 字符串拼接注入、JXPath CVE-2022-41852 |
| 16 | **IP 伪造** | `/ip/` `/XFF/` | X-Forwarded-For / X-Real-IP 伪造 |
| 17 | **DoS 拒绝服务** | `/DOS/` | 正则回溯导致 ReDoS |
| 18 | **未授权访问** | `/Unauth/` | 拦截器配置不当、Swagger UI 暴露 |
| 19 | **高级攻击** | `/AdvancedAttack/` | ClassLoader 远程类加载、反射机制利用 |

---

## 部署方式

### 前置要求

| 工具 | 版本要求 | 说明 |
|------|---------|------|
| JDK | 1.8 (必须) | 高版本不兼容 |
| Maven | 3.6+ | 编译构建 |
| MySQL | 8.0+ | 数据库 |


### 方式一：手动编译部署

适合需要修改代码或调试的场景。

```bash
# 1. 确认 JDK 版本
java -version  # 需要 1.8

# 2. 初始化数据库
mysql -u root -p < src/main/resources/db.sql
# 或手动执行 src/main/resources/db.sql 中的 SQL 语句

# 3. 修改数据库配置（如需要）
# 编辑 src/main/resources/application.properties
# 修改 spring.datasource.url / username / password

# 4. 编译打包
mvn clean package -DskipTests

# 5. 运行
java -jar target/CodeSecOne-0.0.1-SNAPSHOT.jar

# 5.1 如需 JNDI 漏洞利用，添加 JVM 参数：
java -Dcom.sun.jndi.rmi.object.trustURLCodebase=true -jar target/CodeSecOne-0.0.1-SNAPSHOT.jar

# 5.2 Maven 直接运行（跳过打包）
mvn spring-boot:run
```


### 方式二：IDE 直接运行

适合开发调试。

1. 使用 IntelliJ IDEA 或 Eclipse 导入项目（Maven 项目）
2. 确保 Project SDK 设置为 JDK 1.8
3. 确保 MySQL 已启动并执行了 `db.sql`
4. 运行主类 `com.madaha.codesecone.CodeSecOneApplication`
5. 运行启动后访问：
    - 应用首页：http://127.0.0.1:28888/
    - 登录页面：http://127.0.0.1:28888/login
    - Swagger API：http://127.0.0.1:28888/swagger-ui/index.html

> **IntelliJ IDEA 提示**：如需 JNDI 漏洞利用，在 Run Configuration 的 VM options 中添加：
> `-Dcom.sun.jndi.rmi.object.trustURLCodebase=true`

### 数据库初始化

如果使用已有的 MySQL 实例，需要手动初始化数据库：

```sql
-- 创建数据库和表
source src/main/resources/db.sql;

-- 或手动执行：
CREATE DATABASE IF NOT EXISTS test DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE test;

CREATE TABLE `users` (
                         `id`   int(11) unsigned NOT NULL AUTO_INCREMENT,
                         `user` varchar(50) NOT NULL,
                         `pass` varchar(128) NOT NULL,
                         PRIMARY KEY (`id`)
);

INSERT INTO `users` VALUES (1, 'zhangwei', '123456');
INSERT INTO `users` VALUES (2, 'admin', 'password');

CREATE TABLE `auth` (
                        `id`   int(6) unsigned NOT NULL AUTO_INCREMENT,
                        `user` varchar(50) NOT NULL,
                        `ip`   varchar(50) NOT NULL,
                        `date` varchar(60) NOT NULL,
                        PRIMARY KEY (`id`)
);
```

默认数据库配置 (`application.properties`)：
```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=123456
```

---

## 项目结构

```
CodeSecOne/
├── pom.xml                              # Maven 依赖配置
├── README.md
│
├── src/main/java/com/madaha/codesecone/
│   ├── CodeSecOneApplication.java       # Spring Boot 入口
│   ├── controller/                      # 漏洞演示控制器 (按漏洞类型分包)
│   │   ├── Login.java                   # 登录认证
│   │   ├── sqli/                        # SQL 注入 (JDBC / MyBatis / Hibernate)
│   │   ├── xss/                         # XSS 跨站脚本
│   │   ├── rce/                         # 远程命令执行 (Runtime / ProcessBuilder / Groovy / ScriptEngine / YAML)
│   │   ├── ssti/                        # 服务端模板注入
│   │   ├── spel/                        # SpEL 表达式注入
│   │   ├── xxe/                         # XML 外部实体注入
│   │   ├── ssrf/                        # 服务端请求伪造
│   │   ├── jndi/                        # JNDI 注入 (LDAP/RMI)
│   │   ├── filepath/                    # 文件操作漏洞 (上传/下载/读取/列目录)
│   │   ├── url/                         # URL 跳转
│   │   ├── sopCors/                     # CORS/JSONP
│   │   ├── jwt/                         # JWT 安全
│   │   ├── BAC/                         # 越权访问
│   │   ├── xpath/                       # XPath 注入
│   │   ├── ipforge/                     # IP 伪造
│   │   ├── ddos/                        # DoS 拒绝服务
│   │   ├── unauth/                      # 未授权访问 / 信息泄露
│   │   └── AdvancedAttack/              # 高级攻击 (反序列化 / ClassLoader / 反射)
│   ├── config/                          # 配置类
│   │   ├── MvcConfigurer.java           # MVC 配置 (拦截器、视图映射)
│   │   ├── ShiroConfig.java             # Shiro 认证配置
│   │   ├── SwaggerConfig.java           # Swagger API 文档
│   │   ├── FilterConfig.java            # Servlet 过滤器注册
│   │   └── ServletConfig.java           # Servlet 端点注册
│   ├── entity/                          # 实体类 (User, CMDJson)
│   ├── mapper/                          # MyBatis Mapper 接口
│   ├── service/                         # 业务逻辑
│   ├── util/                            # 工具类 (SecurityUtils, JwtUtils, etc.)
│   ├── Filter/                          # Servlet 过滤器实现
│   └── Servlet/                         # Servlet 端点实现
│
├── src/main/resources/
│   ├── application.properties           # 应用配置 (端口 28888、数据库、Thymeleaf)
│   ├── db.sql                           # 数据库初始化脚本
│   ├── mapper/mapper.xml                # MyBatis XML 映射
│   ├── ESAPI.properties                 # OWASP ESAPI 配置
│   │
│   ├── templates/                       # Thymeleaf 模板
│   │   ├── index.html                   # Dashboard 首页
│   │   ├── login.html                   # 登录页面
│   │   ├── upload.html                  # 文件上传页面
│   │   ├── shiroLogin.html              # Shiro 登录页面
│   │   ├── amis-rce-page.html           # Baidu amis RCE 演示
│   │   ├── commons/
│   │   │   ├── commons.html              # commons 测试页面
│   │   │   ├── 401.html                 # 401 错误页面
│   │   │   └── 403.html                 # 403 错误页面
│   │
│   └── static/
│       ├── img/dog.png                  # Logo 图片
│       ├── evil/                        # 预置漏洞利用 Payload
│       ├── fileUpload/                  # 文件上传测试用例
│       └── amis_sdk/                    # Baidu amis SDK (前端 RCE 演示)
```

---

## 安全工具类

项目提供了一个统一的安全工具类 `SecurityUtils` (`com.madaha.codesecone.util.SecurityUtils`)，包含以下防护方法：

| 方法 | 功能 |
|------|------|
| SQL 注入黑名单过滤 | 关键字检测与过滤 |
| XSS 编码 | HTML 实体编码 |
| XXE 检测 | 外部实体注入检测 |
| 路径穿越过滤 | URL 解码 + `..` 检测 |
| 内网 IP 检测 | SSRF 防护 - 识别内网地址 |
| URL 白名单校验 | SSRF 防护 - 域名白名单 |
| OS 命令检测 | RCE 防护 - 检测管道符和命令连接符 |

---

## 注意事项

1. **仅供学习使用**：本项目包含大量真实漏洞，请勿在生产环境或公网环境部署运行。
2. **网络隔离**：建议在本地虚拟机或 Docker 容器中运行，确保网络隔离。
3. **JDK 版本**：必须使用 JDK 1.8，高版本可能存在兼容性问题。
4. **Spring Boot 版本**：项目故意使用 Spring Boot 2.4.1 (非最新版)，以便暴露 Thymeleaf SSTI 等漏洞。
5. **依赖版本**：所有漏洞依赖均为故意引入的特定版本，切勿在实际项目中使用这些版本。
6. **默认凭据**：数据库密码 `123456`、登录账号 `admin/123456` 仅用于本地学习环境。

---

## 致谢

- [java-sec-code](https://github.com/JoyChou93/java-sec-code) - JoyChou93
- [Hello-java-Sec](https://github.com/j3ers3/Hello-java-Sec) - j3ers3
- [宇子哥](https://github.com/aouar001) - 感谢对 后端逻辑、前端页面 和 ReadMe 的梳理。（我本周四离职，在安全行业从业5年了，准备休息一段时间。再这个离别之际，好哥们还在努力帮我写这“毕业总结”，哈哈哈哈~。~）

---

## 免责声明

本项目仅用于安全研究和教学目的。使用者应遵守当地法律法规，不得将本项目用于任何非法用途。因使用本项目造成的任何后果，由使用者自行承担。
