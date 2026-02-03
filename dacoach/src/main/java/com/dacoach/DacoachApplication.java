package com.dacoach;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

@MapperScan(basePackages = "com.dacoach.mapper")
@SpringBootApplication
public class DacoachApplication {

	public static void main(String[] args) {
		SpringApplication.run(DacoachApplication.class, args);
	}
	@Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return (factory) -> factory.addConnectorCustomizers((connector) -> {
            // 톰캣 11 버전에서 멀티파트 파일 개수 제한을 푸는 핵심 코드
            connector.setProperty("maxMultipartFormDataFileCount", "50");
            // 파라미터 개수도 같이 늘려줍니다.
            connector.setMaxParameterCount(1000);
        });
    }

}
