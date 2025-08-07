CREATE DATABASE  IF NOT EXISTS `hk4_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hk4_project`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: hk4_project
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment_services`
--

DROP TABLE IF EXISTS `appointment_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment_services` (
  `id` int NOT NULL AUTO_INCREMENT,
  `price` decimal(38,2) DEFAULT NULL,
  `appointment_id` int NOT NULL,
  `service_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7smp9csy21h26g51aii9gvfn8` (`appointment_id`),
  KEY `FK68fbfnf0iy7uq0tfb5mjmm2hx` (`service_id`),
  CONSTRAINT `FK68fbfnf0iy7uq0tfb5mjmm2hx` FOREIGN KEY (`service_id`) REFERENCES `services` (`id`),
  CONSTRAINT `FK7smp9csy21h26g51aii9gvfn8` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`appointment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment_services`
--

LOCK TABLES `appointment_services` WRITE;
/*!40000 ALTER TABLE `appointment_services` DISABLE KEYS */;
INSERT INTO `appointment_services` VALUES (1,100000.00,1,1),(2,200000.00,1,2),(3,150000.00,2,1);
/*!40000 ALTER TABLE `appointment_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `appointment_id` int NOT NULL AUTO_INCREMENT,
  `appointment_time` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text,
  `image_url` varchar(255) DEFAULT NULL,
  `notes` text,
  `status` enum('CANCELLED','COMPLETED','CONFIRMED','IN_PROGRESS','PENDING') DEFAULT NULL,
  `garage_id` bigint DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `vehicle_type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`appointment_id`),
  KEY `FKh8ndlwfi7rbi2pjsajnp3k5rl` (`garage_id`),
  KEY `FK886ced1atxgvnf1o3oxtj5m4s` (`user_id`),
  KEY `FKs769arx5elbhwmv5qypovaq6t` (`vehicle_type_id`),
  CONSTRAINT `FK886ced1atxgvnf1o3oxtj5m4s` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKh8ndlwfi7rbi2pjsajnp3k5rl` FOREIGN KEY (`garage_id`) REFERENCES `garages` (`id`),
  CONSTRAINT `FKs769arx5elbhwmv5qypovaq6t` FOREIGN KEY (`vehicle_type_id`) REFERENCES `vehicle_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (1,'2025-08-06 00:00:00.000000','2025-07-07 00:00:00.000000','Sửa ok',NULL,'Sửa ok','COMPLETED',1,1,1),(2,'2025-07-07 00:00:00.000000','2025-06-07 00:00:00.000000','Ổn áp',NULL,'Ổn áp','CANCELLED',1,1,2);
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emergency_quotes`
--

DROP TABLE IF EXISTS `emergency_quotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_quotes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estimated_cost` decimal(38,2) DEFAULT NULL,
  `estimated_time` int DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `quoted_at` datetime(6) DEFAULT NULL,
  `status` enum('ACCEPTED','REJECTED','SENT') DEFAULT NULL,
  `garage_id` bigint DEFAULT NULL,
  `request_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg9kwgxs6gfp6ysvh5xfy1v2yd` (`garage_id`),
  KEY `FKg8obql77wcfdjpwn81o6en8c8` (`request_id`),
  CONSTRAINT `FKg8obql77wcfdjpwn81o6en8c8` FOREIGN KEY (`request_id`) REFERENCES `emergency_requests` (`request_id`),
  CONSTRAINT `FKg9kwgxs6gfp6ysvh5xfy1v2yd` FOREIGN KEY (`garage_id`) REFERENCES `garages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emergency_quotes`
--

