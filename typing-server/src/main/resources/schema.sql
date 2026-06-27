-- H2（MODE=MySQL）建表脚本，应用启动时自动执行
DROP TABLE IF EXISTS ts_article;
CREATE TABLE ts_article (
  id          VARCHAR(64)  NOT NULL PRIMARY KEY,
  title       VARCHAR(200) NOT NULL,
  language    VARCHAR(20)  NOT NULL DEFAULT 'zh',
  category    VARCHAR(50),
  content     CLOB         NOT NULL,
  word_count  INT          DEFAULT 0,
  difficulty  VARCHAR(2)   DEFAULT '1',
  sort        INT          DEFAULT 30,
  create_date TIMESTAMP,
  update_date TIMESTAMP,
  remarks     VARCHAR(500),
  status      VARCHAR(2)   DEFAULT '0'
);

DROP TABLE IF EXISTS ts_speed_record;
CREATE TABLE ts_speed_record (
  id          VARCHAR(64) NOT NULL PRIMARY KEY,
  user_code   VARCHAR(64),
  user_name   VARCHAR(100),
  mode        VARCHAR(20) NOT NULL,
  ref_id      VARCHAR(64),
  ref_name    VARCHAR(200),
  cpm         INT DEFAULT 0,
  wpm         INT DEFAULT 0,
  accuracy    INT DEFAULT 0,
  duration    INT DEFAULT 0,
  char_count  INT DEFAULT 0,
  wrong_count INT DEFAULT 0,
  create_date TIMESTAMP,
  status      VARCHAR(2) DEFAULT '0'
);

DROP TABLE IF EXISTS ts_competition;
CREATE TABLE ts_competition (
  id          VARCHAR(64)  NOT NULL PRIMARY KEY,
  title       VARCHAR(200) NOT NULL,
  article_id  VARCHAR(64),
  description  VARCHAR(1000),
  start_time  TIMESTAMP,
  end_time    TIMESTAMP,
  comp_status VARCHAR(2) DEFAULT '0',
  create_date TIMESTAMP,
  update_date TIMESTAMP,
  status      VARCHAR(2) DEFAULT '0'
);

DROP TABLE IF EXISTS ts_competition_result;
CREATE TABLE ts_competition_result (
  id             VARCHAR(64) NOT NULL PRIMARY KEY,
  competition_id VARCHAR(64) NOT NULL,
  user_code      VARCHAR(64),
  user_name      VARCHAR(100),
  cpm            INT DEFAULT 0,
  accuracy       INT DEFAULT 0,
  duration       INT DEFAULT 0,
  char_count     INT DEFAULT 0,
  submit_time    TIMESTAMP,
  status         VARCHAR(2) DEFAULT '0'
);
