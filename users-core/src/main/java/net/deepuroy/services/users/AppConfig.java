package net.deepuroy.services.users;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;

import net.deepuroy.services.providers.ProviderFilters;
import net.deepuroy.services.providers.ProviderRegistry;
import net.deepuroy.services.providers.discovery.DiscoveryProviderRegistry;
import net.deepuroy.services.providers.discovery.ZuulMappingHandlerUpdater;

@Configuration
@EnableScheduling
@EnableEurekaClient
@EnableZuulProxy
@ComponentScan
public class AppConfig {

	@Bean
	public ProviderRegistry configure(DiscoveryClient client) {
		return new DiscoveryProviderRegistry(client, ProviderFilters.regex("^user-provider-(.*)", 1));
	}

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	ViewResolver resolveHandlerBars() {
		HandlebarsViewResolver vr = new HandlebarsViewResolver();
		vr.setPrefix("classpath:/templates");
		vr.setSuffix(".html");
		vr.setFailOnMissingFile(false);
		return vr;
	}

	@Bean
	@ConditionalOnBean({ ProviderRegistry.class, ZuulHandlerMapping.class })
	ZuulMappingHandlerUpdater refresh(ProviderRegistry registry, ZuulHandlerMapping zhm) {
		ZuulMappingHandlerUpdater listener = new ZuulMappingHandlerUpdater(zhm);
		registry.addListener(listener);
		return listener;
	}

	@Bean
	RouteLocator routeLocator(ProviderRegistry registry, DiscoveryClient client, ZuulProperties properties) {
		return new ProviderRouteLocator("/providers", registry, client, properties);
	}

}
