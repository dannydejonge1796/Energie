-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Gegenereerd op: 04 feb 2023 om 23:25
-- Serverversie: 10.4.27-MariaDB
-- PHP-versie: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `energy`
--
CREATE DATABASE IF NOT EXISTS `energy` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `energy`;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `customer`
--

CREATE TABLE `customer` (
  `number` varchar(3) NOT NULL,
  `firstname` varchar(55) NOT NULL,
  `lastname` varchar(55) NOT NULL,
  `advance` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Gegevens worden geëxporteerd voor tabel `customer`
--

INSERT INTO `customer` (`number`, `firstname`, `lastname`, `advance`) VALUES
('123', 'Danny', 'de Jonge', NULL);

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `electricity_rate`
--

CREATE TABLE `electricity_rate` (
  `id` int(11) NOT NULL,
  `customer_number` varchar(3) NOT NULL,
  `rate` float NOT NULL,
  `date_from` date NOT NULL,
  `date_to` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `gas_rate`
--

CREATE TABLE `gas_rate` (
  `id` int(11) NOT NULL,
  `customer_number` varchar(3) NOT NULL,
  `rate` float NOT NULL,
  `date_from` date NOT NULL,
  `date_to` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `weekly_usage`
--

CREATE TABLE `weekly_usage` (
  `id` int(11) NOT NULL,
  `customer_number` varchar(3) NOT NULL,
  `usage_elec` int(11) NOT NULL,
  `usage_gas` int(11) NOT NULL,
  `date_start` date NOT NULL,
  `date_end` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexen voor geëxporteerde tabellen
--

--
-- Indexen voor tabel `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`number`);

--
-- Indexen voor tabel `electricity_rate`
--
ALTER TABLE `electricity_rate`
  ADD PRIMARY KEY (`id`),
  ADD KEY `customer_elec_rate` (`customer_number`);

--
-- Indexen voor tabel `gas_rate`
--
ALTER TABLE `gas_rate`
  ADD PRIMARY KEY (`id`),
  ADD KEY `customer_gas_rate` (`customer_number`);

--
-- Indexen voor tabel `weekly_usage`
--
ALTER TABLE `weekly_usage`
  ADD PRIMARY KEY (`id`),
  ADD KEY `customer_usage` (`customer_number`);

--
-- AUTO_INCREMENT voor geëxporteerde tabellen
--

--
-- AUTO_INCREMENT voor een tabel `electricity_rate`
--
ALTER TABLE `electricity_rate`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT voor een tabel `gas_rate`
--
ALTER TABLE `gas_rate`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT voor een tabel `weekly_usage`
--
ALTER TABLE `weekly_usage`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- Beperkingen voor geëxporteerde tabellen
--

--
-- Beperkingen voor tabel `electricity_rate`
--
ALTER TABLE `electricity_rate`
  ADD CONSTRAINT `customer_elec_rate` FOREIGN KEY (`customer_number`) REFERENCES `customer` (`number`);

--
-- Beperkingen voor tabel `gas_rate`
--
ALTER TABLE `gas_rate`
  ADD CONSTRAINT `customer_gas_rate` FOREIGN KEY (`customer_number`) REFERENCES `customer` (`number`);

--
-- Beperkingen voor tabel `weekly_usage`
--
ALTER TABLE `weekly_usage`
  ADD CONSTRAINT `customer_usage` FOREIGN KEY (`customer_number`) REFERENCES `customer` (`number`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
