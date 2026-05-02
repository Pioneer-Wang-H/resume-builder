-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: resume_builder
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Current Database: `resume_builder`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `resume_builder` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `resume_builder`;

--
-- Table structure for table `basic_info`
--

DROP TABLE IF EXISTS `basic_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `basic_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `linkedin` varchar(255) DEFAULT NULL,
  `github` varchar(255) DEFAULT NULL,
  `intended_position` varchar(100) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `basic_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='åŸºæœ¬ä¿¡æ¯';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basic_info`
--

LOCK TABLES `basic_info` WRITE;
/*!40000 ALTER TABLE `basic_info` DISABLE KEYS */;
INSERT INTO `basic_info` VALUES (1,1,'教育背景','11111111111','zhangsan@example.com','','','','后端','广州','2002-12-01',NULL,'2026-04-29 16:09:14','2026-04-29 16:09:14');
/*!40000 ALTER TABLE `basic_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificate`
--

DROP TABLE IF EXISTS `certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `name` varchar(100) NOT NULL,
  `issuing_authority` varchar(100) DEFAULT NULL,
  `obtain_date` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `sort_order` int DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `certificate_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='è¯ä¹¦å¥–é¡¹';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificate`
--

LOCK TABLES `certificate` WRITE;
/*!40000 ALTER TABLE `certificate` DISABLE KEYS */;
/*!40000 ALTER TABLE `certificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `education`
--

DROP TABLE IF EXISTS `education`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `education` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `school` varchar(100) NOT NULL,
  `major` varchar(100) DEFAULT NULL,
  `degree` varchar(20) DEFAULT NULL,
  `gpa` varchar(10) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `description` text,
  `sort_order` int DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `education_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='æ•™è‚²ç»åŽ†';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `education`
--

LOCK TABLES `education` WRITE;
/*!40000 ALTER TABLE `education` DISABLE KEYS */;
INSERT INTO `education` VALUES (1,1,'广州理工学院','软件工程','本科','3.4','2022-09-01','2026-06-01','',0,'2026-04-29 17:32:00','2026-04-29 17:32:00'),(2,1,'某某大学 计算机科学与技术 (本科) 2022.09',NULL,NULL,NULL,NULL,NULL,NULL,0,'2026-04-29 21:20:54','2026-04-29 21:20:54'),(3,1,'五道山','408','本科','',NULL,NULL,'',0,'2026-05-02 10:08:31','2026-05-02 10:08:31'),(5,1,'青山','软件','本科','',NULL,NULL,'',0,'2026-05-02 10:18:50','2026-05-02 10:18:50');
/*!40000 ALTER TABLE `education` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_application`
--

DROP TABLE IF EXISTS `job_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_application` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `resume_id` bigint DEFAULT NULL,
  `company` varchar(100) NOT NULL,
  `position` varchar(100) DEFAULT NULL,
  `salary_range` varchar(50) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `channel` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'å·²æŠ•é€’',
  `apply_date` date DEFAULT NULL,
  `notes` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `resume_id` (`resume_id`),
  CONSTRAINT `job_application_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `job_application_ibfk_2` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='æŠ•é€’è®°å½•';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_application`
--

LOCK TABLES `job_application` WRITE;
/*!40000 ALTER TABLE `job_application` DISABLE KEYS */;
INSERT INTO `job_application` VALUES (1,1,NULL,'撒旦','十大','大大','大苏打','BOSS直聘','已投递',NULL,'','2026-04-29 19:07:19','2026-04-29 19:07:19'),(2,1,NULL,'阿斯顿','阿斯顿','66666','123','BOSS直聘','已投递',NULL,'','2026-05-02 10:09:02','2026-05-02 10:09:02');
/*!40000 ALTER TABLE `job_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation_log`
--

DROP TABLE IF EXISTS `operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `module` varchar(50) NOT NULL COMMENT 'æ“ä½œæ¨¡å—',
  `operation` varchar(100) NOT NULL COMMENT 'æ“ä½œæè¿°',
  `params` text COMMENT 'è¯·æ±‚å‚æ•°JSON',
  `ip` varchar(50) DEFAULT NULL COMMENT 'è¯·æ±‚IP',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_module` (`module`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='æ“ä½œæ—¥å¿—';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_log`
--

LOCK TABLES `operation_log` WRITE;
/*!40000 ALTER TABLE `operation_log` DISABLE KEYS */;
INSERT INTO `operation_log` VALUES (1,1,'素材管理','删除教育经历','[4]','0:0:0:0:0:0:0:1','2026-05-02 10:18:40'),(2,1,'素材管理','新增教育经历','[{\"id\":5,\"userId\":1,\"school\":\"青山\",\"major\":\"软件\",\"degree\":\"本科\",\"gpa\":\"\",\"startDate\":null,\"endDate\":null,\"description\":\"\",\"sortOrder\":null,\"createTime\":\"2026-05-02T10:18:49.5995577\",\"updateTime\":\"2026-05-02T10:18:49.6008799\"}]','0:0:0:0:0:0:0:1','2026-05-02 10:18:50');
/*!40000 ALTER TABLE `operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_experience`
--

DROP TABLE IF EXISTS `project_experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_experience` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `project_name` varchar(100) NOT NULL,
  `role` varchar(50) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `description` text,
  `highlights` json DEFAULT NULL,
  `sort_order` int DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `project_experience_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='é¡¹ç›®ç»åŽ†';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_experience`
--

LOCK TABLES `project_experience` WRITE;
/*!40000 ALTER TABLE `project_experience` DISABLE KEYS */;
INSERT INTO `project_experience` VALUES (1,1,'牛马项目','负责人','2025-06-01','2026-09-01','','','[]',0,'2026-04-29 17:32:40','2026-04-29 17:32:40');
/*!40000 ALTER TABLE `project_experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume`
--

DROP TABLE IF EXISTS `resume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `template_id` bigint DEFAULT '1',
  `title` varchar(100) NOT NULL,
  `is_default` tinyint DEFAULT '0',
  `status` tinyint DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `template_id` (`template_id`),
  CONSTRAINT `resume_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `resume_ibfk_2` FOREIGN KEY (`template_id`) REFERENCES `resume_template` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ç®€åŽ†';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume`
--

LOCK TABLES `resume` WRITE;
/*!40000 ALTER TABLE `resume` DISABLE KEYS */;
INSERT INTO `resume` VALUES (1,1,1,'后端岗位',0,1,'2026-04-29 16:09:41','2026-04-29 16:09:41'),(2,1,1,'测试',0,1,'2026-05-02 09:47:33','2026-05-02 09:47:33');
/*!40000 ALTER TABLE `resume` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume_section`
--

DROP TABLE IF EXISTS `resume_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume_section` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resume_id` bigint NOT NULL,
  `section_type` varchar(30) NOT NULL,
  `enabled` tinyint DEFAULT '1',
  `sort_order` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_resume_section` (`resume_id`,`section_type`),
  CONSTRAINT `resume_section_ibfk_1` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ç®€åŽ†æ¨¡å—';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume_section`
--

LOCK TABLES `resume_section` WRITE;
/*!40000 ALTER TABLE `resume_section` DISABLE KEYS */;
INSERT INTO `resume_section` VALUES (1,1,'basic_info',1,0),(2,1,'education',1,1),(3,1,'project_experience',1,2),(4,1,'work_experience',1,3),(5,1,'skills',1,4),(6,1,'certificates',1,5),(7,1,'self_evaluation',1,6),(8,2,'basic_info',1,0),(9,2,'education',1,1),(10,2,'project_experience',1,2),(11,2,'work_experience',1,3),(12,2,'skills',1,4),(13,2,'certificates',1,5),(14,2,'self_evaluation',1,6);
/*!40000 ALTER TABLE `resume_section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume_section_item`
--

DROP TABLE IF EXISTS `resume_section_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume_section_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `section_id` bigint NOT NULL,
  `item_type` varchar(30) NOT NULL,
  `item_id` bigint NOT NULL,
  `sort_order` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `section_id` (`section_id`),
  CONSTRAINT `resume_section_item_ibfk_1` FOREIGN KEY (`section_id`) REFERENCES `resume_section` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ç®€åŽ†ç´ æå…³è”';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume_section_item`
--

LOCK TABLES `resume_section_item` WRITE;
/*!40000 ALTER TABLE `resume_section_item` DISABLE KEYS */;
INSERT INTO `resume_section_item` VALUES (3,2,'education',1,0),(4,3,'project_experience',1,0);
/*!40000 ALTER TABLE `resume_section_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume_template`
--

DROP TABLE IF EXISTS `resume_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume_template` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `template_file` varchar(255) NOT NULL,
  `is_builtin` tinyint DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ç®€åŽ†æ¨¡æ¿';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume_template`
--

LOCK TABLES `resume_template` WRITE;
/*!40000 ALTER TABLE `resume_template` DISABLE KEYS */;
INSERT INTO `resume_template` VALUES (1,'ç»å…¸ä¸¤æ ',NULL,'template-classic.html',1,'2026-04-29 07:47:57'),(2,'ç®€çº¦çº¿æ¡',NULL,'template-minimal.html',1,'2026-04-29 07:47:57'),(3,'å·¦å³åˆ†æ ',NULL,'template-split.html',1,'2026-04-29 07:47:57');
/*!40000 ALTER TABLE `resume_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `self_evaluation`
--

DROP TABLE IF EXISTS `self_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `self_evaluation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `content` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `self_evaluation_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='è‡ªæˆ‘è¯„ä»·';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `self_evaluation`
--

LOCK TABLES `self_evaluation` WRITE;
/*!40000 ALTER TABLE `self_evaluation` DISABLE KEYS */;
INSERT INTO `self_evaluation` VALUES (1,1,'你好','2026-04-29 17:55:32','2026-04-29 17:55:32');
/*!40000 ALTER TABLE `self_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill_tag`
--

DROP TABLE IF EXISTS `skill_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `skill_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `skill_name` varchar(50) NOT NULL,
  `proficiency` int DEFAULT '3',
  `category` varchar(30) DEFAULT NULL,
  `sort_order` int DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `skill_tag_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='æŠ€èƒ½æ ‡ç­¾';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill_tag`
--

LOCK TABLES `skill_tag` WRITE;
/*!40000 ALTER TABLE `skill_tag` DISABLE KEYS */;
INSERT INTO `skill_tag` VALUES (1,1,'java',3,'编程语言',0,'2026-04-29 16:25:32'),(2,1,'python',3,'编程语言',0,'2026-04-29 17:32:51'),(3,1,'Spring Boot',3,NULL,0,'2026-04-29 21:20:54'),(4,1,'MyBatis',3,NULL,0,'2026-04-29 21:20:54'),(5,1,'MySQL',3,NULL,0,'2026-04-29 21:20:54'),(6,1,'Redis',3,NULL,0,'2026-04-29 21:20:54'),(8,1,'采用Redis进行提前预热和缓',3,NULL,0,'2026-04-29 21:20:54'),(9,1,'存。此方案有效降低了MySQL的查询并发量',3,NULL,0,'2026-04-29 21:20:54'),(10,1,'将核心接口的响应时间缩短了一半以上。',3,NULL,0,'2026-04-29 21:20:54');
/*!40000 ALTER TABLE `skill_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ç”¨æˆ·è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'test','$2b$10$/rU2KmVmg2To2Zd98/EhcO8rYwktGgCrAqk71uPWX0ysvMNIMlgC2','æµ‹è¯•ç”¨æˆ·','test@example.com',NULL,'2026-04-29 07:47:57','2026-04-29 08:03:36');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_file`
--

DROP TABLE IF EXISTS `user_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_file` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `file_type` varchar(30) DEFAULT NULL,
  `file_size` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_file_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ç”¨æˆ·æ–‡ä»¶';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_file`
--

LOCK TABLES `user_file` WRITE;
/*!40000 ALTER TABLE `user_file` DISABLE KEYS */;
INSERT INTO `user_file` VALUES (1,1,'我的简历.pdf','E:\\project\\429springbootProject\\backend\\uploads\\resume_imports\\c2ff04da-a9d4-4397-8977-161859a6ffc4.pdf','.pdf',739209,'2026-04-29 21:16:11'),(2,1,'我的简历.pdf','E:\\project\\429springbootProject\\backend\\uploads\\resume_imports\\413509cb-6be1-4bfd-acfc-f7b23fc3ab27.pdf','.pdf',739209,'2026-04-29 21:16:32'),(3,1,'我的简历.pdf','E:\\project\\429springbootProject\\backend\\uploads\\resume_imports\\7904332f-3676-47d1-8a5b-2871fbf06062.pdf','.pdf',739209,'2026-04-29 21:20:47'),(4,1,'求职简历.pdf','E:\\project\\429springbootProject\\backend\\uploads\\resume_imports\\7e93e87c-eb07-40d4-b614-b43652ac1ec8.pdf','.pdf',210698,'2026-05-02 09:42:46'),(5,1,'求职简历.pdf','E:\\project\\429springbootProject\\backend\\uploads\\resume_imports\\38cd7368-648f-410b-87b9-d7a9cf3dfece.pdf','.pdf',773508,'2026-05-02 09:46:45'),(6,1,'求职简历.pdf','E:\\project\\429springbootProject\\backend\\uploads\\resume_imports\\08e04903-3298-4d75-b347-68fac098434e.pdf','.pdf',773508,'2026-05-02 09:47:11');
/*!40000 ALTER TABLE `user_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_experience`
--

DROP TABLE IF EXISTS `work_experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_experience` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `company` varchar(100) NOT NULL,
  `position` varchar(100) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `description` text,
  `highlights` json DEFAULT NULL,
  `sort_order` int DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `work_experience_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='å·¥ä½œç»åŽ†';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_experience`
--

LOCK TABLES `work_experience` WRITE;
/*!40000 ALTER TABLE `work_experience` DISABLE KEYS */;
INSERT INTO `work_experience` VALUES (1,1,'乐于接受挑战，在实习中主动跟进慢 SQL、接口性能等问题并完成优化，善于复盘总结经验。',NULL,NULL,NULL,NULL,NULL,0,'2026-04-29 21:20:54','2026-04-29 21:20:54'),(2,1,'沟通协作能力强，做事踏实靠谱，具备较强的问题意识与执行力，能快速适应开发节奏并高效完成交付任务。',NULL,NULL,NULL,NULL,NULL,0,'2026-04-29 21:20:54','2026-04-29 21:20:54'),(3,1,'小林简历','138****8888 zhangsan@example.com',NULL,NULL,NULL,NULL,0,'2026-04-29 21:20:54','2026-04-29 21:20:54'),(4,1,'男 应届生 Java后端工程师',NULL,NULL,NULL,NULL,NULL,0,'2026-04-29 21:20:54','2026-04-29 21:20:54');
/*!40000 ALTER TABLE `work_experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'resume_builder'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-02 10:20:54
