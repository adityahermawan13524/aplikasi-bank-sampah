-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 24, 2025 at 05:16 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 7.4.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bank_sampah`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id_admin` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `dob` varchar(10) NOT NULL,
  `gender` varchar(20) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id_admin`, `username`, `password`, `email`, `dob`, `gender`, `mobile`, `alamat`, `status`) VALUES
(1, 'ADITYA HERMAWAN', 'Ad010497', 'aditya@gmail.com', '1/4/1997', 'Male', '082110969997', 'VILA MUTIARA GADING 3 BLOK H 11 NO 77', 'Admin'),
(2, 'athaya', '123456', 'athaya@gmail.com', '17/08/1998', 'male', '1', 'Vmg', 'Admin'),
(3, 'athaya1', '123456', 'athaya@gmail.com', '17/08/1998', 'male', '1', 'Vmg', 'User'),
(4, 'zaki', '123456', 'athaya@gmail.com', '17/08/1998', 'male', '1', 'Vmg', 'User'),
(5, 'zaki1', '123456', 'athaya@gmail.com', '17/08/1998', 'male', '1', 'Vmg', 'User'),
(6, 'aditya', '123456', 'A@gmail.com', '6/10/2024', 'Male', '123', 'vmhg', 'Admin'),
(7, 'doli23333', '123456', 'athaya@gmail.com', '17/08/1998', 'male', '1', 'Vmg', 'user');

-- --------------------------------------------------------

--
-- Table structure for table `data_sampah`
--

CREATE TABLE `data_sampah` (
  `id_sampah` int(11) NOT NULL,
  `jenis_sampah` varchar(100) NOT NULL,
  `harga_perKg` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `data_sampah`
--

INSERT INTO `data_sampah` (`id_sampah`, `jenis_sampah`, `harga_perKg`) VALUES
(17, 'Kardus', '1600'),
(18, 'Koran A', '4800'),
(19, 'Koran Tabloid', '1200'),
(20, 'Buku (Buku tulis/pelajaran tanpa kupas)', '1280'),
(21, 'Buku LKS/ Pelajaran dikupas sampul', '1440'),
(22, 'Kertas Putih HVS', '1760'),
(23, 'Kantong Semen ', '1600'),
(24, 'Duplek', '880'),
(25, 'Botol Plastik Shampo Ol, sbun cair/HDPE', '1760'),
(26, 'Aqua Gelas Bersih', '3600'),
(27, 'Aqua Gelas Kotor ', '1600'),
(28, 'Botolair mineral/bodong/pet bersih', '3840'),
(29, 'Tutup galon', '4800'),
(30, 'Plastik bening lembaran', '960'),
(31, 'Plastik kemasan', '400'),
(32, 'Besi padat', '3600'),
(33, 'Kaleng', '2000'),
(34, 'Tembaga bakar', '60000'),
(35, 'Kuningan', '32000'),
(36, 'Kabel listrik isi 3 (kawat tembaga)', '8000'),
(37, 'Mijel literan', '4800');

-- --------------------------------------------------------

--
-- Table structure for table `input_sampah`
--

CREATE TABLE `input_sampah` (
  `id_input_smph` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `kategori_sampah` varchar(50) NOT NULL,
  `berat` varchar(10) NOT NULL,
  `harga_perKg` varchar(20) NOT NULL,
  `pajak` varchar(50) NOT NULL,
  `harga` varchar(10) NOT NULL,
  `saldo` varchar(50) NOT NULL,
  `tanggal` varchar(50) NOT NULL,
  `alamat` varchar(50) NOT NULL,
  `catatan` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `input_sampah`
--

INSERT INTO `input_sampah` (`id_input_smph`, `id_user`, `username`, `kategori_sampah`, `berat`, `harga_perKg`, `pajak`, `harga`, `saldo`, `tanggal`, `alamat`, `catatan`, `status`) VALUES
(29, 1, 'aditya', 'PLASTIK', '1.5', '800', '60', '1140', '1781', '18/1/2025', 'vmg', '', 'Sukses'),
(30, 1, 'aditya', 'BOTOL', '1', '1500', '75', '1425', '2625', '19/1/2025', 'vmg', '', 'Sukses'),
(33, 1, 'aditya', 'BOTOL', '1.2', '1500', '90', '1710', '4335', '19/1/2025', 'vmg', '', 'Sukses'),
(34, 1, 'aditya', 'BOTOL', '1.5', '1500', '112', '2138', '6473', '21/1/2025', 'vmg', '', 'Sukses'),
(35, 1, 'aditya', 'BOTOL', '1.5', '1500', '112', '2138', '8611', '21/1/2025', 'vmg', '', 'Sukses'),
(36, 1, 'aditya', 'KARET', '2.3', '2000', '690', '3910', '7521', '28/1/2025', 'vmg', '', 'Sukses'),
(37, 1, 'aditya', 'PLASTIK', '1.5', '800', '180', '1020', '8541', '28/1/2025', 'vmg', '', 'Sukses'),
(38, 1, 'aditya', 'BOTOL', '1', '1500', '225', '1275', '9816', '28/1/2025', 'vmg', '', 'Sukses'),
(39, 1, 'aditya', 'BOTOL', '1', '1500', '225', '1275', '11091', '28/1/2025', 'vmg', '', 'Sukses'),
(40, 1, 'aditya', 'BOTOL', '1', '1500', '225', '1275', '12366', '28/1/2025', 'vmg', '', 'Sukses'),
(41, 1, 'aditya', 'BOTOL', '1', '1500', '225', '1275', '13641', '28/1/2025', 'vmg', '', 'Sukses'),
(42, 2, 'fita', 'BOTOL', '1', '1500', '225', '1275', '4185', '28/1/2025', 'vmg', '', 'Sukses'),
(43, 2, 'fita', 'BOTOL', '1', '1500', '225', '1275', '5460', '28/1/2025', 'vmg', '', 'Sukses'),
(44, 2, 'fita', 'KARET', '2', '2000', '600', '3400', '8860', '28/1/2025', 'vmg', '', 'Sukses'),
(45, 2, 'fita', 'PLASTIK', '1', '800', '120', '680', '9540', '28/1/2025', 'vmg', '', 'Sukses'),
(46, 2, 'fita', 'BOTOL', '1', '1500', '225', '1275', '10815', '28/1/2025', 'vmg', '', 'Sukses'),
(50, 1, 'aditya', 'PLASTIK', '2', '800', '240', '1360', '15001', '4/2/2025', 'vmg', '', 'Sukses'),
(52, 2, 'fita', 'BOTOL', '10', '1500', '1500', '150000', '100000', '18/1/2025', 'vmg', '', 'sukses'),
(53, 2, 'fita', 'Aqua Gelas Bersih', '1', '3600', '540', '3060', '15830', '8/2/2025', 'Vila Mutiara Gading 3 Blok H 11 No 77', '', 'Sukses'),
(54, 2, 'fita', 'Aqua Gelas Bersih', '1', '3600', '540', '3060', '18890', '8/2/2025', 'Vila Mutiara Gading 3 Blok H 11 No 77', '', 'Sukses'),
(56, 2, 'fita', 'Aqua Gelas Bersih', '1', '3600', '540', '3060', '26030', '8/2/2025', 'Vila Mutiara Gading 3 Blok H 11 No 77', '', 'Sukses'),
(57, 2, 'fita', 'Botol Plastik Shampo Ol, sbun cair/HDPE', '1', '1760', '264', '1496', '27526', '8/2/2025', 'Vila Mutiara Gading 3 Blok H 11 No 77', '', 'Sukses'),
(58, 2, 'fita', 'Buku LKS/ Pelajaran dikupas sampul', '1', '1440', '216', '1224', '21224', '8/2/2025', 'Vila Mutiara Gading 3 Blok H 11 No 77', '', 'Sukses'),
(59, 2, 'fita', 'Aqua Gelas Bersih', '1', '3600', '540', '3060', '24284', '8/2/2025', 'Vila Mutiara Gading 3 Blok H 11 No 77', '', 'Sukses'),
(60, 2, 'fita', 'Kaleng', '1', '2000', '300', '1700', '25984', '8/2/2025', 'Vila Mutiara Gading 3 Blok H 11 No 77', '', 'Di-Tolak'),
(61, 2, 'fita', 'Aqua Gelas Bersih', '1', '3600', '540', '3060', '27344', '8/2/2025', 'Vila Mutiara Gading 3 Blok H 11 No 77', '', 'Sukses'),
(62, 2, 'fita', 'Aqua Gelas Bersih', '1', '3600', '540', '3060', '29060', '8/2/2025', 'Vila Mutiara Gading 3 Blok H 11 No 77', '', 'Sukses');

-- --------------------------------------------------------

--
-- Table structure for table `jadwal`
--

CREATE TABLE `jadwal` (
  `id_jadwal` int(11) NOT NULL,
  `Hari` varchar(50) NOT NULL,
  `tanggal` varchar(50) NOT NULL,
  `waktu_awal` varchar(50) NOT NULL,
  `waktu_akhir` varchar(10) NOT NULL,
  `tempat` varchar(50) NOT NULL,
  `catatan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jadwal`
--

INSERT INTO `jadwal` (`id_jadwal`, `Hari`, `tanggal`, `waktu_awal`, `waktu_akhir`, `tempat`, `catatan`) VALUES
(10, 'Rabu', '29/1/2025', '10:0', '13:45', 'Balai Warga Rw 017', 'Datang Tepat Waktu'),
(11, 'kamis', '30/1/2025', '13:29', '17:30', 'Balai Warga Rw 017', ''),
(12, 'Jumat', '31/1/2025', '10:30', '14:30', 'Balai Warga 017', ''),
(16, 'Sabtu', '8/2/2025', '10:30', '2:30', 'Balai Warga 017', ''),
(17, 'minggu', '9/2/2025', '10:30', '1:0', 'Balai Warga Rw 017', '');

-- --------------------------------------------------------

--
-- Table structure for table `otp_codes`
--

CREATE TABLE `otp_codes` (
  `id` int(11) NOT NULL,
  `id_penarikan` int(11) NOT NULL,
  `username` varchar(15) NOT NULL,
  `otp_code` varchar(6) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `penarikan_tabungan`
--

CREATE TABLE `penarikan_tabungan` (
  `id_penarikan` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `tanggal` varchar(50) NOT NULL,
  `jumlah_penarikan` varchar(20) NOT NULL,
  `saldo_awal` varchar(20) NOT NULL,
  `saldo_akhir` varchar(15) NOT NULL,
  `otp_code` int(11) NOT NULL,
  `status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `penarikan_tabungan`
