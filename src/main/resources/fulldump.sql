CREATE DATABASE  IF NOT EXISTS `rlcs` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `rlcs`;
-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: rlcs
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `application_user`
--

DROP TABLE IF EXISTS `application_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_user`
--

LOCK TABLES `application_user` WRITE;
/*!40000 ALTER TABLE `application_user` DISABLE KEYS */;
INSERT INTO `application_user` VALUES (1,'user','$2a$10$.tI3oz9Oo5LqjQUBRvDqE.A3jKIY8aAOHVDo0b2tBEC133lnRQZCK');
/*!40000 ALTER TABLE `application_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game` (
  `id` int NOT NULL AUTO_INCREMENT,
  `series_id` int NOT NULL,
  `winner` int NOT NULL,
  `number` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `winner` (`winner`),
  KEY `fk_game_series1_idx` (`series_id`),
  CONSTRAINT `fk_game_series1` FOREIGN KEY (`series_id`) REFERENCES `series` (`id`),
  CONSTRAINT `game_ibfk_3` FOREIGN KEY (`winner`) REFERENCES `team` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (1,1,1,1),(2,1,1,2),(3,1,2,3),(4,1,1,4),(5,1,2,5),(6,1,2,6),(7,1,1,7),(8,2,4,1),(9,2,3,2),(10,2,4,3),(11,2,4,4),(12,2,4,5),(13,3,6,1),(14,3,8,2),(15,3,8,3),(16,3,6,4),(17,3,8,5),(18,3,8,6);
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `gamertag` varchar(45) NOT NULL,
  `rating` int NOT NULL,
  `team_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`),
  CONSTRAINT `player_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (1,'justin','morales','jstn',6,1),(2,'garret','gordon','gerretg',2,1),(3,'mariano','arruda','squishymuffinz',4,1),(4,'courant','alexandre','kaydop',1,2),(5,'victor','locquet','fairy peak',5,2),(6,'yanis','champenois','alpha54',14,2),(7,'jacob','knapmann','jknaps',8,3),(8,'reed','wilen','chicago',10,3),(9,'massimo','franceschi','atomic',27,3),(10,'jos','van meurs','violentpanda',7,4),(11,'joris','robben','joreuz',65,4),(12,'jack','benton','apparently_jack',81,4),(13,'marc','domingo','marc_by_8',17,5),(14,'evan','rogez','monkeymoon',16,5),(15,'alex','paoli','extra',15,5),(16,'kyle','storer','torment',12,6),(17,'mariano','arruda','squishymuffinz',4,6),(18,'jesus','parra','gimmick',11,6),(19,'tschaka lateef','taylor jr','arsenal',21,7),(20,'slater','thomas','retals',23,7),(21,'daniel','piecenski','daniel',0,7),(22,'pierre','silfver','turbopulsa',3,8),(23,'andres','jordan','dreaz',78,8),(24,'nick','costello','mist',20,8),(25,'alexandre reis','pedrogam','taroco',56,9),(26,'jirai','papazian','Gyro',0,9),(27,'christopher','campbell','Aqua',0,9),(28,'francescco','cinquemanin','kuxir97',29,10),(29,'david','lawrie','miztik',45,10),(30,'jack','packwood-clarke','speed',46,10);
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `series`
--

DROP TABLE IF EXISTS `series`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `series` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tournament_id` int NOT NULL,
  `winner` int NOT NULL,
  `team1_id` int NOT NULL,
  `team2_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tournament_id` (`tournament_id`),
  KEY `fk_series_team1_idx` (`winner`),
  KEY `fk_series_team2_idx` (`team1_id`),
  KEY `fk_series_team3_idx` (`team2_id`),
  CONSTRAINT `fk_series_team1` FOREIGN KEY (`winner`) REFERENCES `team` (`id`),
  CONSTRAINT `fk_series_team2` FOREIGN KEY (`team1_id`) REFERENCES `team` (`id`),
  CONSTRAINT `fk_series_team3` FOREIGN KEY (`team2_id`) REFERENCES `team` (`id`),
  CONSTRAINT `series_ibfk_1` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `series`
--

LOCK TABLES `series` WRITE;
/*!40000 ALTER TABLE `series` DISABLE KEYS */;
INSERT INTO `series` VALUES (1,1,1,1,2),(2,1,4,3,4),(3,1,8,6,8);
/*!40000 ALTER TABLE `series` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `rating` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (1,'nrg',1),(2,'vitality',2),(3,'g2',3),(4,'dignitas',4),(5,'bds',5),(6,'cloud9',6),(7,'ssg',7),(8,'envy',8),(9,'rogue',9),(10,'flipsid3',10);
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tournament`
--

DROP TABLE IF EXISTS `tournament`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tournament` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `date` date NOT NULL,
  `location` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tournament`
--

LOCK TABLES `tournament` WRITE;
/*!40000 ALTER TABLE `tournament` DISABLE KEYS */;
INSERT INTO `tournament` VALUES (1,'fall major','2021-10-25','Zurich');
/*!40000 ALTER TABLE `tournament` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-01-31  8:53:58
