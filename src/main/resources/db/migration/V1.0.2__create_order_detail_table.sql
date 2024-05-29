CREATE TABLE IF NOT EXISTS `order_detail` (
    `id` int not null auto_increment,
    `customer_id` varchar(10) not null,
    `delivery_date` date not null,
    `delivery_address` varchar(50) not null,
    `recipient_name` varchar(30) not null,
    `bouquet_code` int not null,
    `delivery_message` varchar(100) null,
    `recipient_phone_number` varchar(20) not null,
    PRIMARY KEY (`id`)
);
