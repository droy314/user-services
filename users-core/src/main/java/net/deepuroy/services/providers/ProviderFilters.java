package net.deepuroy.services.providers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProviderFilters {

	private static final class RegexFilter implements ProviderFilter {

		private String providerRegexPattern = "^[^-]*-provider-(.*)";
		private Pattern pattern;
		private int nameGroupNumber;

		RegexFilter(String regex, int groupNumber) {
			this.providerRegexPattern = regex;
			this.nameGroupNumber = groupNumber;
			pattern = Pattern.compile(providerRegexPattern);
		}

		@Override
		public boolean test(String serviceId) {
			Matcher m = pattern.matcher(serviceId);
			return m.matches();
		}

		@Override
		public String name(String serviceId) {
			Matcher m = pattern.matcher(serviceId);
			if (m.matches()) {
				return m.group(nameGroupNumber);
			}
			return "unknown";
		}
	}

	private ProviderFilters() {
		// Prevent instantiation.
	}

	public static ProviderFilter regex(String regex, int nameGroupIndex) {
		return new RegexFilter(regex, nameGroupIndex);
	}

}
