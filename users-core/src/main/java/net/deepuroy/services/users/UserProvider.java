package net.deepuroy.services.users;

import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import net.deepuroy.services.providers.Provider;

public class UserProvider {

	private static final Logger LOG = LogManager.getLogger(UserProvider.class);

	private Provider provider;
	private RestOperations rOps;

	public UserProvider(Provider provider, RestOperations restOperations) {
		this.provider = provider;
		this.rOps = restOperations;
	}

	public String getSigninPanel(String uri, String redirectUri) {
		LOG.debug("Invoking provider" + provider);

		String url = "http://{serviceId}/signin-panel";

		String finalUrl = UriComponentsBuilder.fromUriString(url).queryParam("uri", uri)
				.queryParam("redirect_uri", redirectUri).buildAndExpand(provider.getServiceId()).toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> responseEntity = rOps.exchange(finalUrl, HttpMethod.GET, entity, String.class);
		String response = responseEntity.getBody();
		LOG.debug("Response:" + response);
		return response;
	}

}
