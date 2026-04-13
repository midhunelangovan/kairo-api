CREATE TABLE `task`(
    `id` BIGINT AUTO_INCREMENT,
    `title` TEXT NOT NULL,
    `description`TEXT,
    `status` VARCHAR(20) NOT NULL,
    `priority` VARCHAR(20) NOT NULL,
    `due_date` TIMESTAMP,
    `created_date` TIMESTAMP NOT NULL,
    `modified_date` TIMESTAMP,
    `created_by` VARCHAR(100),
    `modified_by` VARCHAR(100),
    `deleted` BIT NOT NULL,
    PRIMARY KEY (`id`)
)