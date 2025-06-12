package com.book.myshow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {
	@Bean
	public OpenAPI myOpenAPI() {
		Contact contact = new Contact();
		contact.setEmail("ganeshkd@yahoo.com");
		contact.setName("Booking APIs");
		contact.setUrl("https://www.publicissapient.com/");

		License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

		Info info = new Info().title("Booking API").version("1.0").contact(contact)
				.description("This API exposes endpoints Booking")
				.termsOfService("https://www.publicissapient.com/en/terms-of-use").license(mitLicense);

		return new OpenAPI().info(info);
	}

}
