package net.deepuroy.services.providers;

public class Provider {
	
	private final String name;
	
	private final String serviceId;
	
	public Provider(String name, String serviceId) {
		this.name = name;
		this.serviceId = serviceId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getServiceId() {
		return serviceId;
	}

	@Override
	public String toString() {
		return String.format("Provider(name=[%s], serviceId=[%s])", name, serviceId);
	}
	
}
