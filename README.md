# SpringBoot 管理后台标准规范模板

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0.32-blue.svg)](https://www.mysql.com/)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5.3-red.svg)](https://baomidou.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 📋 项目简介

这是一个基于 **Spring Boot** 的企业级后台管理系统标准规范模板，采用当前主流技术栈，遵循阿里巴巴 Java 开发手册规范，为企业级应用开发提供稳定、高效、可扩展的基础框架。

### 🎯 项目特色

- ✨ **标准规范架构**：采用经典三层架构，代码结构清晰，易于维护
- 🚀 **主流技术栈**：集成 Spring Boot、MyBatis Plus、Redis 等热门技术
- 🛡️ **完善异常处理**：统一异常处理机制，规范错误响应格式
- 📖 **详细代码注释**：每个模块都有完善的注释，降低学习成本
- 🔧 **开箱即用**：提供完整的项目模板，快速启动开发
- 📏 **开发规范**：遵循阿里巴巴 Java 开发手册，保证代码质量

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|-----|------|------|
| Spring Boot | 2.7.18 | 基础开发框架，LTS 长期支持版本 |
| Spring Web | 2.7.18 | Web 开发和 RESTful API |
| MyBatis Plus | 3.5.3 | 持久层框架，简化 CRUD 操作 |
| MySQL | 8.0.32 | 关系型数据库 |
| Redis | Latest | 缓存和会话存储 |
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
│   │   └── ResultCode.java        # 响应状态码枚举
│   ├── controller/                 # 控制层
│   │   └── UserController.java    # 用户控制器
│   ├── domain/                     # 领域模型
│   │   └── vo/                    # 视图对象
│   │       └── UserVO.java        # 用户视图对象
│   └── exception/                  # 异常处理
│       ├── AuthException.java      # 认证异常
│       ├── BaseException.java      # 基础异常类
│       ├── BusinessException.java  # 业务异常
│       ├── GlobalExceptionHandler.java # 全局异常处理器
│       ├── ParamException.java     # 参数异常
│       └── PermissionDeniedException.java # 权限拒绝异常
├── src/main/resources/
│   └── application.yml             # 应用配置文件
├── pom.xml                        # Maven 依赖配置
├── .gitignore                     # Git 忽略文件配置
└── README.md                      # 项目说明文档
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
git clone <项目地址>
cd springboot_admin_template
```

### 3. 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE IF NOT EXISTS admin_template DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/admin_template?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 4. 启动应用

```bash
# 方式一：使用 Maven
mvn spring-boot:run

# 方式二：使用 IDE
直接运行 com.zzy.admin.App 类

# 方式三：打包后运行
mvn clean package
java -jar target/springboot_admin_template-1.0-SNAPSHOT.jar
```

### 5. 验证启动

访问：http://localhost:8070

## 📋 开发规范

### 代码规范

本项目严格遵循以下开发规范：

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

### 接口规范

#### 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2024-01-01T12:00:00"
}
```

#### 响应状态码

| 状态码 | 说明 |
|-------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 数据库规范

1. **表命名**：使用小写字母和下划线，如 `sys_user`
2. **字段命名**：使用小写字母和下划线，如 `user_name`
3. **主键**：统一使用 `id` 作为主键，类型为 `BIGINT`
4. **时间字段**：创建时间 `create_time`，更新时间 `update_time`

## 📚 API 接口文档

### 用户管理接口

#### 获取用户信息
- **接口地址**：`GET /api/user/{id}`
- **请求参数**：
  - `id`：用户ID（路径参数）
- **响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com"
  }
}
```

## 🔧 配置说明

### 应用配置（application.yml）

```yaml
# 服务器配置
server:
  port: 8070                    # 服务端口

# Spring 配置
spring:
  # 数据源配置
  datasource:
    url: jdbc:mysql://localhost:3306/admin_template
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  # Redis 配置
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0

# MyBatis Plus 配置
mybatis-plus:
  mapper-locations: classpath*:mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

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

3. **Docker 部署**（可选）
```dockerfile
FROM openjdk:11-jre-slim
COPY target/springboot_admin_template-1.0-SNAPSHOT.jar app.jar
EXPOSE 8070
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

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

### v1.0.0 (2024-01-01)
- ✨ 初始化项目模板
- 🎯 集成 Spring Boot 2.7.18
- 📦 集成 MyBatis Plus
- 🛡️ 完善异常处理机制
- 📋 建立项目开发规范

---

**💡 如果这个项目对您有帮助，请给个 ⭐ Star 支持一下！** 