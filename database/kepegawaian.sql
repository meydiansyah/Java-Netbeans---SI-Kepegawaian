-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Waktu pembuatan: 20 Feb 2021 pada 14.19
-- Versi server: 8.0.22
-- Versi PHP: 7.3.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kepegawaian`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `id` int NOT NULL,
  `pegawai_id` int NOT NULL,
  `password` varchar(255) NOT NULL,
  `tgl_daftar` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`id`, `pegawai_id`, `password`, `tgl_daftar`) VALUES
(1, 1, '123123', '2021-01-11 15:54:02'),
(4, 7, '123123', '2021-02-02 13:09:46');

-- --------------------------------------------------------

--
-- Struktur dari tabel `agama`
--

CREATE TABLE `agama` (
  `id` int NOT NULL,
  `nama_agama` varchar(255) CHARACTER SET utf8mb4  NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

--
-- Dumping data untuk tabel `agama`
--

INSERT INTO `agama` (`id`, `nama_agama`) VALUES
(1, 'Islam'),
(2, 'Protestan'),
(3, 'Katolik'),
(4, 'Hindu'),
(5, 'Budha'),
(6, 'Khonghucu'),
(7, 'Lainnya');

-- --------------------------------------------------------

--
-- Struktur dari tabel `jabatan`
--

CREATE TABLE `jabatan` (
  `id` int NOT NULL,
  `nama_jabatan` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

--
-- Dumping data untuk tabel `jabatan`
--

INSERT INTO `jabatan` (`id`, `nama_jabatan`) VALUES
(1, 'Human Resource Division'),
(2, 'Finance'),
(3, 'Akuntan'),
(4, 'Marketing'),
(5, 'Customer Service');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pegawai`
--

CREATE TABLE `pegawai` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `nip` varchar(100) NOT NULL,
  `jabatan_id` int NOT NULL,
  `npwp` varchar(100) NOT NULL,
  `tgl_masuk` datetime NOT NULL,
  `no_bpjs` varchar(100) NOT NULL,
  `no_bpjs_ketenagakerjaan` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

--
-- Dumping data untuk tabel `pegawai`
--

INSERT INTO `pegawai` (`id`, `user_id`, `nip`, `jabatan_id`, `npwp`, `tgl_masuk`, `no_bpjs`, `no_bpjs_ketenagakerjaan`) VALUES
(1, 1, '200109282443', 1, '12.298.288.342.090.000', '2021-01-03 22:50:29', '0001425339184', '28177382910'),
(7, 2, '200109282444', 1, '12.298.288.342.090.000', '2021-02-02 08:41:39', '00001231232', '239239293292'),
(15, 13, '200109282445', 2, '123123', '2021-02-04 13:24:23', '123123123', '123123123'),
(17, 17, '200109282422', 3, '123123', '2021-02-04 15:53:04', '123123', '1231233');

--
-- Trigger `pegawai`
--
DELIMITER $$
CREATE TRIGGER `update_pelamar` AFTER INSERT ON `pegawai` FOR EACH ROW BEGIN

	update pelamar set 
    	status = 'Diterima'
        where user_id = new.user_id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pelamar`
--

CREATE TABLE `pelamar` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `tgl_lamar` timestamp NOT NULL,
  `status` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

--
-- Dumping data untuk tabel `pelamar`
--

INSERT INTO `pelamar` (`id`, `user_id`, `tgl_lamar`, `status`) VALUES
(1, 2, '2021-01-04 03:09:44', 'Diterima'),
(12, 13, '2021-02-04 06:22:09', 'Diterima'),
(14, 15, '2021-02-04 07:30:28', 'Ditolak'),
(15, 16, '2021-02-04 07:32:02', 'Proses'),
(16, 17, '2021-02-04 08:51:45', 'Diterima');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `id` int NOT NULL,
  `nama` varchar(255) NOT NULL,
  `tgl_lahir` date NOT NULL,
  `nik` varchar(20) NOT NULL,
  `no_kk` varchar(20) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `telepon` varchar(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `gol_darah` varchar(10) NOT NULL,
  `kelamin` varchar(20) NOT NULL,
  `pendidikan` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `agama_id` int NOT NULL,
  `admin_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`id`, `nama`, `tgl_lahir`, `nik`, `no_kk`, `alamat`, `telepon`, `email`, `gol_darah`, `kelamin`, `pendidikan`, `image`, `agama_id`, `admin_id`) VALUES
