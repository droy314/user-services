package net.deepuroy.services.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableAutoConfiguration
@EnableZuulProxy
public class ZuulServer {

	public static void main(String[] args) {
		SpringApplication.run(ZuulServer.class, args);
	}
}
