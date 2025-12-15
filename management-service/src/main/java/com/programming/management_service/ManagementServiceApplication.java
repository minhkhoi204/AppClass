package com.programming.management_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@EnableFeignClients(basePackages = "com.programming.management_service.management_caller")
@SpringBootApplication
@ComponentScan(basePackages = {
		"com.programming.management_service",
		"com.programming.common"
})
public class ManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementServiceApplication.class, args);
	}

}
