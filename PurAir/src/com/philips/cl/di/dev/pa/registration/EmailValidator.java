package com.philips.cl.di.dev.pa.registration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

	private Pattern pattern;
	private Matcher matcher;
	
	private  final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]{2,35}+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]{2,8}+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$";
	
	private static EmailValidator smInstance;
	
	public static EmailValidator getInstance() {
		synchronized (EmailValidator.class) {

			if (smInstance == null) {
				smInstance = new EmailValidator();
			}
		}
		return smInstance;
	}

	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	
	public boolean validate(final String hex) {
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}
	
}



