-- MySQL dump 10.13  Distrib 5.7.18, for Win64 (x86_64)
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
-- Current Database: `cb_tv`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `cb_tv` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `cb_tv`;

--
-- Table structure for table `class_instances`
--

DROP TABLE IF EXISTS `class_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `class_instances` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `instance_id` varchar(200) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `enrollees` int(11) DEFAULT NULL,
  `instructor_first` varchar(100) DEFAULT NULL,
  `visible` tinyint(1) NOT NULL DEFAULT '1',
  `cancelled` tinyint(1) NOT NULL DEFAULT '0',
  `class_type` varchar(30) DEFAULT NULL,
  `program_type` varchar(30) DEFAULT NULL,
  `instructor_last` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class_instances`
--

LOCK TABLES `class_instances` WRITE;
/*!40000 ALTER TABLE `class_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `class_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `human_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES (1,'permission.base','Base Permission'),(2,'derp','schmacked');
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_group_permissions`
--

DROP TABLE IF EXISTS `user_group_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_group_id` int(11) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_group_id` (`user_group_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `user_group_permissions_ibfk_1` FOREIGN KEY (`user_group_id`) REFERENCES `user_groups` (`id`),
  CONSTRAINT `user_group_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_group_permissions`
--

LOCK TABLES `user_group_permissions` WRITE;
/*!40000 ALTER TABLE `user_group_permissions` DISABLE KEYS */;
INSERT INTO `user_group_permissions` VALUES (1,1,1),(2,1,2);
/*!40000 ALTER TABLE `user_group_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_groups`
--

DROP TABLE IF EXISTS `user_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_groups`
--

LOCK TABLES `user_groups` WRITE;
/*!40000 ALTER TABLE `user_groups` DISABLE KEYS */;
INSERT INTO `user_groups` VALUES (1,'BASE');
/*!40000 ALTER TABLE `user_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_permissions`
--

DROP TABLE IF EXISTS `user_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `user_permissions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `user_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_permissions`
--

LOCK TABLES `user_permissions` WRITE;
/*!40000 ALTER TABLE `user_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `salt` varchar(200) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'alexjpbanks14','alexjpbanks14@gmail.com','testing','testing',1),(2,'blooper',NULL,'a9aa2c149d2a0c8729e5e38bd24347dc334f396365667fa680423ef6625eb9e5c7fbf10d80d23b0995f582e6d6f2427ddb82362a286ddce4980707aa0c500a51','959de99525cc44579630456713d1ca9894ed4aae67fe6999f864109cc9f42b58',NULL),(3,'meds',NULL,'630608dcddcf4e2fb0c31c475b4b4a36cd07818f8a82b5abf8ae63382398e4ccfbc7855ef16f0f855b2e84a1d464a15361fcb4e92d3899712648649fdaf520dc','c27d11dd25e0e7a2763106a4da3391a9be1f51b00f533d97f2da0fa754c805db',NULL),(4,'meds',NULL,'44e33ba0e31b6957800f3bfa13130f67372337ec39bc3701002bcc237c476008cb9307e48526304a0cda3bf8ac3d305816686c86ddf86ba30a0adbc0e7f77110','3090ceffd2778c527317db0a064e6e67bb7a5f6eabbebcbceeefabd16af7c4df',NULL),(5,'meds',NULL,'b1c314e60f8c8104f2798da893a06f869da86651014bb1f740450c9c4ab735b041c80efaee2eaff38db4e83140c2024ad2b9ea0e0f1f41fae424a2722aa6c879','32645aa0a2bf9e684071ca4fe6c5919484f9d86177d0ce05da29a9bc2abec37e',NULL),(6,'meds',NULL,'1657473f0dc5b771815055ef39ed919aa95aeff94d6867051f7e39a32e56fcd6976e8b3d128567cdca0fdc9495ee1eda23c8aa5813942af672a86f43c090e79d','9dcf61c45ac51ede44414555da9e0ae32e884972048a8e9a84d39e9abea057bc',NULL),(7,'meds',NULL,'3812e6b5a31e0fa11849dfa64e73ce3f2b5e2c12e538c675b3f5aeebc79631cfa8273a2d071cbf8a2d465b127bdf38cb32f36932ad147452bba3ee0238a106ea','5f645212928404e3eb4d7763300194b12cd58a73be12f93e9326475be3d56c6e',NULL),(8,'meds',NULL,'86f4b4245110e9faa518c29e9bdb4e469345c4485a504fd018209d1f327da476b124267ad4fbc20c10b4c408f2041a67e6fbfd7ec9be97546db5e6a92cedeb53','43d7895b81a7cb7e2e10bd6c2d58adcc5c9e6ee6befeac03099295a11178b840',NULL),(9,'meds',NULL,'378d83abb27daa523931f3fc7f9e13770e50ab8a3be4fe41bc4dc167614de47da851922b05aa865876cfd72b7184139cba5a80fca2e0991b1117a2db92085eab','cda91ade99e7068dff32d41013e8dfefc2465661c144d4c6051c01055ec56c59',NULL),(10,'meds',NULL,'70d5ca1c76cda41ef40401d1aa69165fab5d355898530fddd5478b02f937e9f69902b61a024cffffeff06042139c16cb17a025362d0644999e0ad88e3431e09f','92c9593dfc3662e429a5ba07580f800e83b5e35b1af6d7b9da856106798290e3',NULL),(11,'meds',NULL,'c8392663e5ba536b76b3b118e1899af34ab513374bc1636311addd71f6c4b555602feb74fc88d61431f7b9a70679c58a0af1623eda3764f8532651a2c01bb486','24b3d91047f7a5f29cb95146daca764748a49972fd29701c5ccfd6db39a79202',NULL),(12,'meds',NULL,'4153c016ac045132b4f2379334820fa1ebc750d4df63ca2aa801405c56486c4444e688e84f58b576fb458358e55d3058d9631bb34d75b46f7385dbff24a09c63','7268cfafa7c2c9d5a5bccdd1e11189c53be7f2a2b0bfd72888a601241b412604',NULL),(13,'meds',NULL,'5845d833b2e4bb64e2d533fe3529cd859b31d407d874c435dc41a4afdd3bcff5c43b722a060fede4af3b57d4f8c66e424812ac595efff3bd5dfe94d03e6a3188','9cdc585e9448a606a7e122075725fc8fa5bb49b71ec31288667ea3e3e594b436',NULL),(14,'meds',NULL,'2908939088dc8b2d78d45708b3930e526cf3fa662763c91c187d143541a13511440ffe0e5caec79c685a1a93091e74b8d9febc8e0c32962664616eb9cac37a02','9c5422da1e68bea5ca5895f481830b3df6938fb666c4d80a307400d33a0d7746',NULL),(15,'meds',NULL,'b9f63c84947211e9206330ca08d947be70323e38f34d1baa74744a1659fc4cf9fd2eb0d40302ec9b6bc9a5a89c62325937ec029958dc2b564e14321a396cb83e','a0e4f48b1907039f08da3c90f2a0d4c0e43a2a8931bd8b0b1164fde3124c24d9',NULL),(16,'meds',NULL,'8218b5885e2de8cfee99c5853aa32653a886315f25d8c268dc75802819ac98cfc473801c596b6644cd0809025e07e875ba1d614d9ca9061a968d0fb55635fa0f','8086991d9895800588832d8a895b9e3f865f62d69fa50efd77637c0b1dc7596c',NULL),(17,'meds',NULL,'36faebb0cdc9b7485fc9754c2ca6bfa81b8c636d4c6d0c0afd80d43ffed56a2e01b9ffc393cee0a605bd9eb2214effe9d9cdcfa6b93fcf59a7db101d4d07dbc9','b7e2c95dcb24c2c0fcecea5b2f50c60dc47e382daf6d4992c4c8dc24dbad81a4',NULL),(18,'meds',NULL,'109b0c21f9c8e7ce32c38dea7f68c4252a336e1d6786d86b66bd8db0ae9f860c6a5eac6be3bc24c6b074bc8380fa731d016bf97b3f6b95d35ce8a25194254987','33ce0544a7f31643452824380bb6e84488e4c67a076e7db9e309a61beb499c67',NULL),(19,'meds',NULL,'8a2ec930cede8c69ec14f8fcfa78c20940ee3166825c58ed56f8e9167db2b5c3c9c46c74f91768d60509a31d0aaa1bcf98269d5c40e5ed1eae9cfc6db0884015','22a4ece987b9c3ceefc0ff63f96ff80bb2c30a0ecf5135ac4373db839f167604',NULL),(20,'meds',NULL,'5597e65a557ad96474bfdf293f7607c0f6963338e6d962b1ef9a7c28cc5dc2771525d62f87ad50ef0430d92233f89272764a087549b82fb005f5c10cb91cf264','77b7a744118565fbcf6317e210565ba569ed934ec0a191f54d9da700797392fe',NULL),(21,'meds',NULL,'790500d8463a5669389265fe345d33d2113e6284544a1baac3478b093e2477353c92924b44f15abc7665e03c66e81b0477c80b13414999957aecefcf27b978e4','406e4e6d152c93df76a1fe0be6f8eb58dc9321f15b49570696cb02287a9433cd',NULL),(22,'meds',NULL,'f733de89ef0dcabd4fe48eb6c9742748ec8e00dba986212510419f29e02e8f1a0df1b419adb07ef1b558ccc17e94843c59a259a324dbfabbd71dc0a0dc69c175','047e22f20a293672758c5ebfe104522816a12757b50e3538e7a4b94f55c0ded3',NULL),(23,'meds',NULL,'4196e7dbe376d0f49720319c1c7f87b307fc3d89495503abdd7fb5ad4b8f5f7f6dbb2d9dd11cff0c84c44cfd64a013eba6e8b660a9580d346ff901a4877c959e','d0bb2df8be808cf8d57f9c5e3de7256fec1bd8398a7ab653d0b590b1ff84bf7a',NULL),(24,'meds',NULL,'37e2ab2a6b0d26eac159bd6c00edff80df428a582f56d6e6f7478d86b2ae3c8ff146113cfcea1b8369e93b80d9fb04ea09a103953e5edfcdc203718f9ee1abd4','357bbf24f5eb8613edb9507a42c784482664589e33010a2667d9482875d34f96',NULL),(25,'meds',NULL,'e047c7c8c090dda6a10e88287800b13fb711e45841ec2ea87cca003f5ff15b82ec57e8ba207af9a79b415554101322adb7f90c70b9038dbfc17b84404bc78b32','bcfdf519d22da6a147c3d2f1d3d9446b8dbb2cf1e93f09426b6d44ab3a5827dd',NULL),(26,'meds',NULL,'8e85ab1feea976b68c190f2101a108a3e67481086bcfe938fc1591fc7b618e29fdc6a5f0dfe15128e6d06db2f3f228f8e99ebb96805c9ed8f343ddcb139674b6','3e8bce0f468f549caeb93a4d73e51f38235205338593b3c321c6b223c206f4bf',NULL),(27,'meds',NULL,'8199218c9a5c3b03b204060edc268ed53b44d2b8f852ede8593c3d5aae4592e0cedde60a4a6116615d17022d3721219273ab919e1c8992d6c3b1e032fee2bb7b','3e9cba9ede6e51bade0020d88582c4fd6253bdcb713691f2d14f17bff86a2a62',NULL),(28,'meds',NULL,'76e768bee53c6bfeffe1d945974386c98ad004a14ad7ecd5994da7b49bc527feb04d5d0e5a36cba92f00e49aa1368a6085f5c2b1e77f4cced7f99d95e3857868','b217f03b00d872d70933f11446a9d274f86d4ca7625310bc1aa75c53c52ded5c',NULL),(29,'meds',NULL,'fe6af3f226c45fc360f64b0c1e7a01359e6a876b588cf67ce9940d8a76f89167470e279cf9bcf6a827cc1d07cdfcde484de3593a558fe6e75121e4424f15b663','0eec9826730cd06136ca6e221960428b58cea28c03139b4bbc5dab169ab15f98',NULL),(30,'meds',NULL,'fdff855ac7556b7e3b6e67e453d3984fbfe021635f64bbe79caf00be831aa3e8809b95e57d7649f235bbd94fa8aae4f3a91400208df6b26884c31b277aea3e4d','1edebbb0203743ba185d05e72341942597f93670206d1b9af06195e5077c82ff',NULL),(31,'meds',NULL,'fa1cb0ccdc9cefb60ab9dd383bd40a8a0bdc4879ce28e8bce95dcdd1886fffa042e6d7179cb96efcd1dc1e5cf644ffdc5f0572623a000be55667feea7d4e047e','764e8c8052aae8ec83043300d61a268176335711c2362f8aae6c65cab0fab52b',NULL),(32,'meds',NULL,'ab58040213cbb7e02aba1cebf5a41ddf90ef23a4c8f0985fd1e8886f56ab363df2d1d183f481a875fa791fb3381f0f9f388618db27f4529786135afbfe52a5df','ad194ff5f29ffb07f03240c24e0e599733be0c89b0642bf282737dc11977e49b',NULL),(33,'meds',NULL,'29d47d2e721e0a510ddc59798ba64c5479136e8ccdcd816952f8f08c3b182df17186f35c9e8c9817c2f09c205ba474ae12a7535ceee62efadd8723d268762657','96076fd21496c5ed591dae621086cea527590554ac63827c301eb9833bda857c',NULL),(34,'meds',NULL,'a34705a13a921201ed880b492b3ed0c07e9ecd818dec665ed455d0cf3385c8e8bec20e0a3156114c41645e9e37849ce9f1e924a07cd2df38ef6c629c954b64d0','836ce8ec93d46a99a85e5362cb3d9bcae4c1bc1eb8d6c68a9e83e1862b7d2852',NULL),(35,'meds',NULL,'4ca968954ddfb8a672215410cb7371630bc31fdd7208838532059a5270c07ed8cf1a4b6a41f1263b101600989f9e7ee16dc6efb1fcdd86606d04c76cf247fa62','fc2669b7aa02d2bcb70bb89930d9e57fa06accb7193bcb591afc3370a6c26be9',NULL),(36,'meds',NULL,'a594d6d825770965bc76f6fe987a8377f01179f715fc3cedbac3229f48e20007d61647a4a21a6b92e08b985349c7b315b1c2c18f84b69271a1a0f15449d5d2fc','2696e454832ff8f8850f56fc3e9f519a6fe059e4436c42667ca886ce6b366d0f',NULL),(37,'meds',NULL,'edcf4dbe0abe93f3f32d2ca8f0932d8542ad4ca160fe145a58ecaa379a6ea20dcb64bae46951543f0855f611c26a6d8be2d1bea438c94502af80f215d0afee14','f18e5076437f6e52d47a7a340828e78abad10427c3661ae35757bb0c717e3eba',NULL),(38,'meds',NULL,'644ad9a688f78c89ab3b8c703074eb2bfae575e5bdf49020fb38a75f7b3bdf10d10a5c4cd6ad5accb608ffda756c1eb37e7ff67138040e6de5742ef26d4fae0d','a7ad0c537e5a48bc0b16a164c61a35fb791c6930edb0f9c7c8c7e522bbbdd9a7',NULL),(39,'meds',NULL,'ed1542d13293531ad6cc4c8c480874fda9dcc634b5cf0ac25a6176454f11ee36dc01785cdec6122fd4420e69a478fd7512161491d9d76ae447311ec1ee793cfa','5479947301dcd1f1afa9c8500bd7f08999e777a743a81fbec53d21843a7d2582',NULL),(40,'meds',NULL,'afc21c6b14213d4eb7e973af70fe8f9a329aa97dd18f802e24b5e9ff77013df5a175c773a701e5a84a4bfddf66cca9077396e6662a8e32dc1a51382bc44d722b','0140342b78aa30323704af7debec32d565b38173b56cd61cd3a2ce570c5fc3ae',NULL),(41,'meds',NULL,'1e28941bcabb5fe263a7b34dbeae4e068f2c15496cfcd1aff9dafef92557b820243d4629471d340275776081a2b4a08f8d4ed157f6ea98063faba4144820fc33','7fd509f9477a91a0081d9d4bc18651e20b76dbf6bc886067d035b11804cc7b21',NULL),(42,'meds',NULL,'cff963a18e86e6492fe5d291eca06ea6455c79ae5266b135ab87f222148212ecaeda314d8636817ad353674b241547c08ee4d38ae86ed8416b8cc1fb350adc24','0aa3ee81aa49d78dd1dd0613eb0f027c2605fd30443fdc7f0b6847e682815703',NULL),(43,'meds',NULL,'736f4ac61d296c5885f2b4b8c12911ef33b2cc1d4478f55b3df9d8863bad4aef0b1a4b6b1a6ba5e66d528e1a8a260af2d4eb3e91d18db55629065866d02d09ab','124e35068984216f7f78cc79c8b213b8e4ec8f8df78a0551fd349b4b190ee3eb',NULL),(44,'meds',NULL,'99176a53d61f31eccb0b94e77f2a7170ed0bdf3af9d83ac27b829ffdfa56255b7a7cdfb9aa0d25a015a17438e8b463de26230135805127990cb03d7eea98a2e0','1238948cf951fff7fb911e143aa4728ad421242d9aea4fed63f304e989fb506d',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-07-29 22:47:38
