-- =============================================================
-- 打字练习模块（typing）建表脚本  MySQL
-- 适配 JeeSite 5.8.1，字段沿用 DataEntity 标准列
-- 标准列：create_by/create_date/update_by/update_date/remarks/status
-- status: '0' 正常  '1' 删除  '2' 停用/审核中
-- =============================================================

-- ----------------------------
-- 打字文章
-- ----------------------------
DROP TABLE IF EXISTS ts_article;
CREATE TABLE ts_article (
  id          VARCHAR(64)  NOT NULL COMMENT '主键',
  title       VARCHAR(200) NOT NULL COMMENT '标题',
  language    VARCHAR(20)  NOT NULL DEFAULT 'zh' COMMENT '语言：zh 中文 / en 英文',
  category    VARCHAR(50)           COMMENT '分类',
  content     LONGTEXT     NOT NULL COMMENT '正文',
  word_count  INT          DEFAULT 0 COMMENT '字数',
  difficulty  CHAR(1)      DEFAULT '1' COMMENT '难度：1 易 2 中 3 难',
  sort        INT          DEFAULT 30 COMMENT '排序号',
  create_by   VARCHAR(64)           COMMENT '创建者',
  create_date DATETIME              COMMENT '创建时间',
  update_by   VARCHAR(64)           COMMENT '更新者',
  update_date DATETIME              COMMENT '更新时间',
  remarks     VARCHAR(500)          COMMENT '备注信息',
  status      CHAR(1)      DEFAULT '0' COMMENT '状态：0正常 1删除 2停用',
  PRIMARY KEY (id),
  KEY idx_ts_article_language (language),
  KEY idx_ts_article_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打字文章';

-- ----------------------------
-- 打字速度记录
-- ----------------------------
DROP TABLE IF EXISTS ts_speed_record;
CREATE TABLE ts_speed_record (
  id          VARCHAR(64)  NOT NULL COMMENT '主键',
  user_code   VARCHAR(64)           COMMENT '用户编码',
  user_name   VARCHAR(100)          COMMENT '用户昵称',
  mode        VARCHAR(20)  NOT NULL COMMENT '练习模式：word/article/pinyin/wubi/en',
  ref_id      VARCHAR(64)           COMMENT '关联ID：文章ID或词典ID',
  ref_name    VARCHAR(200)          COMMENT '关联名称：文章或词典名',
  cpm         INT          DEFAULT 0 COMMENT '速度：字符每分钟',
  wpm         INT          DEFAULT 0 COMMENT '速度：单词每分钟',
  accuracy    INT          DEFAULT 0 COMMENT '正确率（0-100）',
  duration    INT          DEFAULT 0 COMMENT '用时（秒）',
  char_count  INT          DEFAULT 0 COMMENT '输入字符数',
  wrong_count INT          DEFAULT 0 COMMENT '错误次数',
  create_by   VARCHAR(64)           COMMENT '创建者',
  create_date DATETIME              COMMENT '创建时间（即记录时间）',
  update_by   VARCHAR(64)           COMMENT '更新者',
  update_date DATETIME              COMMENT '更新时间',
  remarks     VARCHAR(500)          COMMENT '备注信息',
  status      CHAR(1)      DEFAULT '0' COMMENT '状态：0正常 1删除',
  PRIMARY KEY (id),
  KEY idx_ts_speed_user (user_code),
  KEY idx_ts_speed_mode_cpm (mode, cpm),
  KEY idx_ts_speed_date (create_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打字速度记录';

-- ----------------------------
-- 打字比赛
-- ----------------------------
DROP TABLE IF EXISTS ts_competition;
CREATE TABLE ts_competition (
  id          VARCHAR(64)  NOT NULL COMMENT '主键',
  title       VARCHAR(200) NOT NULL COMMENT '比赛标题',
  article_id  VARCHAR(64)           COMMENT '指定文章ID',
  description VARCHAR(1000)         COMMENT '比赛说明',
  start_time  DATETIME              COMMENT '开始时间',
  end_time    DATETIME              COMMENT '结束时间',
  comp_status CHAR(1)      DEFAULT '0' COMMENT '比赛状态：0未开始 1进行中 2已结束',
  create_by   VARCHAR(64)           COMMENT '创建者',
  create_date DATETIME              COMMENT '创建时间',
  update_by   VARCHAR(64)           COMMENT '更新者',
  update_date DATETIME              COMMENT '更新时间',
  remarks     VARCHAR(500)          COMMENT '备注信息',
  status      CHAR(1)      DEFAULT '0' COMMENT '状态：0正常 1删除 2停用',
  PRIMARY KEY (id),
  KEY idx_ts_comp_status (comp_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打字比赛';

-- ----------------------------
-- 比赛成绩
-- ----------------------------
DROP TABLE IF EXISTS ts_competition_result;
CREATE TABLE ts_competition_result (
  id             VARCHAR(64) NOT NULL COMMENT '主键',
  competition_id VARCHAR(64) NOT NULL COMMENT '比赛ID',
  user_code      VARCHAR(64)          COMMENT '用户编码',
  user_name      VARCHAR(100)         COMMENT '用户昵称',
  cpm            INT         DEFAULT 0 COMMENT '速度：字符每分钟',
  accuracy       INT         DEFAULT 0 COMMENT '正确率（0-100）',
  duration       INT         DEFAULT 0 COMMENT '用时（秒）',
  char_count     INT         DEFAULT 0 COMMENT '输入字符数',
  submit_time    DATETIME             COMMENT '提交时间',
  create_by      VARCHAR(64)          COMMENT '创建者',
  create_date    DATETIME             COMMENT '创建时间',
  update_by      VARCHAR(64)          COMMENT '更新者',
  update_date    DATETIME             COMMENT '更新时间',
  remarks        VARCHAR(500)         COMMENT '备注信息',
  status         CHAR(1)     DEFAULT '0' COMMENT '状态：0正常 1删除',
  PRIMARY KEY (id),
  KEY idx_ts_cr_comp (competition_id, cpm),
  KEY idx_ts_cr_user (user_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打字比赛成绩';

-- =============================================================
-- 菜单与权限（参考示例，建议用 JeeSite 代码生成器自动生成更准确）
-- 仅示意权限标识，实际 menu_sort / parent_code 请按你的系统调整
-- =============================================================
-- 权限标识约定：
--   typing:article:view / edit        文章管理
--   typing:speedRecord:view           速度记录与排名
--   typing:competition:view / edit    比赛管理