LOCK TABLES `emergency_quotes` WRITE;
/*!40000 ALTER TABLE `emergency_quotes` DISABLE KEYS */;
/*!40000 ALTER TABLE `emergency_quotes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emergency_requests`
--

DROP TABLE IF EXISTS `emergency_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_requests` (
  `request_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `status` enum('ACCEPTED','CANCELLED','COMPLETED','PENDING','QUOTED') DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `vehicle_type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`request_id`),
  KEY `FK1kt0yy2sauibs2c8608dhb1jw` (`user_id`),
  KEY `FK4w09ueylyxw4gtuvouh6jspxs` (`vehicle_type_id`),
  CONSTRAINT `FK1kt0yy2sauibs2c8608dhb1jw` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK4w09ueylyxw4gtuvouh6jspxs` FOREIGN KEY (`vehicle_type_id`) REFERENCES `vehicle_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emergency_requests`
--

LOCK TABLES `emergency_requests` WRITE;
/*!40000 ALTER TABLE `emergency_requests` DISABLE KEYS */;
/*!40000 ALTER TABLE `emergency_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorites`
--

DROP TABLE IF EXISTS `favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorites` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `added_at` datetime(6) DEFAULT NULL,
  `garage_id` bigint DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9y9iy9hmq85dp9jm60fw4ippo` (`garage_id`),
  KEY `FKk7du8b8ewipawnnpg76d55fus` (`user_id`),
  CONSTRAINT `FK9y9iy9hmq85dp9jm60fw4ippo` FOREIGN KEY (`garage_id`) REFERENCES `garages` (`id`),
  CONSTRAINT `FKk7du8b8ewipawnnpg76d55fus` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorites`
--

LOCK TABLES `favorites` WRITE;
/*!40000 ALTER TABLE `favorites` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garage_service_types`
--

DROP TABLE IF EXISTS `garage_service_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garage_service_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garage_service_types`
--

LOCK TABLES `garage_service_types` WRITE;
/*!40000 ALTER TABLE `garage_service_types` DISABLE KEYS */;
INSERT INTO `garage_service_types` VALUES (1,'Dịch vụ bảo trì định kỳ cho xe',_binary '','Bảo trì'),(2,'Sửa chữa các hư hỏng cơ bản và nâng cao',_binary '','Sửa chữa'),(3,'Dịch vụ cứu hộ xe khẩn cấp 24/7',_binary '','Cứu hộ'),(4,'Thay thế linh kiện, phụ tùng chính hãng',_binary '','Thay thế phụ tùng'),(5,'Dịch vụ rửa xe tiêu chuẩn và cao cấp',_binary '','Rửa xe');
/*!40000 ALTER TABLE `garage_service_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garage_services`
--

DROP TABLE IF EXISTS `garage_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garage_services` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `base_price` decimal(38,2) DEFAULT NULL,
  `estimated_time_minutes` int DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `garage_id` bigint DEFAULT NULL,
  `service_type_id` bigint DEFAULT NULL,
  `service_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKopqv8589lcxm77g7bpexape96` (`garage_id`,`service_id`),
  KEY `FKjfgaixx1iu6f5kkvdb1305ils` (`service_type_id`),
  KEY `FKjge5s26k0v163eoh033kao21w` (`service_id`),
  CONSTRAINT `FK9hkjue81arnc0rjmg9nssekan` FOREIGN KEY (`garage_id`) REFERENCES `garages` (`id`),
  CONSTRAINT `FKjfgaixx1iu6f5kkvdb1305ils` FOREIGN KEY (`service_type_id`) REFERENCES `garage_service_types` (`id`),
  CONSTRAINT `FKjge5s26k0v163eoh033kao21w` FOREIGN KEY (`service_id`) REFERENCES `services` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garage_services`
--

LOCK TABLES `garage_services` WRITE;
/*!40000 ALTER TABLE `garage_services` DISABLE KEYS */;
INSERT INTO `garage_services` VALUES (1,120000.00,45,_binary '',1,1,1),(2,150000.00,60,_binary '',1,1,2),(3,200000.00,50,_binary '',1,2,3),(4,130000.00,40,_binary '',2,1,1),(5,170000.00,55,_binary '',2,2,4),(6,220000.00,70,_binary '',2,3,5),(7,160000.00,60,_binary '',3,1,2),(8,190000.00,50,_binary '',3,2,3),(9,250000.00,75,_binary '',3,3,6),(10,120000.00,45,_binary '',4,1,1),(11,180000.00,60,_binary '',4,4,7),(12,300000.00,90,_binary '',4,5,8),(15,NULL,NULL,_binary '',12,NULL,1),(16,NULL,NULL,_binary '',12,NULL,3),(22,NULL,NULL,_binary '',10,NULL,3),(23,NULL,NULL,_binary '',13,NULL,4),(24,NULL,NULL,_binary '',13,NULL,5);
/*!40000 ALTER TABLE `garage_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garage_subscriptions`
--

DROP TABLE IF EXISTS `garage_subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garage_subscriptions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `end_date` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `garage_id` bigint DEFAULT NULL,
  `package_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoqpaapsf62j4y3hffn5oah15i` (`garage_id`),
  KEY `FK8orvie8t9jyahahgb4bel21y0` (`package_id`),
  CONSTRAINT `FK8orvie8t9jyahahgb4bel21y0` FOREIGN KEY (`package_id`) REFERENCES `subscriptions` (`id`),
  CONSTRAINT `FKoqpaapsf62j4y3hffn5oah15i` FOREIGN KEY (`garage_id`) REFERENCES `garages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garage_subscriptions`
--

LOCK TABLES `garage_subscriptions` WRITE;
/*!40000 ALTER TABLE `garage_subscriptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `garage_subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garage_vehicle_types`
--

DROP TABLE IF EXISTS `garage_vehicle_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garage_vehicle_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_active` bit(1) DEFAULT NULL,
  `garage_id` bigint DEFAULT NULL,
  `vehicle_type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhbhoiiqyytvf9t1ybm5ywcek` (`garage_id`,`vehicle_type_id`),
  KEY `FKe9v3d4fdtw3lx0d5ukh5xqndl` (`vehicle_type_id`),
  CONSTRAINT `FKe9v3d4fdtw3lx0d5ukh5xqndl` FOREIGN KEY (`vehicle_type_id`) REFERENCES `vehicle_types` (`id`),
  CONSTRAINT `FKo58wt6ovhut4xa6o67cyhc1qh` FOREIGN KEY (`garage_id`) REFERENCES `garages` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garage_vehicle_types`
--

LOCK TABLES `garage_vehicle_types` WRITE;
/*!40000 ALTER TABLE `garage_vehicle_types` DISABLE KEYS */;
INSERT INTO `garage_vehicle_types` VALUES (3,_binary '',12,7),(4,_binary '',12,8),(7,_binary '',10,4),(8,_binary '',1,1),(9,_binary '',13,5);
/*!40000 ALTER TABLE `garage_vehicle_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garages`
--

DROP TABLE IF EXISTS `garages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `close_time` time(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_verified` bit(1) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `open_time` time(6) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','PENDING') DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnkxkyoc52o4qwft37o2l29x2x` (`user_id`),
  CONSTRAINT `FKnkxkyoc52o4qwft37o2l29x2x` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garages`
--

LOCK TABLES `garages` WRITE;
/*!40000 ALTER TABLE `garages` DISABLE KEYS */;
INSERT INTO `garages` VALUES (1,'101 Lê Lợi, Quận 1, TP.HCM','18:00:00.000000','2025-07-31 22:01:38.350581','Garage chuyên sửa xe tay ga và ô tô.','leloi@gmail.com','https://example.com/garage3.jpg',_binary '',10.775,106.7,'Garage Lê Lợi','08:00:00.000000','0903001001','ACTIVE',3),(2,'55 Nguyễn Văn Cừ, Quận 5, TP.HCM','19:00:00.000000','2025-07-31 22:01:38.350581','Dịch vụ bảo trì nhanh và thân thiện.','garage10@example.com','https://example.com/garage10.jpg',_binary '\0',10.755,106.66,'Garage Văn Cừ','07:30:00.000000','0903001002','PENDING',8),(3,'240 Hai Bà Trưng, Quận 3, TP.HCM','17:30:00.000000','2025-07-31 22:01:38.350581','Chuyên sửa chữa xe doanh nghiệp.','garage11@example.com','https://example.com/garage11.jpg',_binary '',10.785,106.688,'Garage Hai Bà Trưng','08:15:00.000000','0903001003','ACTIVE',9),(4,'12 Nguyễn Thị Minh Khai, Quận 1, TP.HCM','18:30:00.000000','2025-07-31 22:01:38.350581','Garage có dịch vụ giao nhận tận nơi.','garage12@example.com','https://example.com/garage12.jpg',_binary '',10.773,106.693,'Garage Minh Khai','08:00:00.000000','0903001004','ACTIVE',10),(5,'32 Dương Bá Trạc, Quận 8, TP.HCM','18:45:00.000000','2025-07-31 22:01:38.350581','Thay nhớt, rửa xe, kiểm tra tổng quát.','garage13@example.com','https://example.com/garage13.jpg',_binary '\0',10.735,106.682,'Garage Dương Bá Trạc','07:45:00.000000','0903001005','ACTIVE',11),(6,'188 Trường Chinh, Tân Bình, TP.HCM','17:00:00.000000','2025-07-31 22:01:38.350581','Dịch vụ nhanh, chuyên nghiệp.','garage14@example.com','https://example.com/garage14.jpg',_binary '',10.803,106.63,'Garage Trường Chinh','08:30:00.000000','0903001006','ACTIVE',12),(7,'90 Lũy Bán Bích, Tân Phú, TP.HCM','18:15:00.000000','2025-07-31 22:01:38.350581','Chuyên điện - máy - điều hòa xe.','garage15@example.com','https://example.com/garage15.jpg',_binary '',10.786,106.62,'Garage Lũy Bán Bích','08:00:00.000000','0903001007','ACTIVE',13),(8,'290 Nguyễn Oanh, Gò Vấp, TP.HCM','18:00:00.000000','2025-07-31 22:01:38.350581','Sửa chữa đa dạng dòng xe.','garage16@example.com','https://example.com/garage16.jpg',_binary '\0',10.832,106.673,'Garage Nguyễn Oanh','07:30:00.000000','0903001008','ACTIVE',14),(9,'48 Phan Xích Long, Phú Nhuận, TP.HCM','19:00:00.000000','2025-07-31 22:01:38.350581','Dịch vụ sửa chữa và rửa xe cao cấp.','garage17@example.com','https://example.com/garage17.jpg',_binary '',10.797,106.684,'Garage Phan Xích Long','08:00:00.000000','0903001009','ACTIVE',15),(10,'123 Nguyễn Ảnh Thủ, Quận 12, TP.HCM','18:00:00.000000','2025-07-31 22:01:38.350581','Chuyên thay thế linh kiện chính hãng.','garage18@example.com','https://example.com/garage18.jpg',_binary '\0',10.86,106.66,'Garage Nguyễn Ảnh Thủ','08:00:00.000000','0903001010','ACTIVE',16),(12,'Dong Da','21:00:00.000000','2025-08-07 10:30:59.062074','Garage ABC','abc@gmail.com','https://example.com/garage18.jpg',_binary '\0',12,32,'Garage ABC','07:00:00.000000','0947134196','PENDING',NULL),(13,'Hà Nội','19:00:00.000000','2025-08-07 14:02:24.235323','','miclebim12@gmail.com',NULL,_binary '\0',13,32,'Gara Phạm Văn Đồng','07:00:00.000000','0947134111','ACTIVE',NULL);
/*!40000 ALTER TABLE `garages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `recipient_id` bigint DEFAULT NULL,
  `recipient_type` enum('GARAGE','USER') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotions`
--

DROP TABLE IF EXISTS `promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `discount_percent` decimal(38,2) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotions`
--

LOCK TABLES `promotions` WRITE;
/*!40000 ALTER TABLE `promotions` DISABLE KEYS */;
/*!40000 ALTER TABLE `promotions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair_results`
--

DROP TABLE IF EXISTS `repair_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repair_results` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `repair_time` datetime(6) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `total_cost` decimal(38,2) DEFAULT NULL,
  `appointment_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKcddxmo0akj53y4xbysp5u1q40` (`appointment_id`),
  CONSTRAINT `FKj8ibg5x35fao9ajd9chtpx8t5` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`appointment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair_results`
--

LOCK TABLES `repair_results` WRITE;
/*!40000 ALTER TABLE `repair_results` DISABLE KEYS */;
/*!40000 ALTER TABLE `repair_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `comment` text,
  `created_at` datetime(6) DEFAULT NULL,
  `rating` int NOT NULL,
  `appointment_id` int NOT NULL,
  `garage_id` bigint DEFAULT NULL,
  `user_id` int NOT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`review_id`),
  KEY `FKfhaj6kqx2pjpn6eambt0pa1nm` (`appointment_id`),
  KEY `FK9bye5gjgtcwj15hceklasnsjk` (`garage_id`),
  KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
  CONSTRAINT `FK9bye5gjgtcwj15hceklasnsjk` FOREIGN KEY (`garage_id`) REFERENCES `garages` (`id`),
  CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKfhaj6kqx2pjpn6eambt0pa1nm` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`appointment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,'Sửa ok','2025-07-07 00:00:00.000000',5,1,1,1,0);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reward_points`
--

DROP TABLE IF EXISTS `reward_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reward_points` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `awarded_at` datetime(6) DEFAULT NULL,
  `points` int DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKovyxai0u4f1lfi8brc9kovumt` (`user_id`),
  CONSTRAINT `FKovyxai0u4f1lfi8brc9kovumt` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reward_points`
--

LOCK TABLES `reward_points` WRITE;
/*!40000 ALTER TABLE `reward_points` DISABLE KEYS */;
/*!40000 ALTER TABLE `reward_points` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `services`
--

DROP TABLE IF EXISTS `services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `services` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services`
--

LOCK TABLES `services` WRITE;
/*!40000 ALTER TABLE `services` DISABLE KEYS */;
INSERT INTO `services` VALUES (1,'Sửa chữa lốp xe',_binary '','Sửa lốp'),(2,'Thay dầu động cơ',_binary '','Thay dầu'),(3,'Thay nhớt hộp số tự động hoặc số sàn',_binary '','Thay nhớt hộp số'),(4,'Kiểm tra và bảo dưỡng hệ thống phanh',_binary '','Kiểm tra phanh'),(5,'Dịch vụ cứu hộ khi xe gặp sự cố',_binary '','Cứu hộ xe'),(6,'Sửa chữa và bảo trì hệ thống điều hòa',_binary '','Sửa điều hòa'),(7,'Thay thế bình ắc quy mới',_binary '','Thay ắc quy'),(8,'Rửa xe cơ bản và chuyên sâu',_binary '','Rửa xe'),(9,'Sơn lại xe, xử lý trầy xước',_binary '','Sơn xe'),(10,'Cân bằng và chỉnh góc đặt bánh xe',_binary '','Cân chỉnh góc lái'),(11,'Kiểm tra và chẩn đoán lỗi động cơ',_binary '','Kiểm tra động cơ'),(12,'Thay thế bugi đánh lửa động cơ',_binary '','Thay bugi');
/*!40000 ALTER TABLE `services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscriptions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `duration_days` int DEFAULT NULL,
  `features` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriptions`
--

LOCK TABLES `subscriptions` WRITE;
/*!40000 ALTER TABLE `subscriptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_contents`
--

DROP TABLE IF EXISTS `system_contents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_contents` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `content_type` enum('BANNER','GUIDE','NEWS','POLICY') DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_contents`
--

LOCK TABLES `system_contents` WRITE;
/*!40000 ALTER TABLE `system_contents` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_contents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_vehicle_types`
--

DROP TABLE IF EXISTS `user_vehicle_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_vehicle_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) DEFAULT NULL,
  `license_plate` varchar(50) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `category_id` int NOT NULL,
  `user_id` int NOT NULL,
  `vehicle_type_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrvsesjqu46qjt4upg9xvkf14y` (`category_id`),
  KEY `FKn8bkekh0dj1ejig9b7myw4spc` (`user_id`),
  KEY `FKeo34d7hbsbo07hksg77fvca6e` (`vehicle_type_id`),
  CONSTRAINT `FKeo34d7hbsbo07hksg77fvca6e` FOREIGN KEY (`vehicle_type_id`) REFERENCES `vehicle_types` (`id`),
  CONSTRAINT `FKn8bkekh0dj1ejig9b7myw4spc` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKrvsesjqu46qjt4upg9xvkf14y` FOREIGN KEY (`category_id`) REFERENCES `user_vehicle_types_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_vehicle_types`
--

LOCK TABLES `user_vehicle_types` WRITE;
/*!40000 ALTER TABLE `user_vehicle_types` DISABLE KEYS */;
INSERT INTO `user_vehicle_types` VALUES (1,'Toyota','51A-12345','Fortuner',2020,1,1,2),(2,'Hyundai','29B-67890','SantaFe',2021,2,2,2),(3,'Kia','30A-45678','Morning',2019,3,3,2),(4,'Ford','51C-98765','Ranger',2022,2,1,3),(5,'Mazda','60B-13579','CX-5',2020,1,2,2),(6,'Honda','43A-24680','Civic',2018,3,3,2);
/*!40000 ALTER TABLE `user_vehicle_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_vehicle_types_category`
--

DROP TABLE IF EXISTS `user_vehicle_types_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_vehicle_types_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` text,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlpma7e3dd0vqnuhl784doh77r` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_vehicle_types_category`
--

LOCK TABLES `user_vehicle_types_category` WRITE;
/*!40000 ALTER TABLE `user_vehicle_types_category` DISABLE KEYS */;
INSERT INTO `user_vehicle_types_category` VALUES (1,'xe gia đình','Home'),(2,'xe công ty','Public'),(3,'xe cá nhân','Personal');
/*!40000 ALTER TABLE `user_vehicle_types_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `email` varchar(100) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `account_type` enum('admin','garage','user') NOT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'hồ chí minh','2025-06-27 04:30:23','user@gmail.com','uploads/user1.png',NULL,NULL,'Nguyen Văn A','$2a$10$ejTGfnXSvq2YHaqRqL8y1e0ACaMigMad2FtZ6T.VaUS26g2HhCNv2','1234567892','user',1),(2,'hồ chí minh','2025-07-27 07:26:01','admin@gmail.com',NULL,NULL,NULL,'admin','$2a$10$lqyVJZtrTVb8nIzrUNWCQODKF6QIY0KqbPLNzgNG6KsJLq56vRoNa','123456','admin',1),(3,'hồ chí minh','2025-07-27 07:26:55','garage@gmail.com',NULL,NULL,NULL,'garage','$2a$10$O9pHRFgta9dNOFzCLGatqeLtw7FXHIo2KiFUg3NXBWAjSOEabBbO.','123456222333','garage',1),(4,'Hà Nội','2025-07-28 15:44:15','user2@gmail.com',NULL,NULL,NULL,'Trần Thị B','$2a$10$4eT9fQMumlgsl.giiDGNz./TMDzo11LeKDlM9.ge/8rGDWjUubcJG','0900000002','user',1),(5,'Đà Nẵng','2025-07-28 15:45:37','user3@gmail.com',NULL,NULL,NULL,'Lê Văn C','$2a$10$PwZDyPSy1DSLFq9d1JrXHeUhKtkxM8ydWqsKdAfHGO/gaU6ZH7TRe','0900000003','user',1),(6,'Cần Thơ','2025-07-28 15:45:46','user4@gmail.com',NULL,NULL,NULL,'Phạm Thị Diên','$2a$10$mM6ASixmTeuq3yd4G5tFx.bZXGoEe6UivB028qJS1jCRJERHZV5Ri','0900000004','user',1),(7,'Biên Hòa','2025-07-28 15:45:53','user5@gmail.com',NULL,NULL,NULL,'Hoàng Văn E','$2a$10$LeIQI2Xokk/wZ74GabaM.uTc3dZjC3D/7oA8d/HXuag6Ljm8RjTo.','0900000005','user',1),(8,'Vũng Tàu','2025-07-28 15:46:05','user6@gmail.com',NULL,NULL,NULL,'Đặng Thị F','$2a$10$dYqn/YhfsgXC7QQO96oW1ekiQQ4bF.vADck2VlgHHQHNVJPLQqFB6','0900000006','garage',1),(9,'Huế','2025-07-28 15:46:12','user7@gmail.com',NULL,NULL,NULL,'Bùi Văn G','$2a$10$enoYwyOen30va3EkhwQnNubJmjUNQzVyinrVLZArztRyuSTTHh5MO','0900000007','garage',1),(10,'Hải Phòng','2025-07-28 15:46:18','user8@gmail.com',NULL,NULL,NULL,'Vũ Thị H','$2a$10$wV3H6aJCUlY9KfnDgrzQIOaXmIL/czMTyLpJ0Pp.w8cyNOfl942p.','0900000008','garage',1),(11,'Nha Trang','2025-07-28 15:46:26','user9@gmail.com',NULL,NULL,NULL,'Mai Văn I','$2a$10$yOpCkEQ2mfgVY9B7F6/gO.ORPNi2QihnjX47IVJIkS23O6HgP81Z.','0900000009','garage',1),(12,'Đà Lạt','2025-07-28 15:46:32','user10@gmail.com',NULL,NULL,NULL,'Ngô Thị Jack','$2a$10$OwIuR55RopBw6XD9sWRQh.3YhE1DfJlADM6OZwHqGYgi3aJT.j36i','0900000010','garage',1),(13,'Thanh Hóa','2025-07-28 15:46:39','user11@gmail.com',NULL,NULL,NULL,'Phan Văn K','$2a$10$csEtmnOev2CAqm0jttsiIOXvUFNhBZubXAI50NIs8zKITCgsugtvO','0900000011','garage',1),(14,'Quảng Ninh','2025-07-28 15:46:45','user12@gmail.com',NULL,NULL,NULL,'Đỗ Thị L','$2a$10$Kqsh2aBL4UzPOWsWhygyXOIrjZu.rWQouuzeOAmCLrrz7nYFgBwC.','0900000012','garage',1),(15,'Bắc Giang','2025-07-28 15:46:57','user13@gmail.com',NULL,NULL,NULL,'Trịnh Văn M','$2a$10$38t.SvMEsj9IP97MAOGEiu9e/2hsyg44sf4s4ncEXjhLt6ej5Z0bm','0900000013','garage',1),(16,'Nam Định','2025-07-28 15:47:06','user14@gmail.com',NULL,NULL,NULL,'Lương Thị N','$2a$10$N0Q585lL0vYWF7DMxnoy7ewojFO.F.jIdMOKD8pyTSYJXII44wqN2','0900000014','garage',1),(17,'Dong Da','2025-08-04 08:01:09','nta@gmail.com',NULL,NULL,NULL,'Nguyễn Thuỳ Trang','$2a$10$28nUoawOTegvaVtLt2M.Wui./kNrRMgxffvLIbgKPW6yjzHCtBZu6','0947134196','user',1),(18,'HN','2025-08-04 08:16:03','thaovy@gmail.com',NULL,NULL,NULL,'ThaoVy123','$2a$10$jqEuXhEsh1BNEwUNMgFn6.YVE/XSHQID/Z11EMj4w7U7MFHg2f34e','0987666111','user',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_types`
--

DROP TABLE IF EXISTS `vehicle_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_types`
--

LOCK TABLES `vehicle_types` WRITE;
/*!40000 ALTER TABLE `vehicle_types` DISABLE KEYS */;
INSERT INTO `vehicle_types` VALUES (1,'Xe máy phổ thông số: Wave, Sirius, Dream…',_binary '','Xe máy số'),(2,'Xe tay ga: Vision, Air Blade, Lead…',_binary '','Xe tay ga'),(3,'Xe thể thao côn tay: Exciter, Winner, Raider…',_binary '','Xe côn tay'),(4,'Xe mô tô phân khối lớn (PKL)',_binary '','Xe mô tô PKL'),(5,'Ô tô con 4 – 7 chỗ',_binary '','Ô tô con'),(6,'Xe bán tải – Pickup',_binary '','Xe bán tải'),(7,'Xe khách 9 – 16 chỗ: Ford Transit, Solati…',_binary '','Xe khách nhỏ'),(8,'Xe khách 29 – 45 chỗ: Thaco, Samco…',_binary '','Xe khách lớn'),(9,'Xe tải dưới 1 tấn',_binary '','Xe tải nhỏ'),(10,'Xe tải từ 1 đến 3.5 tấn',_binary '','Xe tải trung'),(11,'Xe tải từ 5 đến 15 tấn trở lên',_binary '','Xe tải lớn'),(12,'Xe cứu thương chuyên dụng',_binary '','Xe cứu thương'),(13,'Xe cẩu, xe ben, xe bồn, container, đầu kéo',_binary '','Xe chuyên dụng khác'),(14,'Xe máy điện: VinFast Klara, Yadea…',_binary '','Xe máy điện'),(15,'Ô tô điện: VinFast VF e34, Tesla…',_binary '','Ô tô điện');
/*!40000 ALTER TABLE `vehicle_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'hk4_project'
--

--
-- Dumping routines for database 'hk4_project'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-07 14:04:02
