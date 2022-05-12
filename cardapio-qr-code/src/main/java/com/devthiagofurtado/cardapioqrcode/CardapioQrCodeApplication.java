package com.devthiagofurtado.cardapioqrcode;

import com.devthiagofurtado.cardapioqrcode.config.FileStorageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageConfig.class})
@EnableAutoConfiguration
@ComponentScan
public class CardapioQrCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardapioQrCodeApplication.class, args);
	}

}
