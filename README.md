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
- 🔒 **RSA+AES 混合加密**：前后端数据传输全程加密，保障数据安全
- 📝 **AOP 日志记录**：基于注解的业务操作日志，支持FastJSON2序列化
- 🚀 **主流技术栈**：集成 Spring Boot、MyBatis Plus、Redis、JWT 等热门技术
- 🛡️ **完善安全机制**：登录拦截器、用户上下文管理、令牌自动刷新
- 📖 **详细代码注释**：每个模块都有完善的注释，降低学习成本
- 🔧 **开箱即用**：提供完整的用户认证和授权功能
- 📏 **开发规范**：遵循阿里巴巴 Java 开发手册，保证代码质量
- 🔒 **配置安全**：敏感配置文件不入库，支持JKS密钥存储

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|-----|------|------|
| Spring Boot | 2.7.18 | 基础开发框架，LTS 长期支持版本 |
| Spring Web | 2.7.18 | Web 开发和 RESTful API |
| Spring AOP | 2.7.18 | 面向切面编程，用于日志记录等 |
| MyBatis Plus | 3.5.3 | 持久层框架，简化 CRUD 操作 |
| MySQL | 8.0.32 | 关系型数据库 |
| Redis | Latest | 缓存和会话存储 |
| JJWT | 0.11.5 | JWT 令牌生成和验证 |
| FastJSON2 | 2.0.48 | 高性能 JSON 处理库，用于日志序列化 |
| Hutool | 5.8.22 | Java 工具类库 |
| Lombok | Latest | 简化 Java 代码编写 |
| CryptoJS | Latest | 前端加密解密库（前端依赖） |

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
│   ├── App.java                    # 启动类（含个性化Banner）
│   ├── annotation/                 # 自定义注解
│   │   ├── LogRecord.java         # 日志记录注解
│   │   └── AnonymousAccess.java   # 匿名访问注解
│   ├── aspect/                     # 切面类
│   │   └── LogRecordAspect.java   # 日志记录切面
│   ├── common/                     # 通用模块
│   │   ├── Result.java            # 统一响应结果封装
│   │   ├── ResultCode.java        # 响应状态码枚举
│   │   ├── UserContext.java       # 用户上下文对象
│   │   ├── UserContextHolder.java # 用户上下文管理器
│   │   └── constant/              # 常量定义
│   │       └── RedisConstant.java # Redis常量
│   ├── controller/                 # 控制层
│   │   ├── UserController.java    # 用户控制器
│   │   └── AuthController.java    # 认证控制器（公钥等）
│   ├── service/                    # 服务层
│   │   ├── SysUserService.java    # 用户服务接口
│   │   └── impl/                  # 服务实现
│   │       └── SysUserServiceImpl.java # 用户服务实现
│   ├── mapper/                     # 数据访问层
│   │   └── SysUserMapper.java     # 用户数据访问
│   ├── domain/                     # 领域模型
│   │   ├── po/                    # 持久化对象
│   │   │   ├── SysUser.java       # 用户实体
│   │   │   └── OperationLog.java  # 操作日志实体
│   │   ├── vo/                    # 视图对象
│   │   │   └── UserVO.java        # 用户视图对象
│   │   └── dto/                   # 数据传输对象
│   │       └── RefreshRequest.java # 刷新令牌请求
│   ├── utils/                      # 工具类
│   │   ├── JwtUtil.java           # JWT 工具类
│   │   ├── CryptoUtils.java       # 加密解密工具类
│   │   └── LogUtils.java          # 日志工具类
│   ├── interceptor/               # 拦截器
│   │   └── LoginInterceptor.java  # 登录拦截器
│   ├── config/                    # 配置类
│   │   ├── WebMvcConfig.java      # Web MVC 配置
│   │   ├── JwtProperties.java     # JWT属性配置
│   │   └── RedisConfig.java       # Redis配置
│   └── exception/                  # 异常处理
│       ├── AuthException.java      # 认证异常
│       ├── BaseException.java      # 基础异常类
│       ├── BusinessException.java  # 业务异常
│       ├── GlobalExceptionHandler.java # 全局异常处理器
│       ├── ParamException.java     # 参数异常
│       └── PermissionDeniedException.java # 权限拒绝异常
├── src/main/resources/
│   ├── application-example.yml    # 配置模板文件
│   ├── application.yml            # 应用配置文件（不入库）
│   ├── zzy.jks                    # JKS密钥存储文件
│   └── mapper/                    # MyBatis映射文件
│       └── SysUserMapper.xml     # 用户数据访问映射
├── pom.xml                        # Maven 依赖配置
├── .gitignore                     # Git 忽略文件配置
└── README.md                      # 项目说明文档
```

## 🔐 安全架构

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

### RSA+AES 混合加密

```
前端流程：
1. 获取服务器RSA公钥
2. 生成随机AES密钥和IV
3. 使用AES加密敏感数据（如密码）
4. 使用RSA公钥加密AES密钥
5. 发送加密数据到服务器

