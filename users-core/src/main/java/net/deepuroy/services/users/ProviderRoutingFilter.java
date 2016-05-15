package net.deepuroy.services.users;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import net.deepuroy.services.providers.Provider;
import net.deepuroy.services.providers.ProviderRegistry;


public class ProviderRoutingFilter extends ZuulFilter {

	
	private final ProviderRegistry registry;

	public ProviderRoutingFilter(ProviderRegistry registry) {
		System.err.println("Filter created");
		this.registry = registry;
	}

	@Override
	public Object run() {
		System.err.println("In Run");
		if (shouldFilter()) {
			RequestContext ctx = RequestContext.getCurrentContext();
			String requestUri = ctx.getRequest().getRequestURI();
			String[] components = requestUri.split("/");
			if (components.length >= 1) {
				String provider = components[1];
				StringBuilder b = new StringBuilder();
				for (int i = 2; i < components.length; i++) {
					b.append("/").append(components[i]);
				}
				for (Provider p : registry) {
					if (p.getServiceId().endsWith("-" + provider)) {
						ctx.set("serviceId", p.getServiceId());
						ctx.set("requestURI", b.toString());
						break;
					}
				}

			}

		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		String uri = RequestContext.getCurrentContext().getRequest().getRequestURI();
		System.err.println("Should filter:" + uri);
		return uri.startsWith("/providers/");
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		return "pre";
	}

}
