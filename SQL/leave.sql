-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: localhost    Database: leave
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `entitlement`
--

DROP TABLE IF EXISTS `entitlement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `entitlement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(45) DEFAULT NULL,
  `entitled_days` int(11) DEFAULT NULL,
  `leave_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK28opeb4oasl8egwlya6n2vs6h` (`leave_type_id`),
  CONSTRAINT `FK28opeb4oasl8egwlya6n2vs6h` FOREIGN KEY (`leave_type_id`) REFERENCES `leave_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entitlement`
--

LOCK TABLES `entitlement` WRITE;
/*!40000 ALTER TABLE `entitlement` DISABLE KEYS */;
INSERT INTO `entitlement` VALUES (1,'Admin',14,1),(2,'Employee',18,1),(3,'Manager',18,1),(4,'Admin',60,2),(5,'Employee',60,2),(6,'Manager',60,2);
/*!40000 ALTER TABLE `entitlement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `holiday`
--

DROP TABLE IF EXISTS `holiday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `holiday` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `holiday_date` date DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `holiday`
--

LOCK TABLES `holiday` WRITE;
/*!40000 ALTER TABLE `holiday` DISABLE KEYS */;
INSERT INTO `holiday` VALUES (1,'2019-01-01','New Year'),(2,'2019-02-05','Chinese New Year'),(3,'2019-02-06','Chinese New Year 2'),(4,'2019-04-19','Good Friday'),(6,'2019-05-01','Labour Day'),(7,'2019-06-05','Hari Raya Puasa'),(8,'2019-08-09','National Day'),(9,'2019-08-11','Hari Raya Haji'),(10,'2019-10-27','Deepavali'),(11,'2019-12-25','Christmas Day'),(12,'2019-05-19','Vesak Day'),(13,'2019-05-20','Vesak Day Holiday');
/*!40000 ALTER TABLE `holiday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_balance`
--

DROP TABLE IF EXISTS `leave_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `leave_balance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `staff_id` int(11) DEFAULT NULL COMMENT '			',
  `leave_type_id` int(11) DEFAULT NULL,
  `balance` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr7fmdsbyl1l02pt10gdvgkkrq` (`leave_type_id`),
  KEY `FK5i735xqvtd3qo1ywxhhn2ixbw` (`staff_id`),
  CONSTRAINT `FK5i735xqvtd3qo1ywxhhn2ixbw` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `FKr7fmdsbyl1l02pt10gdvgkkrq` FOREIGN KEY (`leave_type_id`) REFERENCES `leave_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_balance`
--

LOCK TABLES `leave_balance` WRITE;
/*!40000 ALTER TABLE `leave_balance` DISABLE KEYS */;
INSERT INTO `leave_balance` VALUES (1,1,1,4),(2,1,2,53),(3,1,3,0),(4,2,1,10),(5,2,2,54),(6,2,3,0),(7,3,1,5),(8,3,2,48),(9,3,3,0),(10,4,1,18),(11,4,2,60),(12,4,3,0),(13,5,1,18),(14,5,2,60),(15,5,3,0),(16,6,1,6),(17,6,2,57),(18,6,3,0),(19,7,1,13),(20,7,2,56),(21,7,3,0),(22,8,1,8),(23,8,2,30),(24,8,3,0),(25,9,1,18),(26,9,2,60),(27,9,3,0);
/*!40000 ALTER TABLE `leave_balance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_record`
--

DROP TABLE IF EXISTS `leave_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `leave_record` (
  `leave_id` int(11) NOT NULL AUTO_INCREMENT,
  `staff_id` int(11) DEFAULT NULL,
  `leave_type_id` int(11) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `contact_no` varchar(255) DEFAULT NULL,
  `handover` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`leave_id`),
  KEY `FKew6wl5p6ba29itayru5h11wc4` (`leave_type_id`),
  KEY `FK9ie8p293ivssft2hj6e533ofk` (`staff_id`),
  CONSTRAINT `FK9ie8p293ivssft2hj6e533ofk` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `FKew6wl5p6ba29itayru5h11wc4` FOREIGN KEY (`leave_type_id`) REFERENCES `leave_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_record`
--

LOCK TABLES `leave_record` WRITE;
/*!40000 ALTER TABLE `leave_record` DISABLE KEYS */;
INSERT INTO `leave_record` VALUES (1,1,1,'taking one week exam break','2019-04-01','2019-04-05',5,'','covered by petrina','Decision pending','Applied'),(2,1,2,'Cough and flu','2019-04-10','2019-04-12',3,'','none','Decision pending','Applied'),(3,1,2,'wisdom tooth extraction','2019-04-15','2019-04-18',4,'','covered by petrina','Decision pending','Applied'),(4,1,1,'going Thailand for shopping','2019-04-29','2019-04-30',2,'98127324','covered by leo','Decision pending','Applied'),(5,1,1,'going staycation with family (local)','2019-05-08','2019-05-13',3,'','covered by leo','Decision pending','Applied'),(6,6,2,'fever and cough','2019-04-01','2019-04-03',3,'','covered by Leo','approve','Approved'),(7,2,1,'going taiwan for holiday with family','2019-06-03','2019-06-07',4,'93239222','covered by elizabeth','Decision pending','Applied'),(8,7,2,'sick','2019-04-08','2019-04-09',2,'','covered by emily','approve','Approved'),(9,3,1,'local holiday','2019-04-03','2019-04-15',9,'','covered by samuel','approved','Approved'),(10,3,2,'not feeling well','2019-04-16','2019-04-22',4,'','covered by ben','approve','Approved'),(11,3,2,'not feeling well','2019-04-24','2019-04-30',5,'','covered by Ben','approve','Approved'),(12,3,2,'not feeling well','2019-05-01','2019-05-03',3,'','covered by ben','approve','Approved'),(13,3,1,'overseas holiday','2019-06-03','2019-06-07',4,'91113824','covered by Samuel','approve','Approved'),(14,2,2,'fever','2019-04-08','2019-04-09',2,'','covered by elizabeth','approve','Approved'),(15,6,1,'fever','2019-04-10','2019-04-11',2,'','covered by Leo','approve','Approved'),(16,6,1,'going overseas holiday','2019-04-15','2019-04-17',3,'97854587','covered by Leo','approve','Approved'),(17,6,1,'local break','2019-05-21','2019-05-24',4,'','covered by leo','Decision pending','Applied'),(18,2,2,'not feeling well','2019-05-14','2019-05-17',4,'','covered by petrina','approve','Approved'),(19,7,2,'sick','2019-04-04','2019-04-05',2,'','covered by Emily','approve','Approved'),(21,7,1,'going overseas holiday','2019-04-15','2019-04-18',4,'','covered by Emily','approve','Cancelled'),(22,7,1,'taking local break','2019-05-07','2019-05-14',5,'','covered by Emily','Decision pending','Updated'),(23,7,1,'going overseas','2019-05-14','2019-05-16',3,'98982313','covered by Emily','Decision pending','Deleted'),(24,6,1,'going overseas with siblings','2019-06-04','2019-06-07',3,'','97854587','approve','Approved');
/*!40000 ALTER TABLE `leave_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_type`
--

DROP TABLE IF EXISTS `leave_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `leave_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `leave_type_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_type`
--

LOCK TABLES `leave_type` WRITE;
/*!40000 ALTER TABLE `leave_type` DISABLE KEYS */;
INSERT INTO `leave_type` VALUES (1,'Annual Leave'),(2,'Medical Leave'),(3,'Compensation Leave');
/*!40000 ALTER TABLE `leave_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Employee'),(2,'Manager'),(3,'Admin');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `staff` (
  `staff_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `report_to` int(11) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,'sa48team5@gmail.com','Elizabeth','password',4,'Admin','elizabeth'),(2,'sa48team5@gmail.com','Petrina','password',5,'Admin','petrina'),(3,'sa48team5@gmail.com','Andy Tan','password',3,'Manager','andytan'),(4,'sa48team5@gmail.com','Ben Chua','password',3,'Manager','benchua'),(5,'sa48team5@gmail.com','Samuel Ong','password',3,'Manager','samuelong'),(6,'sa48team5@gmail.com','Emily Ng','password',5,'Employee','emilyng'),(7,'sa48team5@gmail.com','Leo Fang','password',5,'Employee','leofang'),(8,'sa48team5@gmail.com','Sarah Seah','password',4,'Employee','sarahseah'),(9,'sa48team5@gmail.com','Timothy Lee','password',4,'Employee','timothylee');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-28 10:57:45
