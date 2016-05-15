package net.deepuroy.services.users.google;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;


@EnableEurekaClient
@SpringBootApplication
public class UserGoogleApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserGoogleApplication.class, args);
	}
	
	
	
	@Bean
	ViewResolver resolveHandlerBars() {
		HandlebarsViewResolver vr =  new HandlebarsViewResolver();
		vr.setPrefix("classpath:/templates");
		vr.setSuffix(".html");
		return vr;
	}
	@Bean
	@Autowired
	public GoogleIdTokenVerifier idTokenVerifier(@Value("${apis.google.key}") String apiKey) {
		HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
					.setAudience(Collections.singletonList(apiKey))
					.build();
		return verifier;
	}

}
