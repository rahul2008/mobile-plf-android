package com.philips.cl.di.dev.pa.registration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

	private  static final String EMAIL_PATTERN = 
			"^[A-Za-z0-9._%+\\-]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]{2,30}+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$";
	
	public static boolean validate(final String email) {
		if(email == null) return false;
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
}