(1, 'Riski Meydiansyah', '2001-09-28', '160528092809010001', '160528092809010001', 'Jl. Tegal Parang Selatan II, Kecamatan Mampang Prapatan, Jakarta Selatan, Jakarta', '081231233344', 'riski@gmail.com', 'A+', 'Laki - Laki', 'Strata 1 Teknik Informatika', '/img/pegawai_man.png', 1, 1),
(2, 'Rudi Widodo', '1998-04-09', '160508040904980098', '160508040904980098', 'Kebayoran Lama', '081231233344', 'rudi@gmail.com', 'B+', 'Laki - Laki', 'Strata 1 Ilmu Komunikasi', '/img/pegawai_man.png', 1, 1),
(13, 'rudi', '2000-01-02', '123123123', '123123132', 'alamat', '131233123', 'rudi@gmail.com', 'as', 'Laki - Laki', 'asdasd', '/img/pegawai_man.png', 1, 1),
(15, 'ari', '2000-02-01', '123123', '123123', 'alamat', '123123', '123123', 'B-', 'Laki - Laki', 'Strata 1 Akuntansi', 'img/pelamar_man.png', 1, 1),
(16, 'asdasd', '2000-02-02', '12313', '123123', 'asdasd', '123123', 'asdda', 'asd', 'Laki - Laki', 'ads', 'img/pelamar_man.png', 1, 1),
(17, 'Ani', '1999-08-02', '123123123', '123123', 'Kebayoran Baru', '12312412', 'ani@gmail.com', 'A', 'Perempuan', 'Strata 1 Akutansi', '/img/pegawai_woman.png', 1, 1);

--
-- Trigger `user`
--
DELIMITER $$
CREATE TRIGGER `add_pelamar` AFTER INSERT ON `user` FOR EACH ROW BEGIN

	insert into pelamar SET
    	user_id = new.id,
        tgl_lamar = NOW(),
        status = "Proses";

END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `delete_pelamar` BEFORE DELETE ON `user` FOR EACH ROW BEGIN

	delete from pegawai where 
    user_id = old.id;
    
	delete from pelamar where 
    user_id = old.id;

END
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_kepegawaian_pegawai_idx` (`pegawai_id`);

--
-- Indeks untuk tabel `agama`
--
ALTER TABLE `agama`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `jabatan`
--
ALTER TABLE `jabatan`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `pegawai`
--
ALTER TABLE `pegawai`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_kepegawaian_user_idx2` (`user_id`),
  ADD KEY `fk_kepegawaian_jabatan_idx` (`jabatan_id`);

--
-- Indeks untuk tabel `pelamar`
--
ALTER TABLE `pelamar`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_kepegawaian_user_idx` (`user_id`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_kepegawaian_agama_idx` (`agama_id`),
  ADD KEY `fk_kepegawaian_admin_idx` (`admin_id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `agama`
--
ALTER TABLE `agama`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `jabatan`
--
ALTER TABLE `jabatan`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `pegawai`
--
ALTER TABLE `pegawai`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT untuk tabel `pelamar`
--
ALTER TABLE `pelamar`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT untuk tabel `user`
--
ALTER TABLE `user`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `fk_kepegawaian_pegawai_idx` FOREIGN KEY (`pegawai_id`) REFERENCES `pegawai` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Ketidakleluasaan untuk tabel `pegawai`
--
ALTER TABLE `pegawai`
  ADD CONSTRAINT `fk_kepegawaian_jabatan_idx` FOREIGN KEY (`jabatan_id`) REFERENCES `jabatan` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `fk_kepegawaian_user_idx2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Ketidakleluasaan untuk tabel `pelamar`
--
ALTER TABLE `pelamar`
  ADD CONSTRAINT `fk_kepegawaian_user_idx` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Ketidakleluasaan untuk tabel `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_kepegawaian_admin_idx` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `fk_kepegawaian_agama_idx` FOREIGN KEY (`agama_id`) REFERENCES `agama` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
