CREATE TABLE `Invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(20) NOT NULL,
   `supplierId` varchar(255) NOT NULL ,
  `invoiceId` varchar(255) NOT NULL,
  `terms` int(20) NOT NULL, 
  `paymentDate` DATE  NULL,
   `invoiceDate` DATE NOT NULL,
   `paymentAmount` DECIMAL(13,2)  NULL,
    `invoiceAmount` DECIMAL(13,2) NOT NULL,
    `state` varchar(50) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `invSupKey` (`invoiceId`,`supplierId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4