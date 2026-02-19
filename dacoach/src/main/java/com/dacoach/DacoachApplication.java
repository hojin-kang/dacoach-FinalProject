package com.dacoach;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages = "com.dacoach.mapper")
@EnableScheduling
@SpringBootApplication
public class DacoachApplication {

	public static void main(String[] args) {
		SpringApplication.run(DacoachApplication.class, args);
	}

}
