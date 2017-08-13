-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: cb_tv
-- ------------------------------------------------------
-- Server version	5.7.19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `program_schedules`
--

DROP TABLE IF EXISTS `program_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_schedules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jp_start` time DEFAULT NULL,
  `jp_end` time DEFAULT NULL,
  `jp_enabled` tinyint(1) DEFAULT NULL,
  `ap_start` time DEFAULT NULL,
  `ap_end` time DEFAULT NULL,
  `ap_enabled` tinyint(1) DEFAULT NULL,
  `ap_end_sunset` tinyint(1) DEFAULT NULL,
  `program_color` varchar(10) DEFAULT NULL,
  `program_text` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program_schedules`
--

LOCK TABLES `program_schedules` WRITE;
/*!40000 ALTER TABLE `program_schedules` DISABLE KEYS */;
INSERT INTO `program_schedules` VALUES (18,'13:00:00','01:00:00',0,'01:00:00','01:00:00',1,0,'#5532cd','Bumbledeeboop'),(19,'01:00:00','01:00:00',0,'01:00:00','01:00:00',0,0,'#00ff40','Derp'),(20,'01:00:00','01:00:00',1,'01:00:00','04:00:00',0,0,'#800040','created group'),(21,'01:00:00','01:00:00',0,'01:00:00','01:00:00',0,0,'#8000ff','created group 2'),(22,'01:00:00','01:00:00',0,'01:00:00','01:00:00',0,0,'#8000ff','created group 2'),(24,'01:00:00','01:00:00',0,'01:00:00','01:00:00',0,0,'#0000ff','Derp'),(25,'01:00:00','01:00:00',1,'01:00:00','04:00:00',0,0,'#ff0080','charming'),(27,'01:00:00','01:00:00',0,'01:00:00','01:00:00',0,0,'#00ff40','Derp444'),(28,'01:00:00','01:00:00',1,'01:00:00','04:00:00',0,0,'#00ff40','group description'),(30,'01:00:00','01:00:00',1,'01:00:00','04:00:00',0,0,'#00ffff','Weekday'),(31,'01:00:00','01:00:00',1,'01:00:00','04:00:00',0,0,'#800040','Weekend'),(32,'01:00:00','01:00:00',1,'01:00:00','04:00:00',0,0,'#00ffff','Weekday');
/*!40000 ALTER TABLE `program_schedules` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-13 15:39:53
