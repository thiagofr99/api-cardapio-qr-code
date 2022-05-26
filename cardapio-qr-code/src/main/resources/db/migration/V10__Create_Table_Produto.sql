CREATE TABLE IF NOT EXISTS `produto` (
  	`id` bigint(20) NOT NULL AUTO_INCREMENT,
  	`produto_nome` varchar(255) DEFAULT NULL,         
  	`valor_produto` double DEFAULT NULL,
   `data_cadastro` date DEFAULT NULL,
	`data_atualizacao` date DEFAULT NULL,
	`disponivel` bit(1) DEFAULT NULL,
	`observacao` VARCHAR(255) DEFAULT NULL,
	`tipo_produto` VARCHAR(255) DEFAULT NULL,
	`cardapio_id` BIGINT(20) DEFAULT NULL,
	PRIMARY KEY (`id`),
   FOREIGN KEY (`cardapio_id`) REFERENCES `cardapio` (`id`)

) ENGINE=InnoDB;