/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.languagepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * The List of Language setter and getter class.
 */

public class LanguageList {

	@SerializedName("languages")
	@Expose
	private ArrayList<LanguagePackModel> languages;

	public ArrayList<LanguagePackModel> getLanguages() {
		return languages;
	}

	public void setLanguages(ArrayList<LanguagePackModel> languages) {
		this.languages = languages;
	}

}
