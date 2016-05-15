package net.deepuroy.services.providers.discovery;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.deepuroy.services.providers.Provider;
import net.deepuroy.services.providers.ProviderFilter;
import net.deepuroy.services.providers.ProviderRegistry;

@Component
@Scope("singleton")
public class DiscoveryProviderRegistry extends ProviderRegistry {

	private static final Logger LOG = LogManager.getLogger(DiscoveryProviderRegistry.class);

	private final DiscoveryClient discovery;

	private final ProviderFilter filter;

	public DiscoveryProviderRegistry(DiscoveryClient client, ProviderFilter filter) {
		this.discovery = client;
		this.filter = filter;
	}

	@Scheduled(fixedDelay = 20000)
	private void loadFromEureka() {
		LOG.info("Refreshing catalog...searching for providers.");
		List<Provider> providers = new ArrayList<Provider>();
		for (String serviceId : discovery.getServices()) {

			if (!filter.test(serviceId)) {
				LOG.debug("Service did not match:" + serviceId);
				continue;
			}
			LOG.debug("Service matched:" + serviceId);
			List<ServiceInstance> instances = discovery.getInstances(serviceId);
			if (instances.size() > 0) {
				providers.add(new Provider(getName(serviceId), serviceId));
			}
		}
		replaceAll(providers);
	}

	private String getName(String serviceId) {
		return filter.name(serviceId);
	}

}
