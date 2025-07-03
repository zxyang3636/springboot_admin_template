# SpringBoot 管理后台标准规范模板

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0.32-blue.svg)](https://www.mysql.com/)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5.3-red.svg)](https://baomidou.com/)
[![JWT](https://img.shields.io/badge/JWT-0.11.5-purple.svg)](https://github.com/jwtk/jjwt)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 📋 项目简介

这是一个基于 **Spring Boot** 的企业级后台管理系统标准规范模板，采用 **JWT 双令牌机制**，集成主流技术栈，遵循阿里巴巴 Java 开发手册规范，为企业级应用开发提供稳定、高效、可扩展的基础框架。

### 🎯 项目特色

- ✨ **标准规范架构**：采用经典三层架构，代码结构清晰，易于维护
- 🔐 **JWT 双令牌认证**：访问令牌 + 刷新令牌，安全性与用户体验并重
- 🚀 **主流技术栈**：集成 Spring Boot、MyBatis Plus、Redis、JWT 等热门技术
- 🛡️ **完善安全机制**：登录拦截器、用户上下文管理、令牌自动刷新
- 📖 **详细代码注释**：每个模块都有完善的注释，降低学习成本
- 🔧 **开箱即用**：提供完整的用户认证和授权功能
- 📏 **开发规范**：遵循阿里巴巴 Java 开发手册，保证代码质量
- 🔒 **配置安全**：敏感配置文件不入库，提供配置模板

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|-----|------|------|
| Spring Boot | 2.7.18 | 基础开发框架，LTS 长期支持版本 |
| Spring Web | 2.7.18 | Web 开发和 RESTful API |
| MyBatis Plus | 3.5.3 | 持久层框架，简化 CRUD 操作 |
| MySQL | 8.0.32 | 关系型数据库 |
| Redis | Latest | 缓存和会话存储 |
| JJWT | 0.11.5 | JWT 令牌生成和验证 |
| FastJSON2 | 2.0.48 | 高性能 JSON 处理库 |
| Hutool | 5.8.22 | Java 工具类库 |
| Lombok | Latest | 简化 Java 代码编写 |

### 开发环境

| 工具 | 版本要求 | 说明 |
|-----|---------|------|
| JDK | 11+ | Java 开发环境 |
| Maven | 3.6+ | 项目构建工具 |
| IntelliJ IDEA | 2020.3+ | 推荐开发工具 |
| MySQL | 8.0+ | 数据库服务 |
| Redis | 6.0+ | 缓存服务 |

## 📁 项目结构

```
springboot_admin_template/
├── src/main/java/com/zzy/admin/
│   ├── App.java                    # 启动类
│   ├── common/                     # 通用模块
│   │   ├── Result.java            # 统一响应结果封装
│   │   ├── ResultCode.java        # 响应状态码枚举
│   │   ├── UserContext.java       # 用户上下文对象
│   │   ├── UserContextHolder.java # 用户上下文管理器
│   │   └── constant/              # 常量定义
│   ├── controller/                 # 控制层
│   │   └── UserController.java    # 用户控制器
│   ├── service/                    # 服务层
│   │   ├── SysUserService.java    # 用户服务接口
│   │   └── impl/                  # 服务实现
│   │       ├── SysUserServiceImpl.java # 用户服务实现
│   │       └── UserContextService.java # 用户上下文服务
│   ├── mapper/                     # 数据访问层
│   │   └── SysUserMapper.java     # 用户数据访问
│   ├── domain/                     # 领域模型
│   │   ├── po/                    # 持久化对象
│   │   │   └── SysUser.java       # 用户实体
│   │   ├── vo/                    # 视图对象
│   │   │   └── UserVO.java        # 用户视图对象
│   │   └── dto/                   # 数据传输对象
│   │       └── RefreshRequest.java # 刷新令牌请求
│   ├── utils/                      # 工具类
│   │   └── JwtUtil.java           # JWT 工具类
│   ├── interceptor/               # 拦截器
│   │   └── LoginInterceptor.java  # 登录拦截器
│   ├── config/                    # 配置类
│   │   └── WebMvcConfig.java      # Web MVC 配置
│   └── exception/                  # 异常处理
│       ├── AuthException.java      # 认证异常
│       ├── BaseException.java      # 基础异常类
│       ├── BusinessException.java  # 业务异常
│       ├── GlobalExceptionHandler.java # 全局异常处理器
│       ├── ParamException.java     # 参数异常
│       └── PermissionDeniedException.java # 权限拒绝异常
├── src/main/resources/
│   ├── application-example.yml    # 配置模板文件
│   └── application.yml            # 应用配置文件（不入库）
├── pom.xml                        # Maven 依赖配置
├── .gitignore                     # Git 忽略文件配置
├── CONFIG.md                      # 配置说明文档
└── README.md                      # 项目说明文档
```

## 🔐 认证架构

### JWT 双令牌机制

```
登录成功 → 返回访问令牌(1小时) + 刷新令牌(7天)
     ↓
用户携带访问令牌访问接口
     ↓
访问令牌过期 → 前端自动用刷新令牌换取新的访问令牌
     ↓
继续正常使用系统
     ↓
刷新令牌过期 → 用户重新登录
```

### 安全特性

- **访问令牌**：短期有效（默认1小时），用于日常API访问
- **刷新令牌**：长期有效（默认7天），仅用于获取新的访问令牌
- **令牌存储**：刷新令牌存储在Redis中，支持强制下线
- **自动续期**：访问令牌快过期时自动刷新，用户无感知
- **安全拦截**：全局登录拦截器，自动验证和设置用户上下文

## 🚀 快速开始

### 1. 环境准备

确保您的开发环境已安装以下软件：
- JDK 11 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本
- Redis 6.0 或更高版本

### 2. 克隆项目

```bash
git clone https://github.com/zxyang3636/springboot_admin_template.git
cd springboot_admin_template
```

### 3. 配置文件设置

1. 复制配置模板：
```bash
cp src/main/resources/application-example.yml src/main/resources/application.yml
```

2. 修改配置文件 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_redis_password

jwt:
  secret: your-jwt-secret-key-at-least-32-characters-long
  expiration: 24    # 访问令牌过期时间（小时）
  refresh: 168      # 刷新令牌过期时间（小时，7天）
```

### 4. 数据库初始化

1. 创建数据库：
```sql
CREATE DATABASE IF NOT EXISTS admin_template DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 创建用户表：
```sql
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码(加密)',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像URL',
  `status` tinyint DEFAULT '0' COMMENT '状态:0启用,1禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 5. 启动应用

```bash
# 方式一：使用 Maven
mvn spring-boot:run

# 方式二：使用 IDE
直接运行 com.zzy.admin.App 类

# 方式三：打包后运行
mvn clean package
java -jar target/springboot_admin_template-1.0-SNAPSHOT.jar
```

### 6. 验证启动

访问：http://localhost:8070

## 📚 API 接口文档

### 认证相关接口

#### 1. 用户登录
- **接口地址**：`POST /user/login`
- **请求参数**：
```json
{
  "username": "admin",
  "password": "123456"
}
```
- **响应示例**：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "avatar": "https://example.com/avatar.jpg",
    "username": "admin",
    "nickname": "管理员"
  },
  "success": true,
  "fail": false,
  "timestamp": 1735123456789
}
```

#### 2. 获取用户信息
- **接口地址**：`GET /user/info`
- **请求头**：`Authorization: Bearer <accessToken>`
- **响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "avatar": "https://example.com/avatar.jpg",
    "username": "admin", 
    "nickname": "管理员"
  },
  "success": true,
  "fail": false,
  "timestamp": 1735123456789
}
```

#### 3. 刷新令牌
- **接口地址**：`POST /user/refresh`
- **请求参数**：
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```
- **响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "avatar": "https://example.com/avatar.jpg",
    "username": "admin",
    "nickname": "管理员"
  },
  "success": true,
  "fail": false,
  "timestamp": 1735123456789
}
```

#### 4. 退出登录
- **接口地址**：`POST /user/logout`
- **请求头**：`Authorization: Bearer <accessToken>`（可选，token过期也能退出）
- **响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null,
  "success": true,
  "fail": false,
  "timestamp": 1735123456789
}
```

