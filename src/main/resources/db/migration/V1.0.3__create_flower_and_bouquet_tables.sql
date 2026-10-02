CREATE TABLE IF NOT EXISTS `flower`
(
    `code`
    int
    not
    null,
    `name`
    varchar
(
    30
) not null,
    `min_unit_quantity` int not null,
    `order_lead_time` int not null,
    `days_of_best_quality` int not null,
    PRIMARY KEY
(
    `code`
)
    );

CREATE TABLE IF NOT EXISTS `bouquet`
(
    `code`
    int
    not
    null,
    PRIMARY
    KEY
(
    `code`
)
    );

CREATE TABLE IF NOT EXISTS `bouquet_flower`
(
    `bouquet_code`
    int
    not
    null,
    `flower_code`
    int
    not
    null,
    `quantity`
    int
    not
    null,
    PRIMARY
    KEY
(
    `bouquet_code`,
    `flower_code`
),
    CONSTRAINT `fk_bouquet_flower_bouquet`
    FOREIGN KEY
(
    `bouquet_code`
) REFERENCES `bouquet`
(
    `code`
),
    CONSTRAINT `fk_bouquet_flower_flower`
    FOREIGN KEY
(
    `flower_code`
) REFERENCES `flower`
(
    `code`
)
    );

INSERT INTO `flower` (`code`, `name`, `min_unit_quantity`, `order_lead_time`, `days_of_best_quality`)
VALUES (1, 'Rose', 30, 2, 10),
       (2, 'Anabel', 10, 3, 8),
       (3, 'Kasumi', 10, 5, 12),
       (4, 'Fragrant Olive', 10, 5, 14),
       (5, 'Cosmos', 20, 3, 10),
       (6, 'Sumire', 10, 3, 10),
       (7, 'Cyclamen', 20, 3, 10),
       (8, 'BellFlower', 20, 5, 7),
       (9, 'Peony', 20, 5, 7),
       (10, 'Tulip', 30, 5, 7);

INSERT INTO `bouquet` (`code`)
VALUES (1),
       (2),
       (3),
       (4),
       (5);

INSERT INTO `bouquet_flower` (`bouquet_code`, `flower_code`, `quantity`)
VALUES (1, 1, 4),
       (1, 2, 3),
       (1, 3, 3),
       (1, 4, 3),
       (1, 5, 4),
       (2, 1, 4),
       (2, 4, 3),
       (2, 5, 3),
       (2, 6, 3),
       (2, 7, 4),
       (3, 3, 4),
       (3, 4, 4),
       (3, 5, 3),
       (3, 6, 3),
       (3, 7, 3),
       (4, 3, 4),
       (4, 4, 4),
       (4, 8, 3),
       (4, 9, 3),
       (4, 10, 3),
       (5, 1, 2),
       (5, 2, 2),
       (5, 3, 4),
       (5, 4, 4),
       (5, 8, 3),
       (5, 9, 2),
       (5, 10, 2);
