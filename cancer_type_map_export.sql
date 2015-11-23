-- phpMyAdmin SQL Dump
-- version 3.3.10.4
-- http://www.phpmyadmin.net
--
-- Host: mysql.dev.tomcastonzo.com
-- Generation Time: Nov 14, 2015 at 12:59 PM
-- Server version: 5.1.56
-- PHP Version: 5.6.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `cancer_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `cancer_type_map`
--

CREATE TABLE IF NOT EXISTS `cancer_type_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_code` int(11) NOT NULL,
  `type_description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `type_code` (`type_code`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=24 ;

--
-- Dumping data for table `cancer_type_map`
--

INSERT INTO `cancer_type_map` (`id`, `type_code`, `type_description`) VALUES
(1, 1, 'All Cancer Sites'),
(2, 71, 'Bladder'),
(3, 76, 'Brain & ONS'),
(4, 55, 'Breast Female'),
(5, 400, 'Breast Female in situ'),
(6, 57, 'Cervix'),
(7, 516, 'Childhood Ages Less Than 15 All Sites'),
(8, 515, 'Childhood Ages Less Than 20 All Sites'),
(9, 20, 'Colon & Rectum'),
(10, 17, 'Esophagus'),
(11, 72, 'Kidney & Renal Pelvis'),
(12, 90, 'Leukemia'),
(13, 35, 'Liver & Bile Duct'),
(14, 47, 'Lung & Bronchus'),
(15, 53, 'Melanoma of the Skin'),
(16, 86, 'Non-Hodgkin Lymphoma'),
(17, 3, 'Oral Cavity & Pharynx'),
(18, 61, 'Ovary'),
(19, 40, 'Pancreas'),
(20, 66, 'Prostate'),
(21, 18, 'Stomach'),
(22, 80, 'Thyroid'),
(23, 58, 'Uterus');
