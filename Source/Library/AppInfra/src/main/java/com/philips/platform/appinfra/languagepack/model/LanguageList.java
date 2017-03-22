package com.philips.platform.appinfra.languagepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gkavya on 3/21/17.
 */

public class LanguageList {

	@SerializedName("languages")
	@Expose
	private ArrayList<LanguageModel> languages;

	public ArrayList<LanguageModel> getLanguages() {
		return languages;
	}

	public void setLanguages(ArrayList<LanguageModel> languages) {
		this.languages = languages;
	}

}
