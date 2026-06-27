# typing-vue（打字练习 Vue3 前端）

将打字练习前端转为 **Vue 3** 实现，结构贴近 jeesite-vue（Vite + Vue3 + TS + Router + 分模块 api/views）。
含玩家端（打字练习 / 比赛）与管理端（文章管理 / 排行可视化），对接 `typing-server` 后端。

## 功能

- **打字练习**：从后端加载文章，整段连续打字，逐字高亮；英文逐键、中文走输入法（compositionend），
  标点空格自动跳过；实时统计（字符/分、正确率、时间、进度）；完赛自动上报成绩。
- **速度排行**：调 `/api/typing/speed/ranking`，**ECharts 柱状图**可视化 + 榜单表格，按模式筛选。
- **打字比赛**：比赛列表 + 排行榜（名次/速度/正确率/用时）。
- **文章管理**（后台）：文章增删改查表单与列表，调 `/api/admin/article/*`。

## 启动

```bash
cd typing-vue
npm install
# 配置后端地址（默认 http://localhost:8090）
echo "VITE_API_BASE=http://localhost:8090" > .env.local
npm run dev      # 开发：http://localhost:5180
npm run build    # 生产构建到 dist/
```

需先启动 `typing-server`（见其 README）。

## 与 jeesite-vue 的关系

本工程为 jeesite-vue 风格的**独立可运行实现**：路由 `src/router`、接口 `src/api/typing.ts`、页面 `src/views/*`。
迁入真实 jeesite-vue 时，可把这些 `views` 作为业务页面接入其菜单与布局，`api` 改为其 `defHttp` 封装，
表格/表单可替换为 Ant Design Vue 组件；接口契约与字段保持一致即可。

> 说明：为保证可独立构建，未引入 Ant Design Vue / Vben 等重型依赖，改用轻量手写样式；
> 如需完全对齐 jeesite-vue 视觉，可在迁入后替换为 AntD 组件。
