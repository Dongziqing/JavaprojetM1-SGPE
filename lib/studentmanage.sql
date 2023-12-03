-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: studentmanage
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `formation`
--

DROP TABLE IF EXISTS `formation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `formation` (
  `formid` int NOT NULL,
  `formname` varchar(45) DEFAULT NULL,
  `formtype` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formation`
--

LOCK TABLES `formation` WRITE;
/*!40000 ALTER TABLE `formation` DISABLE KEYS */;
INSERT INTO `formation` VALUES (1,'ID','FI'),(2,'ID','En Alternance'),(3,'ID','FC'),(4,'IF','FI'),(5,'IF','En Alternace'),(6,'IF','FC'),(7,'SITN','FI'),(8,'SITN','En Alternance'),(9,'SITN','FC');
/*!40000 ALTER TABLE `formation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `projectid` int NOT NULL AUTO_INCREMENT,
  `subjectname` varchar(45) DEFAULT NULL,
  `topic` varchar(45) DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  PRIMARY KEY (`projectid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,'Java','java project','2023-12-05'),(2,'Sar','Sar project','2023-11-28'),(3,'Marketing','Marketing Project','2023-12-01'),(4,'ML','ML project','2023-11-25');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stu_grade`
--

DROP TABLE IF EXISTS `stu_grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stu_grade` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `formid` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `java` varchar(20) DEFAULT NULL,
  `sar` varchar(20) DEFAULT NULL,
  `marketing` varchar(20) DEFAULT NULL,
  `ml` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fid_idx` (`formid`),
  KEY `student1_idx` (`name`),
  KEY `student2_idx` (`name`),
  CONSTRAINT `fid` FOREIGN KEY (`formid`) REFERENCES `formation` (`formid`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stu_grade`
--

LOCK TABLES `stu_grade` WRITE;
/*!40000 ALTER TABLE `stu_grade` DISABLE KEYS */;
INSERT INTO `stu_grade` VALUES (1,'mike',1,'2023-10-28','13','11','15','10'),(2,'angel',3,'2023-10-11','11','13','12','10'),(4,'mike',1,'2023-11-02','11','9','13','10'),(5,'angel',3,'2023-10-30','11','8','16','12'),(6,'maxime',2,'2023-11-01','11','11','11','11'),(7,'lyly',4,'2023-11-01','12','11','12','10'),(9,'Nicolas',6,'2023-10-29','13','15','11','12'),(10,'Nicolas',6,'2023-10-31','11','12','16','11'),(11,'lyly',4,'2023-11-05','15','16','16','14'),(12,'Emma',8,'2023-10-29','15','14','16','13'),(13,'Emma',8,'2023-11-01','11','10','10','9'),(18,'maxime',2,'2023-11-28','4.5','12.5','11.0','8.5'),(20,'zzz',8,'2023-12-06','13.9','13.7','13.0','10.4');
/*!40000 ALTER TABLE `stu_grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stu_group`
--

DROP TABLE IF EXISTS `stu_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stu_group` (
  `grouplistid` int NOT NULL AUTO_INCREMENT,
  `groupid` int DEFAULT NULL,
  `projectname` varchar(45) DEFAULT NULL,
  `student1name` varchar(45) DEFAULT NULL,
  `student2name` varchar(45) DEFAULT NULL,
  `projectgrade` int DEFAULT NULL,
  PRIMARY KEY (`grouplistid`),
  KEY `student1` (`student1name`) /*!80000 INVISIBLE */,
  KEY `student2` (`student2name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stu_group`
--

LOCK TABLES `stu_group` WRITE;
/*!40000 ALTER TABLE `stu_group` DISABLE KEYS */;
INSERT INTO `stu_group` VALUES (1,2,'java','xxx','mike',NULL),(2,2,'sar','lyly','mike',NULL),(6,1,'marketing','mike','Emma',NULL),(7,1,'ml','mike','Nicolas',NULL);
/*!40000 ALTER TABLE `stu_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_information`
--

DROP TABLE IF EXISTS `user_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_information` (
  `id` int NOT NULL AUTO_INCREMENT,
  `password` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `job` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1112 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_information`
--

LOCK TABLES `user_information` WRITE;
/*!40000 ALTER TABLE `user_information` DISABLE KEYS */;
INSERT INTO `user_information` VALUES (111,'1111','lyly','étudiant'),(112,'112','Emma','étudiant'),(113,'113','Nicolas','étudiant'),(115,'115','linda','étudiant'),(1001,'1001','helen','enseignant'),(1002,'10086','francois','enseignant'),(1003,'1003','Aida','enseignant'),(1004,'1004','bob','enseignant'),(1005,'1005','lee','enseignant'),(1111,'1111','Adele','étudiant');
/*!40000 ALTER TABLE `user_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'studentmanage'
--

--
-- Dumping routines for database 'studentmanage'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-01  7:58:27