### 接口调用说明

#### 认证流程

1. **登录获取令牌**：调用登录接口获取访问令牌和刷新令牌
2. **访问受保护接口**：请求头携带 `Authorization: Bearer <accessToken>`
3. **令牌自动刷新**：访问令牌过期时，前端自动调用刷新接口
4. **退出登录**：清理服务器端的令牌信息

#### 拦截器白名单

以下接口无需携带访问令牌：
- `POST /user/login` - 登录接口
- `POST /user/refresh` - 刷新令牌接口  
- `POST /user/logout` - 退出登录接口

## 🔧 配置说明

### 应用配置（application.yml）

```yaml
# 服务器配置
server:
  port: 8070

# Spring 配置
spring:
  # 数据源配置
  datasource:
    url: jdbc:mysql://localhost:3306/admin_template?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  # Redis 配置
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
    timeout: 3000
    jedis:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0

# MyBatis Plus 配置
mybatis-plus:
  mapper-locations: classpath*:mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

# JWT 配置
jwt:
  secret: your-jwt-secret-key-at-least-32-characters-long
  expiration: 24    # 访问令牌过期时间（小时）
  refresh: 168      # 刷新令牌过期时间（小时，7天）
  token-prefix: "Bearer "
  header: "Authorization"
  salt: your-password-salt

# 日志配置
logging:
  level:
    com.zzy.admin: debug
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### 配置文件安全管理

- **配置模板**：`application-example.yml` 提供配置示例，可安全提交到Git
- **实际配置**：`application.yml` 包含敏感信息，已加入`.gitignore`，不会提交到Git
- **快速配置**：新环境部署时，复制模板文件并修改相应配置即可

## 📋 开发规范

### 代码规范

1. **阿里巴巴 Java 开发手册**
   - 命名规范：类名使用 UpperCamelCase，方法名使用 lowerCamelCase
   - 包名全部小写，连续的单词直接拼接，不使用下划线
   - 常量名全部大写，单词间用下划线分隔

2. **分层架构规范**
   ```
   Controller 层：负责接收请求参数，调用 Service 处理业务，返回结果
   Service 层：   负责业务逻辑处理，事务控制
   Mapper 层：    负责数据访问，与数据库交互
   ```

3. **异常处理规范**
   - 统一使用 `Result<T>` 封装响应结果
   - 业务异常继承 `BaseException`
   - 全局异常处理器统一处理异常

### 响应格式规范

#### 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "success": true,
  "fail": false,
  "timestamp": 1735123456789
}
```

