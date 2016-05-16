package net.deepuroy.services.users;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import net.deepuroy.services.providers.ProviderRegistry;

@Controller
public class SigninController {

	@Autowired
	private RestTemplate restOperations;

	@Autowired
	private ProviderRegistry registry;

	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public ModelAndView getLoginPage(@RequestParam(value = "redirect_uri", required = false) String redirectUri)
			throws UnsupportedEncodingException {
		String state = encrypt(redirectUri);
		ModelAndView mv = new ModelAndView("login");
		mv.addObject("state", state);
		mv.addObject("providers", registry.getProviders());
		return mv;
	}

	@RequestMapping(path = "/encrypt", method = RequestMethod.POST)
	@ResponseBody
	public String encrypt(@RequestBody String email) throws UnsupportedEncodingException {
		return URLEncoder.encode(Base64.getEncoder().encodeToString(email.getBytes()), "UTF-8");
	}

	@RequestMapping(path = "/verified", method = RequestMethod.GET)
	public View verified(@RequestParam(value = "u", required = true) String userEmail,
			@RequestParam(value = "state", required = true) String state) throws UnsupportedEncodingException {
		String url = decrypt(state) + "?user=" + userEmail;
		return new RedirectView(url, false);
	}

	private String decrypt(String encrypted) throws UnsupportedEncodingException {
		return new String(Base64.getDecoder().decode(URLDecoder.decode(new String(encrypted.getBytes()), "UTF-8")));
	}

}
