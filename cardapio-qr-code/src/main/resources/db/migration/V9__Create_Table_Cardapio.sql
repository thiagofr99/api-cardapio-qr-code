CREATE TABLE IF NOT EXISTS `cardapio` (
  	`id` bigint(20) NOT NULL AUTO_INCREMENT,
  	`cardapio_nome` varchar(255) DEFAULT NULL,         
   `data_cadastro` date DEFAULT NULL,
	`data_atualizacao` date DEFAULT NULL,
	`upload` bit(1) DEFAULT NULL,
	`url_cardapio` VARCHAR(255) DEFAULT NULL,
	`url_qrcode` VARCHAR(255) DEFAULT NULL,
	`empresa_id` BIGINT(20) DEFAULT NULL,
	PRIMARY KEY (`id`),
   FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`)

) ENGINE=InnoDB;