--

INSERT INTO `penarikan_tabungan` (`id_penarikan`, `id_user`, `username`, `tanggal`, `jumlah_penarikan`, `saldo_awal`, `saldo_akhir`, `otp_code`, `status`) VALUES
(31, 2, 'fita', '15/2/2025', '1000', '700000', '699000', 839500, 'Sukses'),
(32, 2, 'fita', '15/2/2025', '1000', '699000', '698000', 789979, 'Sukses'),
(33, 1, 'aditya', '15/2/2025', '10000', '6010000', '6000000', 144071, 'Sukses'),
(34, 2, 'fita', '15/2/2025', '8000', '698000', '690000', 205011, 'Sedang Diproses !!');

-- --------------------------------------------------------

--
-- Table structure for table `saldo_nasabah`
--

CREATE TABLE `saldo_nasabah` (
  `id_transaksi_saldo` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `saldo_awal` varchar(10) NOT NULL,
  `saldo_akhir` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `saldo_nasabah`
--

INSERT INTO `saldo_nasabah` (`id_transaksi_saldo`, `id_user`, `username`, `saldo_awal`, `saldo_akhir`) VALUES
(81, 2, 'fita', '4034', 4794),
(82, 2, 'fita', '4794', 6219),
(83, 2, 'fita', '4794', 7644),
(84, 1, 'aditya', '3273', 4698),
(85, 2, 'fita', '4794', 6219),
(86, 2, 'fita', '6219', 9069),
(87, 2, 'fita', '9069', 10494),
(88, 2, 'fita', '10494', 11919),
(89, 2, 'fita', '11919', 13344),
(90, 2, 'fita', '13344', 14769),
(91, 2, 'fita', '14769', 16194),
(92, 2, 'fita', '16194', 17619),
(93, 2, 'fita', '17619', 19044),
(94, 2, 'fita', '19044', 20469),
(95, 2, 'fita', '20469', 21894),
(96, 2, 'fita', '21894', 23604),
(97, 1, 'aditya', '3273', 4983),
(98, 1, 'aditya', '73', 1641),
(99, 1, 'aditya', '641', 4204),
(100, 2, 'fita', '13604', 15504),
(101, 2, 'fita', '13604', 156104),
(102, 2, 'fita', '13604', 15172),
(103, 2, 'fita', '13604', 15742),
(104, 1, 'aditya', '641', 1781),
(105, 1, 'aditya', '1200', 2625),
(106, 2, 'fita', '1200', 2910),
(107, 1, 'aditya', '2625', 4193),
(108, 1, 'aditya', '2625', 4335),
(109, 1, 'aditya', '4335', 6473),
(110, 1, 'aditya', '6473', 8611),
(111, 1, 'aditya', '3611', 7521),
(112, 1, 'aditya', '7521', 8541),
(113, 1, 'aditya', '8541', 9816),
(114, 1, 'aditya', '9816', 11091),
(115, 1, 'aditya', '11091', 12366),
(116, 1, 'aditya', '12366', 13641),
(117, 2, 'fita', '2910', 4185),
(118, 2, 'fita', '4185', 5460),
(119, 2, 'fita', '5460', 8860),
(120, 2, 'fita', '8860', 9540),
(121, 2, 'fita', '9540', 10815),
(122, 2, 'fita', '10815', 12090),
(123, 2, 'fita', '12090', 12770),
(124, 1, 'aditya', '13641', 16191),
(125, 1, 'aditya', '13641', 15001),
(126, 2, 'fita', '12770', 15830),
(127, 2, 'fita', '15830', 18890),
(128, 2, 'fita', '18890', 22970),
(129, 2, 'fita', '22970', 26030),
(130, 2, 'fita', '26030', 27526),
(131, 2, 'fita', '20000', 21224),
(132, 2, 'fita', '21224', 24284),
(133, 2, 'fita', '24284', 25984),
(134, 2, 'fita', '24284', 27344),
(135, 2, 'fita', '26000', 29060);

-- --------------------------------------------------------

--
-- Table structure for table `token`
--

CREATE TABLE `token` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `token` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `token`
--

INSERT INTO `token` (`id`, `username`, `token`) VALUES
(14, 'fita', 'e6fCIjPvSFy5fIBHY8D2qq:APA91bE-asp9pO8ygrGUh1ZfzNcm7HxAKD-UneUpSsuGcKrQpigj0aTMcH9JNee4wt985t-vKiPk8QEL6gUG4t1Pn-SXW8N-H8JlfHIW_pQwdIftSQ8rSnc'),
(15, 'hanifah', 'e6fCIjPvSFy5fIBHY8D2qq:APA91bE-asp9pO8ygrGUh1ZfzNcm7HxAKD-UneUpSsuGcKrQpigj0aTMcH9JNee4wt985t-vKiPk8QEL6gUG4t1Pn-SXW8N-H8JlfHIW_pQwdIftSQ8rSnc'),
(16, 'aditya', 'e6fCIjPvSFy5fIBHY8D2qq:APA91bE-asp9pO8ygrGUh1ZfzNcm7HxAKD-UneUpSsuGcKrQpigj0aTMcH9JNee4wt985t-vKiPk8QEL6gUG4t1Pn-SXW8N-H8JlfHIW_pQwdIftSQ8rSnc');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id_user` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `dob` varchar(15) NOT NULL,
  `gender` varchar(30) NOT NULL,
  `mobile` varchar(50) NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `status` varchar(50) NOT NULL,
  `saldo` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `username`, `password`, `email`, `dob`, `gender`, `mobile`, `alamat`, `status`, `saldo`) VALUES
(1, 'aditya', '1234567', 'aditya@gmail.com', '16/12/2024', 'Male', '082312757398', 'vmg', 'User', '6000'),
(2, 'fita', '123456', 'fita@gmail.com', '16/12/2024', 'Female', '089626078965', 'Vila Mutiara Gading 3 Blok H 11 No 77', 'User', '6980000'),
(3, 'hanifah', '080324', 'hanifah@gmail.com', '08/03/2024', 'Female', '082312757398', 'Vila Mutiara Gading 3 blok H', 'Admin', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_admin`),
  ADD KEY `id_admin` (`id_admin`);

