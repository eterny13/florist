CREATE DATABASE if not exists `florist`;

USE `florist`;

CREATE TABLE IF NOT EXISTS `customer` (
    `id` varchar(10) not null,
    `name` varchar(30) not null,
    `email` varchar(50) not null,
    PRIMARY KEY (`id`)
);
