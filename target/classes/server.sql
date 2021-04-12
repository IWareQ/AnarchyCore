-- phpMyAdmin SQL Dump
-- version 4.6.6deb5ubuntu0.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Apr 04, 2021 at 09:51 PM
-- Server version: 5.7.33-0ubuntu0.18.04.1
-- PHP Version: 7.2.24-0ubuntu0.18.04.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `server`
--

-- --------------------------------------------------------

--
-- Table structure for table `Auth`
--

CREATE TABLE `Auth` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `DateReg` varchar(32) NOT NULL,
  `IpReg` varchar(32) NOT NULL,
  `DateLast` varchar(32) DEFAULT NULL,
  `IpLast` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Auth`
--
-- --------------------------------------------------------

--
-- Table structure for table `Bans`
--

CREATE TABLE `Bans` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `Reason` text,
  `Time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Bans`
--

-- --------------------------------------------------------

--
-- Table structure for table `ClanMembers`
--

CREATE TABLE `ClanMembers` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `Role` varchar(32) NOT NULL,
  `ClanID` int(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ClanMembers`
--

-- --------------------------------------------------------

--
-- Table structure for table `Clans`
--

CREATE TABLE `Clans` (
  `ID` int(16) NOT NULL,
  `ClanName` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Clans`
--
-- --------------------------------------------------------

--
-- Table structure for table `Homes`
--

CREATE TABLE `Homes` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `X` int(16) NOT NULL,
  `Y` int(16) NOT NULL,
  `Z` int(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Mutes`
--

CREATE TABLE `Mutes` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `Reason` text,
  `Time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RegionMembers`
--

CREATE TABLE `RegionMembers` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `RegionID` int(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Regions`
--

CREATE TABLE `Regions` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `Main_X` int(16) NOT NULL,
  `Main_Y` int(16) NOT NULL,
  `Main_Z` int(16) NOT NULL,
  `Pos1_X` int(16) NOT NULL,
  `Pos1_Y` int(16) NOT NULL,
  `Pos1_Z` int(16) NOT NULL,
  `Pos2_X` int(16) NOT NULL,
  `Pos2_Y` int(16) NOT NULL,
  `Pos2_Z` int(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Regions`
--
-------------------------

--
-- Table structure for table `RequestsClan`
--

CREATE TABLE `RequestsClan` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `ClanID` int(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RequestsPlayer`
--

CREATE TABLE `RequestsPlayer` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `ClanID` int(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Spectates`
--

CREATE TABLE `Spectates` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `Target` varchar(32) NOT NULL,
  `World` varchar(16) NOT NULL,
  `X` int(16) NOT NULL,
  `Y` int(16) NOT NULL,
  `Z` int(16) NOT NULL,
  `namedTag` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `ID` int(16) NOT NULL,
  `Name` varchar(32) NOT NULL,
  `XboxID` varchar(32) NOT NULL,
  `Permission` varchar(16) NOT NULL DEFAULT 'player',
  `Money` double(32,2) NOT NULL DEFAULT '0.00',
  `GameTime` int(64) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Users`
--

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Auth`
--
ALTER TABLE `Auth`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Bans`
--
ALTER TABLE `Bans`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `ClanMembers`
--
ALTER TABLE `ClanMembers`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Clans`
--
ALTER TABLE `Clans`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Homes`
--
ALTER TABLE `Homes`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Mutes`
--
ALTER TABLE `Mutes`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `RegionMembers`
--
ALTER TABLE `RegionMembers`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Regions`
--
ALTER TABLE `Regions`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `RequestsClan`
--
ALTER TABLE `RequestsClan`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `RequestsPlayer`
--
ALTER TABLE `RequestsPlayer`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Spectates`
--
ALTER TABLE `Spectates`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Auth`
--
ALTER TABLE `Auth`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Bans`
--
ALTER TABLE `Bans`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ClanMembers`
--
ALTER TABLE `ClanMembers`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Clans`
--
ALTER TABLE `Clans`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Homes`
--
ALTER TABLE `Homes`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Mutes`
--
ALTER TABLE `Mutes`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `RegionMembers`
--
ALTER TABLE `RegionMembers`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Regions`
--
ALTER TABLE `Regions`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `RequestsClan`
--
ALTER TABLE `RequestsClan`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `RequestsPlayer`
--
ALTER TABLE `RequestsPlayer`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Spectates`
--
ALTER TABLE `Spectates`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Users`
--
ALTER TABLE `Users`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
