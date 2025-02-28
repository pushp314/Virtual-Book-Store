package com.virtualbookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VirtualBookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualBookstoreApplication.class, args);
		System.out.println("Server is running on the port: http://localhost:8080");
	}

}
