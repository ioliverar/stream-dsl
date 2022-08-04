DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions` (
	`id` varchar(36) NOT NULL,
	`lastName` varchar(64) DEFAULT NULL, 
	`firstName` varchar(64) DEFAULT NULL,
	`creditCardNumber` varchar(20) DEFAULT NULL,
	`itemPurchased` varchar(64) DEFAULT NULL,
	`quantity` decimal(16,4) DEFAULT NULL,  
	`price` decimal(16,4) DEFAULT NULL, 
	`purchaseDate` datetime DEFAULT NULL,
	`zipCode` varchar(32) default NULL,
	PRIMARY KEY (`id`)
);