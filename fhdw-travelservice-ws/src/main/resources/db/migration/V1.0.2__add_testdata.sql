# ************************************************************
# Sequel Pro SQL dump
# Version 3408
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.11)
# Datenbank: urlaubr.
# Erstellungsdauer: 2013-08-08 11:04:27 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Export von Tabelle booking
# ------------------------------------------------------------

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;

INSERT INTO `booking` (`id`, `fk_vacation`, `fk_customer`, `creationdate`, `airport`, `startdate`, `returndate`, `persons`, `state`)
VALUES
	(1,1,1,'2013-02-01 13:00:00','DUI','2013-08-09 00:00:00','2013-08-13 00:00:00',5,0),
	(2,1,3,'2013-02-01 13:01:00','BER','2013-08-20 00:00:00','2013-08-21 00:00:00',3,0),
	(3,4,1,'2013-08-09 00:00:00','DUI','2013-12-09 00:00:00','2013-12-16 00:00:00',3,0);

/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;


# Export von Tabelle customer
# ------------------------------------------------------------

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;

INSERT INTO `customer` (`id`, `firstname`, `lastname`, `username`, `email`, `creationdate`, `password`)
VALUES
	(1,'Patrick','Groß-Holtwick','patrickgh','patrickgh@web.de','2013-08-08 12:01:03','098f6bcd4621d373cade4e832627b4f6'),
	(2,'Hermann','Mels','hmels','hermann-mels@gmx.de','2013-08-08 12:44:21','098f6bcd4621d373cade4e832627b4f6'),
	(3,'Moritz','Barnick','mbarnick','moritzbarnick@gmx.de','2013-08-08 12:44:21','098f6bcd4621d373cade4e832627b4f6'),
	(4,'Priska','Andrea','pandrea','priskaandrea@fhdw.de','2013-08-08 12:44:21','098f6bcd4621d373cade4e832627b4f6');

/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;


# Export von Tabelle rating
# ------------------------------------------------------------

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;

INSERT INTO `rating` (`id`, `fk_customer`, `fk_vacation`, `rating`, `comment`, `creationdate`)
VALUES
	(1,1,1,5,'alles super','2013-08-09 00:00:00'),
	(2,2,1,4,'bisschen teuer','2013-08-09 00:00:00'),
	(3,3,2,3,'bis auf die kakerlaken im salat alles gut','2013-08-09 00:00:00');

/*!40000 ALTER TABLE `rating` ENABLE KEYS */;
UNLOCK TABLES;


# Export von Tabelle traveler
# ------------------------------------------------------------

LOCK TABLES `traveler` WRITE;
/*!40000 ALTER TABLE `traveler` DISABLE KEYS */;

INSERT INTO `traveler` (`id`, `firstname`, `lastname`, `birthdate`, `fk_booking`, `passport`)
VALUES
	(1,'patrick','gh','2013-08-09',1,'asdf'),
	(2,'mitfahrer','1','2013-08-09',1,'asdf'),
	(3,'mitfahrer','2','2013-08-09',1,'asdf'),
	(4,'mitfahrer','3','2013-08-09',1,'asdf'),
	(5,'mitfahrer','4','2013-08-09',1,'asdf'),
	(6,'moritz','barnick','2013-08-09',2,'qwert'),
	(7,'hermann','mels','2013-08-09',2,'qwert'),
	(8,'priska','andrea','2013-08-09',2,'qwert'),
	(9,'skifahrer','eins','2013-08-09',3,'blabla'),
	(10,'skifahrer','zwei','2013-08-09',3,'blabla'),
	(11,'skifahrer','drei','2013-08-09',3,'blabla');

/*!40000 ALTER TABLE `traveler` ENABLE KEYS */;
UNLOCK TABLES;


# Export von Tabelle vacation
# ------------------------------------------------------------

LOCK TABLES `vacation` WRITE;
/*!40000 ALTER TABLE `vacation` DISABLE KEYS */;

INSERT INTO `vacation` (`id`, `title`, `description`, `creationdate`, `availablefrom`, `availableto`, `image`, `hotelstars`, `duration`, `price`, `country`, `city`, `catering`, `airport`)
VALUES
	(1,'3-Tage Mallorca','Gepflegtes Sangriasaufen in niveauvoller Atmosphäre','2013-08-08 12:44:21','2013-05-01','2013-09-01',NULL,2,3,199.99,'ESP','Palma de Mallorca',0,'PMI'),
	(2,'2 Wochen Gran Canaria All-Inklusive','freies futtern!!!','2013-08-08 12:44:21','2013-05-01','2013-09-01',NULL,4,14,699.99,'ESP','Las Palmas',4,'LPA'),
	(3,'Wandern in Norwegen','rumlaufen in kalten bergen','2013-08-08 12:44:21','2013-02-01','2013-11-01',NULL,3,10,599,'NOR','Oslo',3,'OSL'),
	(4,'Skiurlaub in Sölden','inkl. Skipass und Leihausrüstung','2013-08-08 12:44:21','2013-12-01','2014-03-01',NULL,4,6,899.99,'AUT','Sölden',3,'INN');

/*!40000 ALTER TABLE `vacation` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
