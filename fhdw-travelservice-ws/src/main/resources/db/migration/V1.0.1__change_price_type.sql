/* 12:36:08  root@localhost */
ALTER TABLE `vacations` CHANGE `price` `price` DOUBLE  NULL  DEFAULT NULL;
/* 12:37:29  root@localhost */
RENAME TABLE `vacations` TO `vacation`;
/* 12:37:56  root@localhost */
RENAME TABLE `ratings` TO `rating`;
/* 12:38:13  root@localhost */
RENAME TABLE `bookings` TO `booking`;