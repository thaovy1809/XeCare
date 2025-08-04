package com.group3.xecare2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.group3.xecare2"})
public class XeCare2Application {

	public static void main(String[] args) {
		SpringApplication.run(XeCare2Application.class, args);
	}

}
