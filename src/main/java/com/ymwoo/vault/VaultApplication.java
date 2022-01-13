package com.ymwoo.vault;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class VaultApplication {


	@Value("${robot}")
	private String robot;

	public static void main(String[] args) {
		SpringApplication.run(VaultApplication.class, args);
	}

	@PostConstruct
	public void postConstruct() {
		System.out.println(">>>>>>>>>>>>>>>>>>>> " + robot);
	}
}
