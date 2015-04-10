package com.philips.cl.di.reg.ui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtility {

	public static boolean isValidEmail(String email) {
		if (email == null)
			return false;
		if (email.length() == 0)
			return false;
		String emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isValidPassword(String password) {
		if (password == null)
			return false;

		int passwordValidatorCheckCount = 0;

		if (ValidatorUtility.isAlphabetPresent(password)) {
			passwordValidatorCheckCount++;
		}

		if (ValidatorUtility.isNumberPresent(password)) {
			passwordValidatorCheckCount++;
		}

		if (passwordValidatorCheckCount == 2) {
			return true;
		}

		if (ValidatorUtility.isSymbolsPresent(password)) {
			passwordValidatorCheckCount++;
		}

		if (passwordValidatorCheckCount >= 2) {
			return true;
		}

		return false;
	}

	private static boolean isAlphabetPresent(String string) {
		if (string == null)
			return false;

		if (string.length() == 0)
			return false;

		Pattern pattern = Pattern.compile("[a-zA-Z]");
		Matcher matcher = pattern.matcher(string);

		return matcher.find();
	}

	private static boolean isNumberPresent(String string) {
		if (string == null)
			return false;

		if (string.length() == 0)
			return false;

		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher = pattern.matcher(string);
		return matcher.find();
	}

	private static boolean isSymbolsPresent(String string) {
		if (string == null)
			return false;

		if (string.length() == 0)
			return false;

		Pattern pattern = Pattern.compile("[_.@$]");
		Matcher matcher = pattern.matcher(string);
		return matcher.find();
	}

	public static boolean isValidSerialNo(String serialNo) {
		if (serialNo == null)
			return false;

		if (serialNo.length() < 14)
			return false;

		Pattern pattern = Pattern.compile("[a-zA-Z]{2}[\\d]{12}");
		Matcher matcher = pattern.matcher(serialNo);
		return matcher.matches();
	}

}
