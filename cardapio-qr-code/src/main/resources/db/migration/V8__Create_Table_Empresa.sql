CREATE TABLE IF NOT EXISTS `empresa` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `empresa_nome` varchar(255) DEFAULT NULL,
   `cep_endereco` varchar(8) DEFAULT NULL,
   `numero_endereco` VARCHAR(255) DEFAULT NULL,
   `complemento_endereco` VARCHAR(30) DEFAULT NULL,
   `data_cadastro` date DEFAULT NULL,
	`data_atualizacao` date DEFAULT NULL,
	`enabled` bit(1) DEFAULT NULL,
	`url_image` VARCHAR(255) DEFAULT NULL,
	`user_id` BIGINT(20) DEFAULT NULL,
	PRIMARY KEY (`id`),
   FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)

) ENGINE=InnoDB;