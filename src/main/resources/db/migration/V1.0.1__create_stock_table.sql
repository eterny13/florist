CREATE TABLE IF NOT EXISTS `stock` (
    `id` int not null auto_increment,
    `flower_code` int not null,
    `flower_name` varchar(20) not null,
    `quantity` int not null,
    `arrival_date` date not null,
    PRIMARY KEY (`id`)
);
