package com.devthiagofurtado.cardapioqrcode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SuppressWarnings("squid:S2699")
@SpringBootTest
class CardapioQrCodeApplicationTests {

	@Test
	void contextLoads() {
		CardapioQrCodeApplication.main(new String[]{
				"--spring.main.web-environment=false",
				"--spring.autoconfigure.exclude=blahblahblah",
				// Override any other environment properties according to your needs
		});
	}

}
