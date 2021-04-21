CREATE TABLE `job_config`
(
    `ID`             BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `NAME`           VARCHAR(128) NOT NULL COMMENT '任务名称',
    `CRON`           VARCHAR(20) NOT NULL COMMENT 'cron表达式',
    `CLASS_NAME`     VARCHAR(256) NOT NULL COMMENT 'job全路径名',
    `PARAM`          TEXT NOT NULL COMMENT '参数',
    `DESCRIPTION`    VARCHAR(256) NOT NULL COMMENT '描述',
    `STATUS`         CHAR(2) NOT NULL COMMENT '定时任务状态',
    `TIME_CREATED`   BIGINT(20) NOT NULL,
    `TIME_UPDATED`   BIGINT(20) NOT NULL,
    KEY `index__time_created` (`TIME_CREATED`),
    KEY `index__time_updated` (`TIME_UPDATED`),
    UNIQUE KEY `uindex__name` (`NAME`)
);
