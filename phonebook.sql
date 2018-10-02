-- phpMyAdmin SQL Dump
-- version 4.8.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 02, 2018 at 04:55 PM
-- Server version: 10.1.33-MariaDB
-- PHP Version: 7.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `phonebook`
--

-- --------------------------------------------------------

--
-- Table structure for table `contact`
--

CREATE TABLE `contact` (
  `contactid` int(10) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `contacttype` varchar(50) NOT NULL,
  `description` varchar(100) NOT NULL,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  `datechanged` datetime NOT NULL,
  `userid` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `contactemail`
--

CREATE TABLE `contactemail` (
  `contactemailid` int(10) NOT NULL,
  `email` varchar(50) NOT NULL,
  `datechanged` datetime NOT NULL,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  `contactid` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `contactphonenum`
--

CREATE TABLE `contactphonenum` (
  `phonenumid` int(10) NOT NULL,
  `phonenum` varchar(50) NOT NULL,
  `phonenumtype` varchar(50) NOT NULL,
  `datechanged` datetime NOT NULL,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  `contactid` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `contacttype`
--

CREATE TABLE `contacttype` (
  `contacttypeid` int(10) NOT NULL,
  `contacttype` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `phonetype`
--

CREATE TABLE `phonetype` (
  `phonetypeid` int(10) NOT NULL,
  `phonetype` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userid` int(10) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  `datechanged` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `contact`
--
ALTER TABLE `contact`
  ADD PRIMARY KEY (`contactid`),
  ADD KEY `userid` (`userid`);

--
-- Indexes for table `contactemail`
--
ALTER TABLE `contactemail`
  ADD PRIMARY KEY (`contactemailid`),
  ADD KEY `contactid` (`contactid`);

--
-- Indexes for table `contactphonenum`
--
ALTER TABLE `contactphonenum`
  ADD PRIMARY KEY (`phonenumid`),
  ADD KEY `contactid` (`contactid`);

--
-- Indexes for table `contacttype`
--
ALTER TABLE `contacttype`
  ADD PRIMARY KEY (`contacttypeid`);

--
-- Indexes for table `phonetype`
--
ALTER TABLE `phonetype`
  ADD PRIMARY KEY (`phonetypeid`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `contact`
--
ALTER TABLE `contact`
  MODIFY `contactid` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `contactemail`
--
ALTER TABLE `contactemail`
  MODIFY `contactemailid` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `contactphonenum`
--
ALTER TABLE `contactphonenum`
  MODIFY `phonenumid` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `contacttype`
--
ALTER TABLE `contacttype`
  MODIFY `contacttypeid` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `phonetype`
--
ALTER TABLE `phonetype`
  MODIFY `phonetypeid` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userid` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `contact`
--
ALTER TABLE `contact`
  ADD CONSTRAINT `contact_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `contactemail`
--
ALTER TABLE `contactemail`
  ADD CONSTRAINT `contactemail_ibfk_1` FOREIGN KEY (`contactid`) REFERENCES `contact` (`contactid`);

--
-- Constraints for table `contactphonenum`
--
ALTER TABLE `contactphonenum`
  ADD CONSTRAINT `contactphonenum_ibfk_1` FOREIGN KEY (`contactid`) REFERENCES `contact` (`contactid`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
