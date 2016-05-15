package net.deepuroy.services.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ProviderRegistry implements Iterable<Provider> {
	
	private static final Logger LOG = LogManager.getLogger(ProviderRegistry.class);

	private Map<String, Provider> providers = new ConcurrentHashMap<>();

	private List<ProviderRegistryListener> listeners = new ArrayList<>(2);
	
	protected void addProvider(Provider p) {
		addProviderInternal(p);
		fireRefresh();
	}
	
	private void addProviderInternal(Provider provider) {
		if (providers.putIfAbsent(provider.getName(), provider) != null) {
			providers.replace(provider.getName(), provider);
		}
	}

	public Provider getProvider(String name) {
		return providers.get(name);
	}

	public static String canonicalizeName(String name) {
		return (name != null) ? name.toLowerCase() : "";
	}

	public Collection<Provider> getProviders() {
		return providers.values();
	}

	protected synchronized void replaceAll(Collection<Provider> providers) {
		this.providers.clear();
		if (providers != null) {
			providers.forEach(p -> {
				addProvider(p);
			});
		}
		fireRefresh();
		LOG.debug("Providers:" + this.providers);
	}

	@Override
	public Iterator<Provider> iterator() {
		return getProviders().iterator();
	}
	
	public void addListener(ProviderRegistryListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(ProviderRegistryListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}
	
	
	protected void fireRefresh() {
		for (ProviderRegistryListener listener: listeners) {
			listener.registryUpdated();
		}
	}
	
}
