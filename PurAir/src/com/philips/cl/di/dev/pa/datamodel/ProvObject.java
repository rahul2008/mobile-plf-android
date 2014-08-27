package com.philips.cl.di.dev.pa.datamodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProvObject {
	Map<String, List<String>> myString;

	/**
	 * @return the myString
	 */
	public HashMap<String, List<String>> getMyString() {
		return (HashMap<String, List<String>>) myString;
	}

	/**
	 * @param myString
	 *            the myString to set
	 */
	public void setMyString(HashMap<String, List<String>> pmyString) {
		this.myString = pmyString;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
