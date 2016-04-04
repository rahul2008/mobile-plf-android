
package com.philips.cdp.registration.errormapping;

import java.util.ArrayList;

public class CheckLocale {


    static ArrayList<String> stadardLocales = new ArrayList<>();

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
    }

    public CheckLocale() {

    }

    public String checkLanguage(String locale) {
        return getStadardLocale(locale);
    }

    private String getStadardLocale(String locale) {
        if (!stadardLocales.contains(locale)) {
            return "en-US";
        }
        return locale;
    }
}
