CREATE TABLE `User` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(20) NOT NULL,
  `active` bit(1) NOT NULL default true,
  `email` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `createDate` DATETIME NOT NULL default now(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `emailAddressUniqueKey` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4