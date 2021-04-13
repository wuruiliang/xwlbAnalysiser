CREATE TABLE `xwlb_word`
(
    `ID`           BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `WORD`         VARCHAR(128) NOT NULL,
    `TEXT_IDS`     TEXT,
    `TIME_CREATED` BIGINT(20) NOT NULL,
    `TIME_UPDATED` BIGINT(20) NOT NULL,
    KEY `index__time_created` (`TIME_CREATED`),
    KEY `index__time_updated` (`TIME_UPDATED`),
    UNIQUE KEY `index__word` (`WORD`)
);