# jeesite-typing-service（打字练习微服务）

按 **JeeSite 5.8.1 微服务版** 规范编写的「打字练习」后端模块，提供：

- 打字**文章管理**（CRUD）
- 打字**速度记录**入库
- **速度排名**（按用户取最好成绩）
- 打字**比赛**管理、成绩提交与**排行榜**
- 供 React 玩家端调用的开放 **REST API**

> ⚠️ 重要：本模块是**符合 JeeSite 规范的脚手架**，在当前仓库中**无法独立编译**（缺少你的 JeeSite Cloud 父工程与依赖）。请把它拷入你的 JeeSite 微服务工作区后，按下文「接入清单」调整即可编译运行。包名默认 `com.jeesite.modules.typing`，模块名 `typing`，数据库默认 MySQL。

## 目录结构

```
jeesite-typing-service/
├── pom.xml                         # 模块 POM（parent 指向你的 jeesite-cloud 聚合父工程）
└── src/main/
    ├── java/com/jeesite/modules/typing/
    │   ├── TypingApplication.java          # 启动类（接入后通常由网关/聚合工程统一启动）
    │   ├── entity/   Article / SpeedRecord / Competition / CompetitionResult
    │   ├── dao/      *Dao（@MyBatisDao，标准 CRUD 由 @Table 注解自动生成）
    │   ├── service/  *Service（CrudService，含排名/排行榜聚合）
    │   └── web/      Article/Competition/SpeedRecord 管理端 + TypingApiController 玩家端 API
    └── resources/
        ├── bootstrap.yml / application.yml # Nacos + 数据源占位
        ├── mappings/modules/typing/SpeedRecordDao.xml  # 仅自定义「排名」聚合查询
        └── sql/typing_schema_mysql.sql     # 建表脚本（MySQL）
```

## 接入清单（务必逐项核对）

1. **父工程**：把 `pom.xml` 的 `<parent>` 改为你的 JeeSite Cloud 聚合父工程（含依赖管理、版本）。
2. **依赖**：确认能解析 `com.jeesite:jeesite-web`（提供 `DataEntity`/`CrudService`/`CrudDao`/`BaseController`/`UserUtils` 等）。
3. **包扫描**：JeeSite 通常按 `com.jeesite.modules.*` 自动扫描，模块放对包即可。
4. **Mapper 扫描**：`mappings/**/*.xml`，与 JeeSite 默认一致；如有自定义路径请对齐。
5. **建表**：执行 `src/main/resources/sql/typing_schema_mysql.sql`。其它数据库请按方言改写 DDL。
6. **菜单与权限**：建议用 JeeSite **代码生成器**对四张表生成菜单/权限更省心；权限标识：
   - `typing:article:view` / `typing:article:edit`
   - `typing:speedRecord:view`
   - `typing:competition:view` / `typing:competition:edit`
7. **安全与跨域**（玩家端 API）：
   - `/api/typing/**` 需在 **Shiro/安全过滤链放行**（允许匿名或自定义 token 鉴权）。
   - React 与服务**跨域**：已在 `TypingApiController` 上加 `@CrossOrigin(origins = "*")` 方便联调，**生产请收紧来源**；或在网关统一处理 CORS。
8. **网关路由**：在你的 Gateway 增加将 `/api/typing/**`、`${adminPath}/typing/**` 路由到本服务（`spring.application.name = jeesite-typing-service`）。
9. **管理端页面**：`web` 里管理端 Controller 的 `list/form` 返回的是 Beetl 模板名（单体后台用）。**jeesite-vue 后台**只调用 `listData/save/delete/get` 等 JSON 接口；对应的 Vue 页面属于前端工程（见下「后续阶段」）。
10. **javax / jakarta 兼容**：代码默认用 `javax.validation.*`、`javax.servlet.*`（Spring Boot 2 / JeeSite 传统栈）。若你的 JeeSite 5.8.1 跑在 **Spring Boot 3 / Jakarta** 上，请把这些 import 的 `javax.` 整体替换为 `jakarta.`（涉及 `entity/Article.java`、`entity/Competition.java` 的 validation，及 `web/*Controller.java` 的 servlet 与 validation）。

## REST API（玩家端，前缀 `/api/typing`）

| 方法 | 路径                                             | 说明                                                                                                                  |
| ---- | ------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------- |
| GET  | `/article/list?language=zh\|en`                  | 文章列表（不含正文）                                                                                                  |
| GET  | `/article/get?id=`                               | 文章详情（含正文）                                                                                                    |
| POST | `/speed/save`                                    | 上报速度记录（JSON：mode, refId, refName, cpm, wpm, accuracy, duration, charCount, wrongCount, userCode?, userName?） |
| GET  | `/speed/ranking?mode=&top=50`                    | 速度排名（按用户最好成绩）                                                                                            |
| GET  | `/competition/list?compStatus=`                  | 比赛列表                                                                                                              |
| GET  | `/competition/get?id=`                           | 比赛详情（含指定文章）                                                                                                |
| POST | `/competition/submit`                            | 提交比赛成绩（JSON：competitionId, cpm, accuracy, duration, charCount, userCode?, userName?）                         |
| GET  | `/competition/leaderboard?competitionId=&top=50` | 比赛排行榜（按用户去重并赋名次）                                                                                      |

- 未传 `userCode/userName` 且玩家已登录 JeeSite 时，后端用 `UserUtils.getUser()` 自动补全；匿名则记为「匿名用户」。
- `mode` 取值约定：`word`(单词)、`article`(文章)、`pinyin`、`wubi`、`en`。

## 速度排名口径

`/speed/ranking` 按 `user_code` 聚合，取每位用户的**最高 cpm**、平均正确率与记录条数，按最高 cpm 降序。可用 `mode` 过滤不同练习类型的榜单。

## 后续阶段（前端）

- **Phase 2 React 玩家端对接**（在 qwerty-learner 前端）：完赛上报速度、排行榜可视化页、比赛页、文章模式从后端加载文章。
- **Phase 3 jeesite-vue 管理后台**：文章管理增删改查页、速度排名 ECharts 可视化、比赛管理与排行榜页。

> 说明：本仓库（qwerty-learner）是 React 工程，Java 模块放在 `jeesite-typing-service/` 仅为交付与版本管理，最终应迁入你的 JeeSite 微服务工作区。
