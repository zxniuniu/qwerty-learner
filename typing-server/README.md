# typing-server（打字练习独立后端 · Spring Boot 2）

一个**可直接启动**的 Spring Boot 2.7 服务，提供文章管理、速度记录、速度排名、打字比赛与排行榜的 REST 接口。
刻意**解耦 JeeSite 专有基类**，以便先跑起来；后续可平滑迁回 `jeesite-typing-service`（JeeSite/微服务版）。

> ✅ 本服务已在本环境实测：`mvn package` 编译通过、`java -jar` 正常启动（约 3 秒，端口 8090），全部接口联调通过。

## 技术栈

- Spring Boot 2.7.18 + Spring MVC
- MyBatis（注解式 Mapper，贴近 JeeSite 持久层风格）
- **默认 H2 内存库**：零配置启动，启动时自动执行 `schema.sql` + `data.sql`
- MySQL：以 `mysql` profile 切换

## 快速启动

```bash
cd typing-server
mvn spring-boot:run
# 或
mvn -DskipTests package && java -jar target/typing-server-1.0.0.jar
```

启动后：
- 服务： http://localhost:8090
- H2 控制台： http://localhost:8090/h2-console （JDBC URL: `jdbc:h2:mem:typing`，用户 `sa`，空密码）

切换 MySQL：

```bash
java -jar target/typing-server-1.0.0.jar --spring.profiles.active=mysql
# 先在 MySQL 建库 typing，并执行 src/main/resources/schema.sql（H2 语法与 MySQL 基本兼容，
# 如需可改用 jeesite-typing-service/src/main/resources/sql/typing_schema_mysql.sql）
```

## REST 接口

玩家端（`/api/typing`）：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/article/list?language=zh\|en` | 文章列表（不含正文） |
| GET | `/article/get?id=` | 文章详情（含正文） |
| POST | `/speed/save` | 上报速度记录（JSON） |
| GET | `/speed/ranking?mode=&top=50` | 速度排名（按用户最好成绩） |
| GET | `/competition/list?compStatus=` | 比赛列表 |
| GET | `/competition/get?id=` | 比赛详情 |
| POST | `/competition/submit` | 提交比赛成绩 |
| GET | `/competition/leaderboard?competitionId=&top=50` | 比赛排行榜 |

管理端（`/api/admin`，供 Vue 后台）：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/article/list` | 文章列表（含正文） |
| POST | `/article/save` | 新增/编辑文章（无 id 为新增） |
| POST | `/article/delete?id=` | 删除文章 |
| GET | `/speed/list?mode=` | 速度记录列表 |
| POST | `/competition/save` | 新增/编辑比赛 |
| POST | `/competition/delete?id=` | 删除比赛 |

跨域：`WebConfig` 对 `/api/**` 放开 CORS，便于前端联调（生产请收紧来源）。

## 前端对接

- React（qwerty-learner）：设 `VITE_TYPING_API_BASE=http://localhost:8090`。
- Vue（typing-vue）：设 `VITE_API_BASE=http://localhost:8090`。

## 迁回 JeeSite / 微服务

本服务与 `jeesite-typing-service/`（JeeSite 5.8.1 规范脚手架）表结构、接口契约一致。迁移时：
将 `model` 换为继承 `DataEntity` 的 Entity、`mapper` 换为 `CrudDao`+`@MyBatisDao`、`service` 换为 `CrudService`、
controller 换为 JeeSite `BaseController` 风格并加权限注解；接口路径与 JSON 字段保持不变即可无缝替换。
