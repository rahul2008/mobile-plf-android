
package com.philips.cdp.registration.errormapping;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;

import java.util.ArrayList;

public class CheckLocale {


    static ArrayList<String> stadardLocales = new ArrayList<>();
    static ArrayList<String> coppaLocales = new ArrayList<>();

    static {
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
        if (RegistrationConfiguration.getInstance().isCoppaFlow()) {
            return getCoppaLocale(locale);
        }

        return getStadardLocale(locale);

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