#### 响应状态码

| 状态码 | 说明 |
|-------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未认证 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 📦 部署说明

### 开发环境部署

```bash
mvn spring-boot:run
```

### 生产环境部署

1. **打包应用**
```bash
mvn clean package -Dmaven.test.skip=true
```

2. **运行应用**
```bash
java -jar -Dspring.profiles.active=prod target/springboot_admin_template-1.0-SNAPSHOT.jar
```

3. **Docker 部署**
```dockerfile
FROM openjdk:11-jre-slim
COPY target/springboot_admin_template-1.0-SNAPSHOT.jar app.jar
EXPOSE 8070
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🔍 故障排查

### 常见问题

1. **启动失败**
   - 检查配置文件是否正确
   - 确认数据库和Redis连接正常
   - 查看启动日志定位具体错误

2. **登录失败**
   - 检查用户密码是否正确（需要MD5加密）
   - 确认用户状态是否为启用状态
   - 查看数据库用户表数据

3. **令牌验证失败**
   - 检查JWT密钥配置
   - 确认令牌格式是否正确
   - 查看令牌是否过期

4. **Redis连接失败**
   - 检查Redis服务是否启动
   - 确认连接配置是否正确
   - 查看网络连接状态

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支：`git checkout -b feature/new-feature`
3. 提交更改：`git commit -am 'Add some feature'`
4. 推送分支：`git push origin feature/new-feature`
5. 提交 Pull Request

## 📄 许可证

本项目采用 MIT 许可证，详情请参阅 [LICENSE](LICENSE) 文件。

## 👥 团队

- **开发者**：zzy
- **项目地址**：https://github.com/zxyang3636/springboot_admin_template

## 🔮 更新日志

### v1.2.0 (2024-07-03) - 当前版本
- 🔐 **新增 JWT 双令牌认证机制**：访问令牌 + 刷新令牌，提升安全性和用户体验
- 👤 **新增用户上下文管理**：UserContext 和 UserContextHolder，简化用户信息获取
- 🛡️ **新增登录拦截器**：自动验证令牌和设置用户上下文
- 🔄 **新增令牌刷新机制**：支持自动刷新访问令牌，用户无感知续期
- 🚪 **完善退出登录**：支持令牌过期时也能正常退出登录
- 🔒 **配置文件安全管理**：敏感配置不入库，提供配置模板
- 📚 **完善API接口**：登录、获取用户信息、刷新令牌、退出登录
- 🧪 **支持令牌调试**：可配置短期过期时间便于测试

### v1.0.0 (2024-01-01)
- ✨ 初始化项目模板
- 🎯 集成 Spring Boot 2.7.18
- 📦 集成 MyBatis Plus
- 🛡️ 完善异常处理机制
- 📋 建立项目开发规范

---

**💡 如果这个项目对您有帮助，请给个 ⭐ Star 支持一下！** 