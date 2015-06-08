
package com.philips.cl.di.reg.errormapping;

public class CheckLocale {

	String mLanguageCode;

	public CheckLocale() {

	}

	public String checkLanguage(String locale) {

		if (!(locale.equals("bg-BG")) && !(locale.equals("da-DK")) && !(locale.equals("de-AT"))
		        && !(locale.equals("de-CH")) && !(locale.equals("de-DE"))
		        && !(locale.equals("el-GR")) && !(locale.equals("en-AU"))
		        && !(locale.equals("en-CA")) && !(locale.equals("en-GB"))
		        && !(locale.equals("en-HK")) && !(locale.equals("en-ID"))
		        && !(locale.equals("en-IE")) && !(locale.equals("en-IN"))
		        && !(locale.equals("en-MY")) && !(locale.equals("en-NZ"))
		        && !(locale.equals("en-PH")) && !(locale.equals("en-PK"))
		        && !(locale.equals("en-SA")) && !(locale.equals("en-SG"))
		        && !(locale.equals("en-US")) && !(locale.equals("en-ZA"))
		        && !(locale.equals("es-AR")) && !(locale.equals("es-CL"))
		        && !(locale.equals("es-CO")) && !(locale.equals("es-ES"))
		        && !(locale.equals("es-MX")) && !(locale.equals("es-PA"))
		        && !(locale.equals("es-PE")) && !(locale.equals("es-UY"))
		        && !(locale.equals("es-VE")) && !(locale.equals("et-EE"))
		        && !(locale.equals("fi-FI")) && !(locale.equals("fr-BE"))
		        && !(locale.equals("fr-CA")) && !(locale.equals("fr-CH"))
		        && !(locale.equals("fr-FR")) && !(locale.equals("hr-HR"))
		        && !(locale.equals("hu-HU")) && !(locale.equals("it-IT"))
		        && !(locale.equals("ja-JP")) && !(locale.equals("ko-KR"))
		        && !(locale.equals("lt-LT")) && !(locale.equals("lv-LV"))
		        && !(locale.equals("nl-BE")) && !(locale.equals("nl-NL"))
		        && !(locale.equals("no-NO")) && !(locale.equals("pl-PL"))
		        && !(locale.equals("pt-BR")) && !(locale.equals("pt-PT"))
		        && !(locale.equals("ro-RO")) && !(locale.equals("ru-RU"))
		        && !(locale.equals("ru-UA")) && !(locale.equals("sk-SK"))
		        && !(locale.equals("sl-SI")) && !(locale.equals("sv-SE"))
		        && !(locale.equals("th-TH")) && !(locale.equals("tr-TR"))
		        && !(locale.equals("vi-VN")) && !(locale.equals("zh-CN"))
		        && !(locale.equals("zh-HK")) && !(locale.equals("zh-TW"))) {
			mLanguageCode = "en-US";
		} else {
			mLanguageCode = locale;
		}
		return mLanguageCode;
	}

}
