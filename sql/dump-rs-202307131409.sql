-- MySQL dump 10.13  Distrib 5.7.42, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: rs
-- ------------------------------------------------------
-- Server version	5.7.40-0ubuntu0.18.04.1

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
-- Table structure for table `menus`
--

DROP TABLE IF EXISTS `menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menus` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` char(1) DEFAULT NULL COMMENT 'P=Private,O=Open',
  `admin` char(1) DEFAULT NULL COMMENT 'T=true,F=false',
  `secure` int(11) DEFAULT NULL COMMENT '0=Public menu,1=System,2=Admin/System,3=Everyone',
  `root` varchar(500) DEFAULT NULL,
  `link` varchar(200) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menus`
--

LOCK TABLES `menus` WRITE;
/*!40000 ALTER TABLE `menus` DISABLE KEYS */;
INSERT INTO `menus` VALUES (1,'P','T',2,'scr/sk/handlers/admin/','/admin/menus','Menus'),(2,'P','T',2,'scr/sk/handlers/admin/','/admin/users','Usuarios'),(3,'P','T',2,'src/sk/handlers/admin/','/admin/pincludes','Private includes'),(4,'P','T',2,'src/sk/handlers/admin/','/admin/proutes','Private Routes'),(5,'P','T',2,'src/sk/handlers/admin/','/admin/rincludes','Open Includes'),(6,'P','T',2,'src/sk/handlers/admin/','/admin/routes','Open Routes'),(7,'P','T',1,'src/sk/handlers/admin/','/admin/tipo_creditos','Tipo_creditos'),(8,'P','T',1,'src/sk/handlers/admin/','/admin/zonas','Zonas'),(9,'P','T',1,'src/sk/handlers/admin/','/admin/constructoras','Constructoras'),(10,'P','T',1,'src/sk/handlers/admin/','/admin/fraccionamientos','Fraccionamientos'),(11,'P','T',1,'src/sk/handlers/admin/','/admin/casas','Casas'),(14,'P','T',1,'src/sk/handlers/admin/','/admin/clientes','Clientes'),(15,'P','F',3,'src/sk/handlers/','/clientes','Clientes');
/*!40000 ALTER TABLE `menus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `routes`
--

DROP TABLE IF EXISTS `routes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `routes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dt` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `routes`
--

LOCK TABLES `routes` WRITE;
/*!40000 ALTER TABLE `routes` DISABLE KEYS */;
INSERT INTO `routes` VALUES (1,'(GET \"/table_ref/get_users\" [] (generate-string (table_ref/get-users)))'),(2,'(GET \"/table_ref/validate_email/:email\" [email] (generate-string (table_ref/get-users-email email)))'),(3,'(GET \"/table_ref/months\" [] (generate-string (table_ref/months)))'),(4,'(GET \"/table_ref/years/:pyears/:nyears\" [pyears nyears] (generate-string (table_ref/years pyears nyears)))'),(5,'(GET \"/table_ref/get-item/:table/:field/:fname/:fval\" [table field fname fval] (table_ref/get-item table field fname fval))'),(6,'(GET \"/table_ref/get-time\" [] (generate-string (table_ref/build-time)))'),(7,'(GET \"/table_ref/levels\" [] (generate-string (table_ref/level-options)))'),(8,'(GET \"/table_ref/get-titulo/:id\" [id] (table_ref/get-titulo id))'),(9,'(GET \"/table_ref/get-titulos\" [] (generate-string (table_ref/get-titulos)))'),(10,'(GET \"/table_ref/get-paises\" [] (generate-string (table_ref/get-pais)))'),(11,'(GET \"/table_ref/get-pais/:id\" [id] (table_ref/get-pais-id id))'),(12,'(GET \"/\" req [] (home/main req))'),(13,'(GET \"/home/login\" req [] (home/login req))'),(14,'(POST \"/home/login\" [username password] (home/login! username password))'),(15,'(GET \"/home/logoff\" [] (home/logoff))'),(16,'(GET \"/register\" req [] (registrar/registrar req))'),(17,'(POST \"/register\" req [] (registrar/registrar! req))'),(18,'(GET \"/rpaswd\" req [] (registrar/reset-password req))'),(19,'(POST \"/rpaswd\" req [] (registrar/reset-password! req))'),(20,'(GET \"/reset_password/:token\" [token] (registrar/reset-jwt token))'),(21,'(POST \"/reset_password\" req [] (registrar/reset-jwt! req))'),(22,'(GET \"/table_ref/get_constructoras\" [] (generate-string (table_ref/get-constructoras)))'),(23,'(GET \"/table_ref/get_zonas\" [] (generate-string (table_ref/get-zonas)))'),(24,'(GET \"/table_ref/get_tipo_creditos\" [] (generate-string (table_ref/get-tipo-creditos)))'),(25,'(GET \"/table_ref/get_fraccionamientos\" [] (generate-string (table_ref/get-fraccionamientos)))'),(26,'(GET \"/table_ref/get_tipos\" [] (generate-string (table_ref/tipo_casas)))');
/*!40000 ALTER TABLE `routes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rincludes`
--

DROP TABLE IF EXISTS `rincludes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rincludes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dt` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rincludes`
--

LOCK TABLES `rincludes` WRITE;
/*!40000 ALTER TABLE `rincludes` DISABLE KEYS */;
INSERT INTO `rincludes` VALUES (1,'[cheshire.core :refer [generate-string]]'),(2,'[compojure.core :refer [defroutes GET POST]]'),(3,'[sk.handlers.home.handler :as home]'),(4,'[sk.handlers.registrar.handler :as registrar]'),(5,'[sk.handlers.tref.handler :as table_ref]');
/*!40000 ALTER TABLE `rincludes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proutes`
--

DROP TABLE IF EXISTS `proutes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proutes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dt` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proutes`
--

LOCK TABLES `proutes` WRITE;
/*!40000 ALTER TABLE `proutes` DISABLE KEYS */;
INSERT INTO `proutes` VALUES (1,'(GET \"/admin/menus\" req [] (menus/menus req))'),(2,'(POST \"/admin/menus\" req [] (menus/menus-grid req))'),(3,'(GET \"/admin/menus/edit/:id\" [id] (menus/menus-form id))'),(4,'(POST \"/admin/menus/save\" req [] (menus/menus-save req))'),(5,'(POST \"/admin/menus/delete\" req [] (menus/menus-delete req))'),(6,'(GET \"/admin/users\" req [] (users/users req))'),(7,'(POST \"/admin/users\" req [] (users/users-grid req))'),(8,'(GET \"/admin/users/edit/:id\" [id] (users/users-form id))'),(9,'(POST \"/admin/users/save\" req [] (users/users-save req))'),(10,'(POST \"/admin/users/delete\" req [] (users/users-delete req))'),(11,'(GET \"/admin/pincludes\" req [] (pincludes/pincludes req))'),(12,'(POST \"/admin/pincludes\" req [] (pincludes/pincludes-grid req))'),(13,'(GET \"/admin/pincludes/edit/:id\" [id] (pincludes/pincludes-form id))'),(14,'(POST \"/admin/pincludes/save\" req [] (pincludes/pincludes-save req))'),(15,'(POST \"/admin/pincludes/delete\" req [] (pincludes/pincludes-delete req))'),(16,'(GET \"/admin/proutes\" req [] (proutes/proutes req))'),(17,'(POST \"/admin/proutes\" req [] (proutes/proutes-grid req))'),(18,'(GET \"/admin/proutes/edit/:id\" [id] (proutes/proutes-form id))'),(19,'(POST \"/admin/proutes/save\" req [] (proutes/proutes-save req))'),(20,'(POST \"/admin/proutes/delete\" req [] (proutes/proutes-delete req))'),(21,'(GET \"/admin/rincludes\" req [] (rincludes/rincludes req))'),(22,'(POST \"/admin/rincludes\" req [] (rincludes/rincludes-grid req))'),(23,'(GET \"/admin/rincludes/edit/:id\" [id] (rincludes/rincludes-form id))'),(24,'(POST \"/admin/rincludes/save\" req [] (rincludes/rincludes-save req))'),(25,'(POST \"/admin/rincludes/delete\" req [] (rincludes/rincludes-delete req))'),(26,'(GET \"/admin/routes\" req [] (routes/routes req))'),(27,'(POST \"/admin/routes\" req [] (routes/routes-grid req))'),(28,'(GET \"/admin/routes/edit/:id\" [id] (routes/routes-form id))'),(29,'(POST \"/admin/routes/save\" req [] (routes/routes-save req))'),(30,'(POST \"/admin/routes/delete\" req [] (routes/routes-delete req))'),(31,'(GET \"/admin/tipo_creditos\" req [] (tipo_creditos/tipo_creditos req))'),(32,'(POST \"/admin/tipo_creditos\" req [] (tipo_creditos/tipo_creditos-grid req))'),(33,'(GET \"/admin/tipo_creditos/edit/:id\" [id] (tipo_creditos/tipo_creditos-form id))'),(34,'(POST \"/admin/tipo_creditos/save\" req [] (tipo_creditos/tipo_creditos-save req))'),(35,'(POST \"/admin/tipo_creditos/delete\" req [] (tipo_creditos/tipo_creditos-delete req))'),(36,'(GET \"/admin/zonas\" req [] (zonas/zonas req))'),(37,'(POST \"/admin/zonas\" req [] (zonas/zonas-grid req))'),(38,'(GET \"/admin/zonas/edit/:id\" [id] (zonas/zonas-form id))'),(39,'(POST \"/admin/zonas/save\" req [] (zonas/zonas-save req))'),(40,'(POST \"/admin/zonas/delete\" req [] (zonas/zonas-delete req))'),(41,'(GET \"/admin/constructoras\" req [] (constructoras/constructoras req))'),(42,'(POST \"/admin/constructoras\" req [] (constructoras/constructoras-grid req))'),(43,'(GET \"/admin/constructoras/edit/:id\" [id] (constructoras/constructoras-form id))'),(44,'(POST \"/admin/constructoras/save\" req [] (constructoras/constructoras-save req))'),(45,'(POST \"/admin/constructoras/delete\" req [] (constructoras/constructoras-delete req))'),(46,'(GET \"/admin/fraccionamientos\" req [] (fraccionamientos/fraccionamientos req))'),(47,'(POST \"/admin/fraccionamientos\" req [] (fraccionamientos/fraccionamientos-grid req))'),(48,'(GET \"/admin/fraccionamientos/edit/:id\" [id] (fraccionamientos/fraccionamientos-form id))'),(49,'(POST \"/admin/fraccionamientos/save\" req [] (fraccionamientos/fraccionamientos-save req))'),(50,'(POST \"/admin/fraccionamientos/delete\" req [] (fraccionamientos/fraccionamientos-delete req))'),(51,'(GET \"/admin/casas\" req [] (casas/casas req))'),(52,'(POST \"/admin/casas\" req [] (casas/casas-grid req))'),(53,'(GET \"/admin/casas/edit/:id\" [id] (casas/casas-form id))'),(54,'(POST \"/admin/casas/save\" req [] (casas/casas-save req))'),(55,'(POST \"/admin/casas/delete\" req [] (casas/casas-delete req))'),(66,'(GET \"/admin/clientes\" req [] (clientes/clientes req))'),(67,'(POST \"/admin/clientes\" req [] (clientes/clientes-grid req))'),(68,'(GET \"/admin/clientes/edit/:id\" [id] (clientes/clientes-form id))'),(69,'(POST \"/admin/clientes/save\" req [] (clientes/clientes-save req))'),(70,'(POST \"/admin/clientes/delete\" req [] (clientes/clientes-delete req))'),(71,'(GET \"/contactos\" req [] (clientes-output/clientes req))'),(72,'(GET \"/contactos/reporte\" req [] (clientes-output/clientes-reporte req))'),(73,'(GET \"/contactos/pdf\" req [] (clientes-output/clientes-pdf req))'),(74,'(GET \"/contactos/csv\" req [] (clientes-output/clientes-csv req))');
/*!40000 ALTER TABLE `proutes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pincludes`
--

DROP TABLE IF EXISTS `pincludes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pincludes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dt` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pincludes`
--

LOCK TABLES `pincludes` WRITE;
/*!40000 ALTER TABLE `pincludes` DISABLE KEYS */;
INSERT INTO `pincludes` VALUES (1,'[compojure.core :refer [defroutes GET POST]]'),(2,'[sk.handlers.admin.menus.handler :as menus]'),(3,'[sk.handlers.admin.users.handler :as users]'),(4,'[sk.handlers.admin.pincludes.handler :as pincludes]'),(5,'[sk.handlers.admin.proutes.handler :as proutes]'),(6,'[sk.handlers.admin.rincludes.handler :as rincludes]'),(7,'[sk.handlers.admin.routes.handler :as routes]'),(8,'[sk.handlers.admin.tipo_creditos.handler :as tipo_creditos]'),(9,'[sk.handlers.admin.zonas.handler :as zonas]'),(10,'[sk.handlers.admin.constructoras.handler :as constructoras]'),(11,'[sk.handlers.admin.fraccionamientos.handler :as fraccionamientos]'),(12,'[sk.handlers.admin.casas.handler :as casas]'),(15,'[sk.handlers.admin.clientes.handler :as clientes]'),(16,'[sk.handlers.clientes.handler :as clientes-output]');
/*!40000 ALTER TABLE `pincludes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-13 14:09:32
