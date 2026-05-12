package com.example.flow_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableFeignClients
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class FlowManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowManagerApplication.class, args);
	}

}
