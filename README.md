# 📄 Resume Builder — 在线简历生成与管理平台

一款面向求职者的在线简历生成与管理平台，涵盖简历素材池管理、模块化简历组装与预览、求职进度追踪等完整业务闭环。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.2.5 / Java 17 |
| ORM | MyBatis-Plus 3.5.6 |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis 7 |
| 认证 | Sa-Token 1.38 + JWT |
| 前端 | Vue 3 + TypeScript + Vite 8 |
| UI 组件 | Element Plus 2 |
| 文档解析 | Apache Tika 2.9 |
| PDF 渲染 | Flying Saucer 9.4 |
| 基础设施 | Docker Compose |

## 项目结构

```
├── backend/                    # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/java/com/resumebuilder/
│       ├── annotation/         # 自定义注解 (@OperationLog)
│       ├── aspect/             # AOP 切面
│       ├── common/             # 全局异常处理、统一响应
│       ├── config/             # Sa-Token、Redis、CORS、MyBatis-Plus 配置
│       ├── controller/         # REST 控制器
│       ├── dto/                # 数据传输对象
│       ├── entity/             # 数据库实体
│       ├── mapper/             # MyBatis-Plus Mapper
│       └── service/            # 业务逻辑层
├── frontend/                   # Vue 3 前端
│   ├── src/
│   │   ├── api/                # Axios 请求封装
│   │   ├── layouts/            # 布局组件
│   │   ├── router/             # 路由配置
│   │   ├── store/              # Pinia 状态管理
│   │   └── views/              # 页面视图
│   │       ├── material/       # 素材仓库页面
│   │       ├── resume/         # 简历管理页面
│   │       ├── job/            # 投递管理页面
│   │       └── system/         # 系统管理页面
│   └── vite.config.ts
├── docker-compose.yml          # MySQL + Redis 容器编排
└── README.md
```

## 功能模块

### 素材仓库
独立管理可复用的求职素材，包括基本信息、教育经历、项目经历、工作经历、技能标签、证书奖项、自我评价。支持文件导入（PDF/Word 自动解析）。

### 简历管理
基于素材池的模块化简历组装——按需勾选素材、控制模块显隐与排序，同一份素材可生成多份差异化简历。支持预览与服务端 PDF 导出。

### 投递追踪
记录投递公司、职位、渠道、状态等信息，提供按状态和渠道的聚合统计。

### 操作审计
基于 AOP 的声明式操作日志，自动记录所有增删改操作的操作人、模块、参数及 IP。

## 技术亮点

- **素材池与简历组装解耦**：用户维护一套可复用经历数据，简历组装时按需勾选素材、控制段落显隐与排序，实现同一经历在不同简历中的差异化呈现
- **Redis 缓存优化**：针对简历模板、预览组装结果等高频读取数据，基于 Spring Cache + Redis 构建缓存层，素材变更时主动失效，降低数据库重复查询压力
- **服务端 PDF 渲染**：基于 Flying Saucer 实现 HTML → PDF 服务端渲染，自动探测系统中文字体并嵌入，生成可直接投递的 PDF 文件流
- **AOP 声明式操作审计**：自定义 `@OperationLog` 注解，结合 Spring AOP 切面 + `@Async` 异步落库，在不侵入业务代码的前提下实现操作全量留痕
- **文件上传与智能解析**：基于 Apache Tika 实现 PDF/Word 简历文件内容提取，通过正则匹配与关键词章节切分自动解析为结构化数据，一键导入素材池

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.8+
- Node.js 18+
- Docker & Docker Compose

### 1. 启动基础设施
```bash
docker-compose up -d
```

### 2. 启动后端
```bash
cd backend
mvn spring-boot:run
```

### 3. 启动前端
```bash
cd frontend
npm install
npm run dev
```

### 4. 访问
- 前端页面：http://localhost:3000
- API 文档：http://localhost:8080/doc.html
- 测试账号：`test` / `123456`

## API 概览

| 模块 | 端点 |
|------|------|
| 用户 | `/api/user/register` `/api/user/login` `/api/user/logout` |
| 基本信息 | `/api/basic-info` |
| 教育经历 | `/api/education` |
| 项目经历 | `/api/project-experience` |
| 工作经历 | `/api/work-experience` |
| 技能标签 | `/api/skill-tag` |
| 证书奖项 | `/api/certificate` |
| 自我评价 | `/api/self-evaluation` |
| 简历管理 | `/api/resume` |
| 简历组装 | `/api/resume/{id}/assemble` |
| PDF 导出 | `/api/resume/{id}/export-pdf` |
| 文件导入 | `/api/file/upload-resume` `/api/file/import-resume` |
| 操作日志 | `/api/operation-log` |
| 投递记录 | `/api/job-application` |