后端流程：
1. 从JKS文件加载RSA私钥
2. 使用RSA私钥解密AES密钥
3. 使用AES密钥和IV解密数据
4. 处理业务逻辑
```

### 安全特性

- **访问令牌**：短期有效（默认1小时），用于日常API访问
- **刷新令牌**：长期有效（默认7天），仅用于获取新的访问令牌
- **令牌存储**：刷新令牌存储在Redis中，支持强制下线
- **自动续期**：访问令牌快过期时自动刷新，用户无感知
- **安全拦截**：全局登录拦截器，自动验证和设置用户上下文
- **数据加密**：敏感数据传输采用RSA+AES混合加密
- **密钥管理**：使用JKS文件安全存储密钥对

## 📝 日志记录系统

### 功能特性

- **基于AOP**：使用 `@LogRecord` 注解标记需要记录的方法
- **智能序列化**：使用FastJSON2进行参数和结果序列化
- **过滤机制**：自动过滤不可序列化对象和敏感信息
- **性能优化**：支持异步日志记录，不影响业务性能
- **JDK 1.8兼容**：完全兼容JDK 1.8环境

### 使用方式

```java
@PostMapping("/login")
@LogRecord(value = "用户登录", businessType = "认证")
public Result<?> login(@RequestBody SysUser sysUser) {
    return sysUserService.login(sysUser);
}
```

### 日志输出示例

```
2025-07-05 10:48:27 [http-nio-8070-exec-1] INFO  c.z.a.aspect.LogRecordAspect - 
=== 操作完成 === | 操作: 用户登录 | 状态: SUCCESS | 执行时间: 1139ms | 操作人: admin | 结果: {...}
```

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


### 3. 启动应用

```bash
# 方式一：使用 Maven
mvn spring-boot:run

# 方式二：使用 IDE
直接运行 com.zzy.admin.App 类

# 方式三：打包后运行
mvn clean package
java -jar target/springboot_admin_template-1.0-SNAPSHOT.jar
```

### 4. 验证启动

访问：http://localhost:8070


### 接口调用说明

#### 认证流程

1. **获取公钥**：调用获取公钥接口，用于加密传输
2. **加密登录**：使用混合加密方式安全传输登录信息
3. **获取令牌**：登录成功后获取访问令牌和刷新令牌
4. **访问受保护接口**：请求头携带 `Authorization: Bearer <accessToken>`
5. **令牌自动刷新**：访问令牌过期时，前端自动调用刷新接口
6. **退出登录**：清理服务器端的令牌信息




### 配置文件安全管理

- **配置模板**：`application-example.yml` 提供配置示例，可安全提交到Git
- **实际配置**：`application.yml` 包含敏感信息，已加入`.gitignore`，不会提交到Git
- **密钥管理**：使用JKS文件存储RSA密钥对，支持密码保护
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

4. **日志记录规范**
   - 使用 `@LogRecord` 注解记录业务操作
   - 重要业务操作必须记录日志
   - 敏感信息自动过滤，避免泄露

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
COPY src/main/resources/zzy.jks /app/zzy.jks
EXPOSE 8070
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 安全部署建议

1. **密钥管理**：
   - 生产环境使用独立的JKS文件
   - 定期轮换密钥对
   - 严格控制密钥文件访问权限

2. **配置安全**：
   - 不要在代码中硬编码敏感信息
   - 使用环境变量或外部配置管理敏感配置
   - 启用HTTPS确保传输安全

3. **监控告警**：
   - 监控异常登录行为
   - 记录关键操作日志
   - 设置安全告警机制

## 🔍 故障排查

### 常见问题

1. **启动失败**
   - 检查配置文件是否正确
   - 确认数据库和Redis连接正常
   - 查看启动日志定位具体错误
   - 检查JKS文件是否存在且密码正确

2. **登录失败**
   - 检查用户密码是否正确
   - 确认用户状态是否为启用状态
   - 查看数据库用户表数据
   - 检查加密解密流程是否正确

3. **令牌验证失败**
   - 检查JWT密钥配置
   - 确认令牌格式是否正确
   - 查看令牌是否过期
   - 检查请求头格式

4. **加密解密错误**
   - 确认前后端加密算法一致
   - 检查IV长度是否为16字节
   - 验证密钥格式是否正确
   - 查看序列化配置

5. **日志记录异常**
   - 检查切面是否生效
   - 确认方法可见性（public）
   - 查看FastJSON2依赖
   - 验证过滤配置

6. **Redis连接失败**
   - 检查Redis服务是否启动
   - 确认连接配置是否正确
   - 查看网络连接状态

## 🚀 性能优化

### 缓存策略

- **用户信息缓存**：Redis存储用户基础信息，减少数据库查询
- **令牌缓存**：利用Redis的过期机制管理令牌生命周期
- **公钥缓存**：RSA公钥缓存到内存，避免重复生成

### 异步处理

- **日志异步写入**：使用异步方式记录操作日志，提高响应速度
- **令牌异步刷新**：后台异步处理令牌刷新逻辑

### 数据库优化

- **连接池配置**：合理配置数据库连接池参数
- **索引优化**：为频繁查询字段添加索引
- **分页查询**：大数据量查询使用分页避免内存溢出

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

### v1.3.0 (2025-07-05) - 当前版本
- 🔒 **新增 RSA+AES 混合加密**：前后端数据传输全程加密，保障敏感信息安全
- 📝 **新增 AOP 日志记录系统**：基于 `@LogRecord` 注解的业务操作日志，支持FastJSON2序列化
- 🔑 **新增 JKS 密钥存储**：使用Java KeyStore安全存储RSA密钥对，支持密码保护
- 🛡️ **拦截器优化升级**：支持注解配置、配置文件、路径约定三种白名单模式
- ⚡ **序列化引擎升级**：从Jackson迁移到FastJSON2，提升性能并解决序列化兼容性问题
- 🔧 **加密工具类完善**：提供完整的前后端加密解密工具类和使用示例
- 📊 **操作日志数据模型**：新增OperationLog实体和数据表，支持操作审计
- 🚀 **智能参数过滤**：自动过滤不可序列化对象和敏感信息，提升日志安全性

### v1.2.0 (2024-07-03)
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
