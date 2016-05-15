package net.deepuroy.services.providers.discovery;

import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;

import net.deepuroy.services.providers.ProviderRegistryListener;

public class ZuulMappingHandlerUpdater implements ProviderRegistryListener {

	private ZuulHandlerMapping zmh;

	public ZuulMappingHandlerUpdater(ZuulHandlerMapping zhm) {
		this.zmh = zhm;
	}

	@Override
	public void registryUpdated() {
		zmh.setDirty(true);
	}

}
