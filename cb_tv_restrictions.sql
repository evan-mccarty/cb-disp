-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: cb_tv
-- ------------------------------------------------------
-- Server version	5.7.18-log

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
-- Table structure for table `restrictions`
--

DROP TABLE IF EXISTS `restrictions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `restrictions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `restriction_type_id` int(11) DEFAULT NULL,
  `button_text` varchar(100) DEFAULT NULL,
  `display_text` varchar(200) DEFAULT NULL,
  `class` varchar(50) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `restriction_type_id` (`restriction_type_id`),
  CONSTRAINT `restrictions_ibfk_1` FOREIGN KEY (`restriction_type_id`) REFERENCES `restriction_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restrictions`
--

LOCK TABLES `restrictions` WRITE;
/*!40000 ALTER TABLE `restrictions` DISABLE KEYS */;
INSERT INTO `restrictions` VALUES (1,1,'Keelboats Reef','All keelboats must reef.','STANDARD',1),(2,1,'Lagoon Restriction','Kayaks and stand up paddleboards must stay within the lagoon system.','STANDARD',2),(3,1,'Two to a Jib','Two sailors minimum to a mercury with a jib.','STANDARD',3),(4,1,'Two to a Main','Minimum two sailors to any mercury.','STANDARD',4),(5,1,'Yellow Keel','Yellow rated sailors must use keel mercuries.','STANDARD',5),(6,1,'Yellow Crew','Yellow rated sailors may crew only.','STANDARD',6),(7,1,'Red Crew','Only red rated sailors on any boat. All sailors must be red rated!','STANDARD',7),(8,1,'Red Keel','Red rated sailors MUST take keel mercuries.','STANDARD',8),(9,2,'Weather Warning','Chance of inclement weather. Stay alert and check the American flag frequently!','STANDARD',1),(10,2,'1/2 River','All boats are restricted to half river!','STANDARD',2),(11,2,'1/4 River','All boats are restricted to 1/4 River','STANDARD',3),(12,2,'3/4 River','All boats are restricted to 3/4 river','STANDARD',4),(13,2,'Wetsuits','Wetsuits are required on all H.P. boats and windsurfs.','STANDARD',5),(14,2,'Sunscreen','Remember to use sunscreen and reapply as necessary!','STANDARD',6),(15,2,'Drink Water','Remember to stay hydrated!','STANDARD',7),(16,2,'Algae Bloom','Due to an algae bloom in the river CBI recommends you rinse yourself after any contact with the water.','STANDARD',8),(17,3,'Use Blue Sails','Please use blue Polar sails to support CBI!','STANDARD',1),(18,3,'Use Pink Sails','Please use pink EF sails to support CBI!','STANDARD',2),(19,3,'Barges','Please keep clear of the barges at least 100ft.','STANDARD',3),(20,3,'4th of July','The lagoons are closed to paddling. All boats keep off fireworks barge at least 200ft.\r\nDO NOT ENTER THE RESTRICTED AREA.','STANDARD',4),(21,3,'Head of the Charles','Half River. Stay away from any event boats and crew shells.','STANDARD',5),(22,4,'Green Keel','Green rated sailors must use keel mercuries','STANDARD',1),(23,4,'Green Crew','Green rated sailors may crew only.','STANDARD',2),(24,5,'Custom','We will be closing sailing at 5:30 for our dock party! Buy your tickets here!','CUSTOM',1);
/*!40000 ALTER TABLE `restrictions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-05 21:29:56
