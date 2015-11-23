-- phpMyAdmin SQL Dump
-- version 3.3.10.4
-- http://www.phpmyadmin.net
--
-- Host: mysql.dev.tomcastonzo.com
-- Generation Time: Nov 14, 2015 at 01:00 PM
-- Server version: 5.1.56
-- PHP Version: 5.6.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `cancer_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `state_map`
--

CREATE TABLE IF NOT EXISTS `state_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `state_id` int(11) NOT NULL,
  `state_code` varchar(2) NOT NULL,
  `state_description` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `state_code` (`state_code`),
  KEY `state_id` (`state_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=52 ;

--
-- Dumping data for table `state_map`
--

INSERT INTO `state_map` (`id`, `state_id`, `state_code`, `state_description`) VALUES
(1, 1, 'AL', 'Alabama'),
(2, 2, 'AK', 'Alaska'),
(3, 4, 'AZ', 'Arizona'),
(4, 5, 'AR', 'Arkansas'),
(5, 6, 'CA', 'California'),
(6, 8, 'CO', 'Colorado'),
(7, 9, 'CT', 'Connecticut'),
(8, 10, 'DE', 'Delaware'),
(9, 11, 'DC', 'District of Columbia'),
(10, 12, 'FL', 'Florida'),
(11, 13, 'GA', 'Georgia'),
(12, 15, 'HI', 'Hawaii'),
(13, 16, 'ID', 'Idaho'),
(14, 17, 'IL', 'Illinois'),
(15, 18, 'IN', 'Indiana'),
(16, 19, 'IA', 'Iowa'),
(17, 20, 'KS', 'Kansas'),
(18, 21, 'KY', 'Kentucky'),
(19, 22, 'LA', 'Louisiana'),
(20, 23, 'ME', 'Maine'),
(21, 24, 'MD', 'Maryland'),
(22, 25, 'MA', 'Massachusetts'),
(23, 26, 'MI', 'Michigan'),
(24, 27, 'MN', 'Minnesota'),
(25, 28, 'MS', 'Mississippi'),
(26, 29, 'MO', 'Missouri'),
(27, 30, 'MT', 'Montana'),
(28, 31, 'NE', 'Nebraska'),
(29, 32, 'NV', 'Nevada'),
(30, 33, 'NH', 'New Hampshire'),
(31, 34, 'NJ', 'New Jersey'),
(32, 35, 'NM', 'New Mexico'),
(33, 36, 'NY', 'New York'),
(34, 37, 'NC', 'North Carolina'),
(35, 38, 'ND', 'North Dakota'),
(36, 39, 'OH', 'Ohio'),
(37, 40, 'OK', 'Oklahoma'),
(38, 41, 'OR', 'Oregon'),
(39, 42, 'PA', 'Pennsylvania'),
(40, 44, 'RI', 'Rhode Island'),
(41, 45, 'SC', 'South Carolina'),
(42, 46, 'SD', 'South Dakota'),
(43, 47, 'TN', 'Tennessee'),
(44, 48, 'TX', 'Texas'),
(45, 49, 'UT', 'Utah'),
(46, 50, 'VT', 'Vermont'),
(47, 51, 'VA', 'Virginia'),
(48, 53, 'WA', 'Washington'),
(49, 54, 'WV', 'West Virginia'),
(50, 55, 'WI', 'Wisconsin'),
(51, 56, 'WY', 'Wyoming');
