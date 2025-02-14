package com.koreait.hanGyeDolpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class HanGyeDolpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanGyeDolpaApplication.class, args);
	}
}
