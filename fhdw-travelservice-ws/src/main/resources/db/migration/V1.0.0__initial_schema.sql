# ************************************************************
# Sequel Pro SQL dump
# Version 3408
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.11)
# Datenbank: urlaubr
# Erstellungsdauer: 2013-07-30 20:04:50 +0000
# ************************************************************


# Export von Tabelle bookings
# ------------------------------------------------------------

DROP TABLE IF EXISTS `bookings`;

CREATE TABLE `bookings` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fk_vacation` int(11) DEFAULT NULL,
  `fk_customer` int(11) DEFAULT NULL,
  `creationdate` datetime DEFAULT NULL,
  `airport` varchar(255) DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `returndate` datetime DEFAULT NULL,
  `persons` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle customer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `creationdate` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle ratings
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ratings`;

CREATE TABLE `ratings` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fk_customer` int(11) DEFAULT NULL,
  `fk_vacation` int(11) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `comment` longtext,
  `creationdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle traveler
# ------------------------------------------------------------

DROP TABLE IF EXISTS `traveler`;

CREATE TABLE `traveler` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `fk_booking` int(11) DEFAULT NULL,
  `passport` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle vacations
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vacations`;

CREATE TABLE `vacations` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `description` longtext,
  `creationdate` datetime DEFAULT NULL,
  `availablefrom` date DEFAULT NULL,
  `availableto` date DEFAULT NULL,
  `image` blob,
  `hotelstars` int(11) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `catering` int(11) DEFAULT NULL,
  `airport` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;