--
-- Indexes for table `data_sampah`
--
ALTER TABLE `data_sampah`
  ADD PRIMARY KEY (`id_sampah`);

--
-- Indexes for table `input_sampah`
--
ALTER TABLE `input_sampah`
  ADD PRIMARY KEY (`id_input_smph`);

--
-- Indexes for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD PRIMARY KEY (`id_jadwal`);

--
-- Indexes for table `otp_codes`
--
ALTER TABLE `otp_codes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `penarikan_tabungan`
--
ALTER TABLE `penarikan_tabungan`
  ADD PRIMARY KEY (`id_penarikan`);

--
-- Indexes for table `saldo_nasabah`
--
ALTER TABLE `saldo_nasabah`
  ADD PRIMARY KEY (`id_transaksi_saldo`);

--
-- Indexes for table `token`
--
ALTER TABLE `token`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `data_sampah`
--
ALTER TABLE `data_sampah`
  MODIFY `id_sampah` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `input_sampah`
--
ALTER TABLE `input_sampah`
  MODIFY `id_input_smph` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;

--
-- AUTO_INCREMENT for table `jadwal`
--
ALTER TABLE `jadwal`
  MODIFY `id_jadwal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `otp_codes`
--
ALTER TABLE `otp_codes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `penarikan_tabungan`
--
ALTER TABLE `penarikan_tabungan`
  MODIFY `id_penarikan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `saldo_nasabah`
--
ALTER TABLE `saldo_nasabah`
  MODIFY `id_transaksi_saldo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=136;

--
-- AUTO_INCREMENT for table `token`
--
ALTER TABLE `token`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
