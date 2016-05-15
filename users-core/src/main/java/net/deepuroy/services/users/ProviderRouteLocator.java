package net.deepuroy.services.users;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;

import net.deepuroy.services.providers.Provider;
import net.deepuroy.services.providers.ProviderRegistry;

public class ProviderRouteLocator extends DiscoveryClientRouteLocator implements RefreshableRouteLocator {

	public static final String DEFAULT_ROUTE = "/**";

	private final ProviderRegistry registry;

	private final String pathPrefix;

	public ProviderRouteLocator(String pathPrefix, ProviderRegistry registry, DiscoveryClient discovery,
			ZuulProperties properties) {
		super("/", discovery, properties);
		this.pathPrefix = canonize(pathPrefix);
		this.registry = registry;
	}

	private String canonize(String input) {
		String path = (input == null || input.trim().length() == 0) ? "/" : input.trim();
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	protected LinkedHashMap<String, ZuulRoute> locateRoutes() {
		LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<String, ZuulRoute>();
		// routesMap.putAll(super.locateRoutes());
		if (this.registry != null) {
			Map<String, ZuulRoute> staticServices = new LinkedHashMap<String, ZuulRoute>();
			for (ZuulRoute route : routesMap.values()) {
				String serviceId = route.getServiceId();
				if (serviceId == null) {
					serviceId = route.getId();
				}
				if (serviceId != null) {
					staticServices.put(serviceId, route);
				}
			}
			for (Provider provider : registry) {
				ZuulRoute route = new ZuulRoute(getRouteUrl(provider), provider.getServiceId());
				routesMap.put(route.getPath(), route);

			}
		}
		if (routesMap.get(DEFAULT_ROUTE) != null) {
			ZuulRoute defaultRoute = routesMap.get(DEFAULT_ROUTE);
			// Move the defaultServiceId to the end
			routesMap.remove(DEFAULT_ROUTE);
			routesMap.put(DEFAULT_ROUTE, defaultRoute);
		}
		return routesMap;
	}

	private String getRouteUrl(Provider provider) {
		return pathPrefix + "/" + provider.getName() + "/**";

	}

	@Override
	public void refresh() {
		doRefresh();
	}
}
