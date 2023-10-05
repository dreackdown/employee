CREATE TABLE IF NOT EXISTS `employee`
(
    `employee_id`  bigint(20)   NOT NULL AUTO_INCREMENT,
    `first_name` varchar(80)  NOT NULL,
    `last_name`  varchar(80)  NOT NULL,
    `role`    varchar(50) NOT NULL,
    PRIMARY KEY (`employee_id`)
);