package com.cloud.chuchen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class FastdfsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastdfsServerApplication.class, args);
	}
}
