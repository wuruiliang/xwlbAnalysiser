CREATE TABLE `xwlb_text`
(
    `ID`           BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `TITLE`        VARCHAR(1024) NOT NULL,
    `DATE`         BIGINT(20) NOT NULL,
    `SUMMARY`      TEXT,
    `CONTENT`      TEXT,
    `URL`          VARCHAR(256),
    `TIME_CREATED` BIGINT(20) NOT NULL,
    `TIME_UPDATED` BIGINT(20) NOT NULL,
    KEY `index__time_created` (`TIME_CREATED`),
    KEY `index__time_updated` (`TIME_UPDATED`),
    KEY `index__date` (`DATE`)
);