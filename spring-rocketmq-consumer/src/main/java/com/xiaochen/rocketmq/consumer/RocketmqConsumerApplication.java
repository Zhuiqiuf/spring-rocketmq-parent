package com.xiaochen.rocketmq.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@MapperScan(basePackages = {"com.xiaochen.rocketmq.consumer.mapper"})
@SpringBootApplication(scanBasePackages = {"com.xiaochen.rocketmq.consumer",
		"com.xiaochen.rocketmq.starter"})
@EnableTransactionManagement(proxyTargetClass = true)
public class RocketmqConsumerApplication {

	@Qualifier("noBalanceRestTemplate")
	@Bean
	public RestTemplate getNoBalanceRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		return restTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(RocketmqConsumerApplication.class, args);
	}
}
