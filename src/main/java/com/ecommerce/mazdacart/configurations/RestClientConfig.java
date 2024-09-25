package com.ecommerce.mazdacart.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

	@Autowired
	MetricsInterceptor metricsInterceptor;

	@Bean
	public RestClient restClient () {
		return RestClient.builder().requestInterceptor(metricsInterceptor).build();
	}


}
