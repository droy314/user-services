package net.deepuroy.services.providers;

public interface ProviderFilter {
	
	boolean test(String serviceId);
	
	String name(String serviceId);
	
}
