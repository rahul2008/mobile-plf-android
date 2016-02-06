
package com.philips.cdp.registration.errormapping;

import com.philips.cdp.registration.settings.RegistrationHelper;

import java.util.ArrayList;

public class CheckLocale {


    static ArrayList<String> stadardLocales = new ArrayList<>();
    static ArrayList<String> coppaLocales = new ArrayList<>();

    static void populateLocales() {

        stadardLocales.add("bg-BG");
        stadardLocales.add("da-DK");
        stadardLocales.add("de-AT");
        stadardLocales.add("de-CH");
        stadardLocales.add("de-DE");
        stadardLocales.add("el-GR");
        stadardLocales.add("en-AU");
        stadardLocales.add("en-CA");
        stadardLocales.add("en-GB");
        stadardLocales.add("en-HK");
        stadardLocales.add("en-ID");
        stadardLocales.add("en-IE");
        stadardLocales.add("en-IN");
        stadardLocales.add("en-MY");
        stadardLocales.add("en-NZ");
        stadardLocales.add("en-PH");
        stadardLocales.add("en-PK");
        stadardLocales.add("en-SA");
        stadardLocales.add("en-SG");
        stadardLocales.add("en-US");
        stadardLocales.add("en-ZA");
        stadardLocales.add("es-AR");
        stadardLocales.add("es-CL");
        stadardLocales.add("es-CO");
        stadardLocales.add("es-ES");
        stadardLocales.add("es-MX");
        stadardLocales.add("es-PA");
        stadardLocales.add("es-PE");
        stadardLocales.add("es-UY");
        stadardLocales.add("es-VE");
        stadardLocales.add("et-EE");
        stadardLocales.add("fi-FI");
        stadardLocales.add("fr-BE");
        stadardLocales.add("fr-CA");
        stadardLocales.add("fr-CH");
        stadardLocales.add("fr-FR");
        stadardLocales.add("hr-HR");
        stadardLocales.add("hu-HU");
        stadardLocales.add("it-IT");
        stadardLocales.add("ja-JP");
        stadardLocales.add("ko-KR");
        stadardLocales.add("lt-LT");
        stadardLocales.add("lv-LV");
        stadardLocales.add("nl-BE");
        stadardLocales.add("nl-NL");
        stadardLocales.add("no-NO");
        stadardLocales.add("pl-PL");
        stadardLocales.add("pt-BR");
        stadardLocales.add("pt-PT");
        stadardLocales.add("ro-RO");
        stadardLocales.add("ru-RU");
        stadardLocales.add("ru-UA");
        stadardLocales.add("sk-SK");
        stadardLocales.add("sl-SI");
        stadardLocales.add("sv-SE");
        stadardLocales.add("th-TH");
        stadardLocales.add("tr-TR");
        stadardLocales.add("vi-VN");
        stadardLocales.add("zh-CN");
        stadardLocales.add("zh-HK");
        stadardLocales.add("zh-TW");

        //COPPA LOCALES
        coppaLocales.add("bg-BG");
        coppaLocales.add("cs-CZ");
        coppaLocales.add("da-DK");
        coppaLocales.add("de-DE");
        coppaLocales.add("en-GB");
        coppaLocales.add("es-MX");
        coppaLocales.add("et-EE");
        coppaLocales.add("fi-FI");
        coppaLocales.add("fr-CA");
        coppaLocales.add("fr-FR");
        coppaLocales.add("hu-HU");
        coppaLocales.add("it-IT");
        coppaLocales.add("ja-JP");
        coppaLocales.add("ko-KR");
        coppaLocales.add("lt-LT");
        coppaLocales.add("lv-LV");
        coppaLocales.add("nb-NO");
        coppaLocales.add("nl-NL");
        coppaLocales.add("pl-PL");
        coppaLocales.add("ro-RO");
        coppaLocales.add("ru-RU");
        coppaLocales.add("sk-SK");
        coppaLocales.add("sl-SI");
        coppaLocales.add("sv-SE");
        coppaLocales.add("uk-UA");
        coppaLocales.add("zh-CN");
        coppaLocales.add("zh-TW");
    }

    public CheckLocale() {

    }

    public String checkLanguage(String locale) {
        if (RegistrationHelper.getInstance().isCoppaFlow()) {
            return getCoppaLocale(locale);
        }

        return getStadardLocale(locale);

/*		if (!(locale.equals("bg-BG")) && !(locale.equals("da-DK")) && !(locale.equals("de-AT"))
                && !(locale.equals("de-CH")) && !(locale.equals("de-DE"))

		        && !(locale.equals("el-GR"))
				&& !(locale.equals("en-AU"))
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
		        && !(locale.equals("es-VE"))

				&& !(locale.equals("et-EE"))
		        && !(locale.equals("fi-FI")) && !(locale.equals("fr-BE"))
		        && !(locale.equals("fr-CA")) && !(locale.equals("fr-CH"))
		        && !(locale.equals("fr-FR")) && !(locale.equals("hr-HR"))
		        && !(locale.equals("hu-HU")) && !(locale.equals("it-IT"))

		        && !(locale.equals("ja-JP")) && !(locale.equals("ko-KR"))
		        && !(locale.equals("lt-LT")) && !(locale.equals("lv-LV"))
		        && !(locale.equals("nl-BE")) && !(locale.equals("nl-NL"))
		        && !(locale.equals("no-NO")) && !(locale.equals("pl-PL"))
		        && !(locale.equals("pt-BR")) && !(locale.equals("pt-PT"))
				//Done
		        && !(locale.equals("ro-RO")) && !(locale.equals("ru-RU"))
		        && !(locale.equals("ru-UA")) && !(locale.equals("sk-SK"))
		        && !(locale.equals("sl-SI")) && !(locale.equals("sv-SE"))
		        && !(locale.equals("th-TH")) && !(locale.equals("tr-TR"))

		        && !(locale.equals("vi-VN")) && !(locale.equals("zh-CN"))
		        && !(locale.equals("zh-HK")) && !(locale.equals("zh-TW"))) {
			mLanguageCode = "en-US";
		} else {
			mLanguageCode = locale;
		}*/
        //  return mLanguageCode;
    }

    private String getStadardLocale(String locale) {
        if (!stadardLocales.contains(locale)) {
            return "en-US";
        }
        return locale;
    }

    private String getCoppaLocale(String locale) {
        if (!coppaLocales.contains(locale)) {
            return "en-US";
        }
        return locale;
    }
}
