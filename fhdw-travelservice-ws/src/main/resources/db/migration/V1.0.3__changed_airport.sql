/* 16:14:23  root@localhost */ ALTER TABLE `booking` DROP `airport`;

/* 16:14:59  root@localhost */ ALTER TABLE `vacation` ADD `homeairport` VARCHAR(255)  NULL  DEFAULT NULL  AFTER `airport`;
