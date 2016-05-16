package net.deepuroy.services.users.google;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

@Controller
@RefreshScope
public class GoogleController {

	private static final Logger LOG = LogManager.getLogger(GoogleController.class);

	@Value("${apis.google.key}")
	private String apiKey;

	@Autowired
	private GoogleIdTokenVerifier idTokenVerifier;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(path = "/signin-panel", method = RequestMethod.GET)
	public ModelAndView getComponent(@RequestParam(value = "uri", required = false) String uriPrefix,
			@RequestParam(value = "redirect_uri", required = false) String redirectUri,
			@RequestParam(value = "state", required = false) String state) {
		ModelAndView mv = new ModelAndView("signinbutton");
		mv.addObject("uri", uriPrefix);
		mv.addObject("apiKey", apiKey);
		mv.addObject("redirectUri", redirectUri);
		mv.addObject("state", state);
		return mv;
	}

	@RequestMapping(path = "/verify", method = RequestMethod.POST)
	@ResponseBody
	public String verify(@RequestBody String idTokenString) throws GeneralSecurityException, IOException {
		GoogleIdToken token = idTokenVerifier.verify(idTokenString);
		String email = token.getPayload().getEmail();
		LOG.debug("Received email:" + email);
		return email;
	}

}
