package net.deepuroy.services.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableAutoConfiguration
@EnableEurekaServer
public class EurekaServer {
	
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer.class, args);
	}

}
