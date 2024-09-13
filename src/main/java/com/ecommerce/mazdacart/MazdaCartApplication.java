package com.ecommerce.mazdacart;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
/**
 * Authored by www.linkedin.com/in/pbdevaraj
 */ public class MazdaCartApplication {

	public static void main (String[] args) {
		SpringApplication.run(MazdaCartApplication.class, args);
	}


	@Bean
	public ModelMapper modelMapper () {
		return new ModelMapper();
	}

	@Bean
	public RestClient restClient () {
		return RestClient.builder().build();
	}

}
