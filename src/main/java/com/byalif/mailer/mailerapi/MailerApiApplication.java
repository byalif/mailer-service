package com.byalif.mailer.mailerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class MailerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailerApiApplication.class, args);
	}

}
