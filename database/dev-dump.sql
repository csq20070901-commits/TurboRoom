mysqldump: [Warning] Using a password on the command line interface can be insecure.
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: dev
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `meetroom_admin`
--

DROP TABLE IF EXISTS `meetroom_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetroom_admin` (
  `ADMIN_ID` bigint NOT NULL AUTO_INCREMENT,
  `ADMIN_NAME` varchar(200) NOT NULL,
  `ADMIN_PASSWORD` varchar(255) NOT NULL,
  `ADMIN_DESC` varchar(255) DEFAULT NULL,
  `ADMIN_PHONE` varchar(255) DEFAULT NULL,
  `ADMIN_LOGIN_CNT` int NOT NULL DEFAULT '0',
  `ADMIN_TYPE` int NOT NULL DEFAULT '0',
  `ADMIN_STATUS` int NOT NULL DEFAULT '1',
  `ADMIN_LOGIN_TIME` bigint NOT NULL DEFAULT '0',
  `ADD_TIME` bigint NOT NULL DEFAULT '0',
  `EDIT_TIME` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`ADMIN_ID`),
  UNIQUE KEY `uk_admin_name` (`ADMIN_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetroom_admin`
--

LOCK TABLES `meetroom_admin` WRITE;
/*!40000 ALTER TABLE `meetroom_admin` DISABLE KEYS */;
INSERT INTO `meetroom_admin` VALUES (1,'admin','e10adc3949ba59abbe56e057f20f883e','系统管理员','13800000000',0,1,1,0,1760000000000,1760000000000);
/*!40000 ALTER TABLE `meetroom_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meetroom_fav`
--

DROP TABLE IF EXISTS `meetroom_fav`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetroom_fav` (
  `FAV_ID` bigint NOT NULL AUTO_INCREMENT,
  `FAV_USER_ID` bigint NOT NULL DEFAULT '0',
  `FAV_TITLE` varchar(255) NOT NULL,
  `FAV_TYPE` varchar(100) NOT NULL,
  `FAV_OID` bigint NOT NULL DEFAULT '0',
  `FAV_PATH` varchar(255) NOT NULL,
  `ADD_TIME` bigint NOT NULL DEFAULT '0',
  `EDIT_TIME` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`FAV_ID`),
  KEY `idx_fav_user` (`FAV_USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetroom_fav`
--

LOCK TABLES `meetroom_fav` WRITE;
/*!40000 ALTER TABLE `meetroom_fav` DISABLE KEYS */;
INSERT INTO `meetroom_fav` VALUES (2,1,'群观楼 Q202 会议室','meet',2,'/pages/meet/detail?id=2',1760000002000,1760000002000),(3,2,'文观楼 W101 研讨室','meet',1,'/pages/meet/detail?id=1',1760000003000,1760000003000),(4,3,'文观楼 W301 自习室','meet',3,'/pages/meet/detail?id=3',1760000004000,1760000004000),(5,4,'群观楼 Q202 会议室','meet',2,'/pages/meet/detail?id=2',1760000005000,1760000005000),(7,1,'文观楼 201','预约',4,'#/meet?id=4',1793185149752,1793185149752);
/*!40000 ALTER TABLE `meetroom_fav` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meetroom_meet`
--

DROP TABLE IF EXISTS `meetroom_meet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetroom_meet` (
  `MEET_ID` bigint NOT NULL AUTO_INCREMENT,
  `MEET_TITLE` varchar(255) NOT NULL,
  `MEET_CATE_ID` bigint NOT NULL DEFAULT '0',
  `MEET_CATE_NAME` varchar(255) DEFAULT NULL,
  `MEET_STATUS` int NOT NULL DEFAULT '1',
  `MEET_ORDER` int NOT NULL DEFAULT '9999',
  `MEET_VOUCH` int NOT NULL DEFAULT '0',
  `MEET_DAYS` longtext NOT NULL,
  `MEET_VIEW_CNT` int NOT NULL DEFAULT '0',
  `MEET_MAX_CNT` int NOT NULL DEFAULT '0',
  `MEET_FORMS` longtext NOT NULL,
  `MEET_OBJ` longtext NOT NULL,
  `ADD_TIME` bigint NOT NULL DEFAULT '0',
  `EDIT_TIME` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`MEET_ID`),
  KEY `idx_meet_status_order` (`MEET_STATUS`,`MEET_ORDER`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetroom_meet`
--

LOCK TABLES `meetroom_meet` WRITE;
/*!40000 ALTER TABLE `meetroom_meet` DISABLE KEYS */;
INSERT INTO `meetroom_meet` VALUES (1,'文观楼 W101 研讨室',1,'研讨室',0,1,1,'[\"2026-10-16\",\"2026-10-17\",\"2026-10-18\",\"2026-10-19\",\"2026-10-20\"]',1,20,'[]','{\"cover\":\"\",\"time\":\"08:00-22:00\",\"location\":\"文观楼 W101\"}',1760000000000,1792577764902),(2,'群观楼 Q202 会议室',2,'会议室',0,2,1,'[\"2026-10-16\",\"2026-10-17\",\"2026-10-18\",\"2026-10-19\",\"2026-10-20\"]',0,40,'[]','{\"cover\":\"\",\"time\":\"08:00-22:00\",\"location\":\"群观楼 Q202\"}',1760000000000,1760000000000),(3,'文观楼 W301 自习室',3,'自习室',0,3,0,'[\"2026-10-16\",\"2026-10-17\",\"2026-10-18\",\"2026-10-19\",\"2026-10-20\"]',0,60,'[]','{\"cover\":\"\",\"time\":\"08:00-22:00\",\"location\":\"文观楼 W301\"}',1760000000000,1760000000000),(4,'文观楼 201',1,'普通教室',1,1,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',18,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1793202984197),(5,'文观楼 202',1,'普通教室',1,2,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',5,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1793188575285),(6,'文观楼 203',1,'普通教室',1,3,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',1,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1793102468143),(10,'文观楼 301',1,'普通教室',1,7,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(11,'文观楼 302',1,'普通教室',1,8,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(12,'文观楼 303',1,'普通教室',1,9,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(15,'文观楼 401',1,'普通教室',1,12,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(16,'文观楼 402',1,'普通教室',1,13,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(17,'文观楼 403',1,'普通教室',1,14,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(25,'群观楼 101',1,'普通教室',1,22,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(26,'群观楼 102',1,'普通教室',1,23,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(27,'群观楼 103',1,'普通教室',1,24,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(28,'群观楼 104',1,'普通教室',1,25,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(29,'群观楼 105',1,'普通教室',1,26,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(30,'群观楼 201',1,'普通教室',1,27,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(31,'群观楼 202',1,'普通教室',1,28,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(32,'群观楼 203',1,'普通教室',1,29,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(33,'群观楼 204',1,'普通教室',1,30,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(34,'群观楼 205',1,'普通教室',1,31,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"普通教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-classroom.png\"}',1792672410000,1792672410000),(74,'文观楼 515',3,'计算机机房',1,201,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,60,'[]','{\"tag\": [\"计算机机房\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-computer.png\"}',1792672410000,1792672410000),(75,'文观楼 516',3,'计算机机房',1,202,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,60,'[]','{\"tag\": [\"计算机机房\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-computer.png\"}',1792672410000,1792672410000),(76,'文观楼 517',3,'计算机机房',1,203,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,60,'[]','{\"tag\": [\"计算机机房\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-computer.png\"}',1792672410000,1792672410000),(77,'文观楼 518',3,'计算机机房',1,204,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',4,60,'[]','{\"tag\": [\"计算机机房\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-computer.png\"}',1792672410000,1793104778136),(78,'群观楼 F111 财务管理专业机房',3,'计算机机房',1,205,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,60,'[]','{\"tag\": [\"计算机机房\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-computer.png\"}',1792672410000,1792672410000),(81,'文观楼 100 新天地实训室',4,'专业实验室',1,301,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"专业实验室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-lab.png\"}',1792672410000,1792672410000),(82,'文观楼 103 康复医学实训中心',4,'专业实验室',1,302,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"专业实验室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-lab.png\"}',1792672410000,1792672410000),(83,'文观楼 404 装饰材料实验室',4,'专业实验室',1,303,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"专业实验室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-lab.png\"}',1792672410000,1792672410000),(84,'群观楼 F112 虚拟商业环境实训室',4,'专业实验室',1,304,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"专业实验室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-lab.png\"}',1792672410000,1792672410000),(85,'群观楼 F114 物理实验室（二）',4,'专业实验室',1,305,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"专业实验室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-lab.png\"}',1792672410000,1792672410000),(86,'群观楼 117 物理实验室',4,'专业实验室',1,306,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"专业实验室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-lab.png\"}',1792672410000,1792672410000),(87,'群观楼 315 健康管理综合实训室',4,'专业实验室',1,307,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"专业实验室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-lab.png\"}',1792672410000,1792672410000),(88,'影视教学实验实训室',4,'专业实验室',1,308,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"专业实验室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-lab.png\"}',1792672410000,1792672410000),(89,'工艺制作实验室',4,'专业实验室',1,309,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"专业实验室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-lab.png\"}',1792672410000,1792672410000),(96,'群观楼 112 智能语音室',5,'语音室',1,401,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"语音室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-language.png\"}',1792672410000,1792672410000),(97,'文观楼 101 健身房',6,'形体健身房',1,501,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,30,'[]','{\"tag\": [\"形体健身房\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-gym.png\"}',1792672410000,1792672410000),(98,'文观楼 605 形体室',6,'形体健身房',1,502,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,30,'[]','{\"tag\": [\"形体健身房\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-gym.png\"}',1792672410000,1792672410000),(100,'文观楼 405 艺术教室',7,'艺术教室',1,601,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"艺术教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-art.png\"}',1792672410000,1792672410000),(101,'文观楼 406 艺术教室',7,'艺术教室',1,602,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"艺术教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-art.png\"}',1792672410000,1792672410000),(102,'文观楼 407 艺术教室',7,'艺术教室',1,603,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"艺术教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-art.png\"}',1792672410000,1792672410000),(103,'文观楼 408 艺术教室',7,'艺术教室',1,604,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',1,40,'[]','{\"tag\": [\"艺术教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-art.png\"}',1792672410000,1793186488176),(104,'冰雪艺术实验室',7,'艺术教室',1,605,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,40,'[]','{\"tag\": [\"艺术教室\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/cat-art.png\"}',1792672410000,1792672410000),(107,'羽毛球场',8,'体育场馆',1,701,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',1,80,'[]','{\"tag\": [\"体育场馆\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/sport-badminton.png\"}',1792672410000,1793185719783),(108,'足球场',8,'体育场馆',1,702,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,80,'[]','{\"tag\": [\"体育场馆\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/sport-football.png\"}',1792672410000,1792672410000),(109,'足球场2',8,'体育场馆',1,703,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,80,'[]','{\"tag\": [\"体育场馆\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/sport-football.png\"}',1792672410000,1792672410000),(110,'篮球场1',8,'体育场馆',1,704,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,80,'[]','{\"tag\": [\"体育场馆\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/sport-basketball.png\"}',1792672410000,1792672410000),(111,'篮球场2',8,'体育场馆',1,705,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,80,'[]','{\"tag\": [\"体育场馆\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/sport-basketball.png\"}',1792672410000,1792672410000),(112,'篮球场3',8,'体育场馆',1,706,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,80,'[]','{\"tag\": [\"体育场馆\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/sport-basketball.png\"}',1792672410000,1792672410000),(113,'篮球场4',8,'体育场馆',1,707,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,80,'[]','{\"tag\": [\"体育场馆\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/sport-basketball.png\"}',1792672410000,1792672410000),(114,'排球场1',8,'体育场馆',1,708,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,80,'[]','{\"tag\": [\"体育场馆\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/sport-volleyball.png\"}',1792672410000,1792672410000),(115,'排球场2',8,'体育场馆',1,709,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',0,80,'[]','{\"tag\": [\"体育场馆\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/sport-volleyball.png\"}',1792672410000,1792672410000),(116,'文观楼 102 毽球场地',8,'体育场馆',1,710,0,'[\"2026-10-28\",\"2026-10-29\",\"2026-10-30\",\"2026-10-31\",\"2026-11-01\"]',1,80,'[]','{\"tag\": [\"体育场馆\"], \"time\": [{\"title\": \"08:00-10:00\"}, {\"title\": \"10:00-12:00\"}, {\"title\": \"14:00-16:00\"}, {\"title\": \"16:00-18:00\"}, {\"title\": \"19:00-21:00\"}], \"cover\": \"/pic/sport-shuttlecock.png\"}',1792672410000,1793185731425);
/*!40000 ALTER TABLE `meetroom_meet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meetroom_meet_join`
--

DROP TABLE IF EXISTS `meetroom_meet_join`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetroom_meet_join` (
  `MEET_JOIN_ID` bigint NOT NULL AUTO_INCREMENT,
  `MEET_JOIN_USER_ID` bigint NOT NULL DEFAULT '0',
  `MEET_JOIN_MEET_ID` bigint NOT NULL DEFAULT '0',
  `MEET_JOIN_CODE` varchar(20) NOT NULL,
  `MEET_JOIN_IS_CHECK` int NOT NULL DEFAULT '0',
  `MEET_JOIN_CHECK_TIME` bigint NOT NULL DEFAULT '0',
  `MEET_JOIN_FORMS` longtext NOT NULL,
  `MEET_JOIN_OBJ` longtext NOT NULL,
  `MEET_JOIN_STATUS` int NOT NULL DEFAULT '0',
  `MEET_JOIN_TIME` varchar(255) NOT NULL,
  `MEET_JOIN_DAY` varchar(255) NOT NULL,
  `MEET_JOIN_MEET_TITLE` varchar(255) NOT NULL,
  `ADD_TIME` bigint NOT NULL DEFAULT '0',
  `EDIT_TIME` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`MEET_JOIN_ID`),
  KEY `idx_join_meet_day_time` (`MEET_JOIN_MEET_ID`,`MEET_JOIN_DAY`,`MEET_JOIN_TIME`),
  KEY `idx_join_user` (`MEET_JOIN_USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetroom_meet_join`
--

LOCK TABLES `meetroom_meet_join` WRITE;
/*!40000 ALTER TABLE `meetroom_meet_join` DISABLE KEYS */;
INSERT INTO `meetroom_meet_join` VALUES (2,2,1,'MR202610160002',1,1760000200000,'[]','{\"name\":\"张同学\",\"college\":\"信息工程学院\"}',1,'08:00-10:00','2026-10-16','文观楼 W101 研讨室',1760000011000,1760000011000),(3,3,2,'MR202610170001',0,0,'[]','{\"name\":\"李同学\",\"college\":\"管理学院\"}',1,'14:00-16:00','2026-10-17','群观楼 Q202 会议室',1760000012000,1760000012000),(4,4,2,'MR202610180001',0,0,'[]','{\"name\":\"王同学\",\"college\":\"外国语学院\"}',1,'19:00-21:00','2026-10-18','群观楼 Q202 会议室',1760000013000,1760000013000),(6,1,4,'8988164229',0,0,'[{\"mark\":\"name\",\"title\":\"姓名\",\"type\":\"text\",\"val\":\"许一甲\"},{\"mark\":\"phone\",\"title\":\"手机\",\"type\":\"mobile\",\"val\":\"12344444444\"}]','{\"name\":\"许一甲\",\"phone\":\"12344444444\"}',1,'08:00-10:00','2026-10-29','文观楼 201',0,0),(8,1,5,'3468239049',0,0,'[{\"mark\":\"name\",\"title\":\"姓名\",\"type\":\"text\",\"val\":\"许一甲\"},{\"mark\":\"phone\",\"title\":\"手机\",\"type\":\"mobile\",\"val\":\"12344444444\"}]','{\"name\":\"许一甲\",\"phone\":\"12344444444\"}',1,'08:00-10:00','2026-10-28','文观楼 202',0,0);
/*!40000 ALTER TABLE `meetroom_meet_join` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meetroom_news`
--

DROP TABLE IF EXISTS `meetroom_news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetroom_news` (
  `NEWS_ID` bigint NOT NULL AUTO_INCREMENT,
  `NEWS_TITLE` varchar(255) NOT NULL,
  `NEWS_CATE_ID` bigint NOT NULL DEFAULT '0',
  `NEWS_CATE_NAME` varchar(255) DEFAULT NULL,
  `NEWS_STATUS` int NOT NULL DEFAULT '1',
  `NEWS_ORDER` int NOT NULL DEFAULT '9999',
  `NEWS_VOUCH` int NOT NULL DEFAULT '0',
  `NEWS_CONTENT` longtext,
  `NEWS_VIEW_CNT` int NOT NULL DEFAULT '0',
  `NEWS_PIC` varchar(255) DEFAULT NULL,
  `NEWS_OBJ` longtext,
  `NEWS_FORMS` longtext,
  `ADD_TIME` bigint NOT NULL DEFAULT '0',
  `EDIT_TIME` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`NEWS_ID`),
  KEY `idx_news_status_order` (`NEWS_STATUS`,`NEWS_ORDER`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetroom_news`
--

LOCK TABLES `meetroom_news` WRITE;
/*!40000 ALTER TABLE `meetroom_news` DISABLE KEYS */;
INSERT INTO `meetroom_news` VALUES (1,'会议室预约使用说明',1,'通知公告',1,1,1,'一、预约流程\n1. 登录系统后进入「自习场地」页面\n2. 选择场地并查看可预约日期\n3. 选择时段并填写预约信息\n4. 提交预约并保存凭证\n\n二、注意事项\n1. 请提前确认开放时间再预约\n2. 每人同时段仅可预约一个场地\n3. 如需取消请提前操作\n4. 预约成功后请按时到场',15,NULL,'{\"cover\": \"/pic/news-1.png\"}','[]',1760000000000,1793192981328),(2,'文明使用会议室倡议',1,'通知公告',1,2,1,'一、使用规范\n1. 请按时使用，不迟到早退\n2. 保持室内安静，勿大声喧哗\n3. 爱护公共设施，损坏需赔偿\n\n二、离场要求\n1. 离开前关闭空调、灯光等设备\n2. 带走个人物品和垃圾\n3. 将桌椅归位，保持整洁\n\n三、违规处理\n违反使用规范者将暂停预约权限',1,NULL,'{\"cover\": \"/pic/news-2.png\"}','[]',1760000000000,1793185081360),(3,'系统试运行通知',1,'通知公告',1,3,0,'会议室预约系统已进入试运行阶段。',1,NULL,'{\"cover\": \"/pic/news-3.png\"}','[]',1760000000000,1793185086266);
/*!40000 ALTER TABLE `meetroom_news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meetroom_user`
--

DROP TABLE IF EXISTS `meetroom_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetroom_user` (
  `USER_ID` bigint NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(255) NOT NULL,
  `USER_ACCOUNT` varchar(255) NOT NULL,
  `USER_STATUS` int NOT NULL DEFAULT '1',
  `USER_PASSWORD` varchar(255) NOT NULL,
  `USER_LOGIN_TIME` bigint NOT NULL DEFAULT '0',
  `USER_LOGIN_CNT` int NOT NULL DEFAULT '0',
  `USER_OBJ` longtext,
  `USER_FORMS` longtext,
  `ADD_TIME` bigint NOT NULL DEFAULT '0',
  `EDIT_TIME` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `uk_user_account` (`USER_ACCOUNT`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetroom_user`
--

LOCK TABLES `meetroom_user` WRITE;
/*!40000 ALTER TABLE `meetroom_user` DISABLE KEYS */;
INSERT INTO `meetroom_user` VALUES (1,'测试用户','student',1,'e10adc3949ba59abbe56e057f20f883e',1793196137735,0,'{\"sex\":\"男\",\"birth\":\"2006-12-31\",\"college\":\"计算机学院\",\"major\":\"计算机科学与技术\",\"item\":\"2501\",\"year\":2024,\"no\":\"1111111111\"}','[{\"mark\":\"sex\",\"title\":\"性别\",\"type\":\"select\",\"val\":\"男\"},{\"mark\":\"birth\",\"title\":\"出生年月\",\"type\":\"date\",\"val\":\"2006-12-31\"},{\"mark\":\"college\",\"title\":\"学院系所\",\"type\":\"text\",\"val\":\"计算机学院\"},{\"mark\":\"major\",\"title\":\"所学专业\",\"type\":\"text\",\"val\":\"计算机科学与技术\"},{\"mark\":\"item\",\"title\":\"班级\",\"type\":\"text\",\"val\":\"2501\"},{\"mark\":\"year\",\"title\":\"入学年份\",\"type\":\"year\",\"val\":2024},{\"mark\":\"no\",\"title\":\"学号\",\"type\":\"text\",\"val\":\"1111111111\"}]',1760000000000,1793104709986),(2,'张同学','zhangsan',1,'e10adc3949ba59abbe56e057f20f883e',0,0,'{\"college\":\"信息工程学院\",\"phone\":\"13800000002\"}','[]',1760000000000,1760000000000),(3,'李同学','lisi',1,'e10adc3949ba59abbe56e057f20f883e',0,0,'{\"college\":\"管理学院\",\"phone\":\"13800000003\"}','[]',1760000000000,1760000000000),(4,'王同学','wangwu',1,'e10adc3949ba59abbe56e057f20f883e',0,0,'{\"college\":\"外国语学院\",\"phone\":\"13800000004\"}','[]',1760000000000,1760000000000),(5,'爱学习的佐助同学','zuozhu',1,'e10adc3949ba59abbe56e057f20f883e',1793189230164,0,'{\"sex\":\"男\",\"birth\":\"2006-12-31\",\"college\":\"计算机科学学院\",\"major\":\"计算机科学与技术\",\"item\":\"2501\",\"year\":2025,\"no\":\"999999999999\"}','[{\"mark\":\"sex\",\"title\":\"性别\",\"type\":\"select\",\"val\":\"男\"},{\"mark\":\"birth\",\"title\":\"出生年月\",\"type\":\"date\",\"val\":\"2006-12-31\"},{\"mark\":\"college\",\"title\":\"学院系所\",\"type\":\"text\",\"val\":\"计算机科学学院\"},{\"mark\":\"major\",\"title\":\"所学专业\",\"type\":\"text\",\"val\":\"计算机科学与技术\"},{\"mark\":\"item\",\"title\":\"班级\",\"type\":\"text\",\"val\":\"2501\"},{\"mark\":\"year\",\"title\":\"入学年份\",\"type\":\"year\",\"val\":2025},{\"mark\":\"no\",\"title\":\"学号\",\"type\":\"text\",\"val\":\"999999999999\"}]',1793189192332,1793189192332);
/*!40000 ALTER TABLE `meetroom_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-29 15:59:38
