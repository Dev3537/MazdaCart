package com.ecommerce.mazdacart.configurations;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
public class MetricsInterceptor implements ClientHttpRequestInterceptor {
	@Autowired
	private MeterRegistry meterRegistry;


	@Override
	public ClientHttpResponse intercept (HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
		throws IOException {
		Instant start = Instant.now();
		ClientHttpResponse response = execution.execute(request, body);
		Instant end = Instant.now();
		long duration = Duration.between(start, end).toMillis();
		Timer.builder("dev.rest.client.calls").tag("method", request.getMethod().name())
			.tag("uri", request.getURI().toString()).tag("status", String.valueOf(response.getStatusCode().value()))
			.register(meterRegistry).record(duration, TimeUnit.MILLISECONDS);
		return response;
	}
}
