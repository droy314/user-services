package net.deepuroy.services.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import net.deepuroy.services.providers.Provider;
import net.deepuroy.services.providers.ProviderRegistry;

@Controller
public class SigninController {

	@Autowired
	private RestTemplate restOperations;

	@Autowired
	private ProviderRegistry registry;

	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public ModelAndView getLoginPage() {
		ModelAndView mv = new ModelAndView("login");
		List<String> templates = new ArrayList<String>();
		for (Provider provider : registry) {
			templates.add(getPageFragment(provider));
		}
		mv.addObject("signinPanels", templates);
		return mv;
	}

	private String getPageFragment(Provider provider) {
		UserProvider up = new UserProvider(provider, restOperations);
		return up.getSigninPanel("/providers/" + provider.getName(), "/verified");
	}

}
