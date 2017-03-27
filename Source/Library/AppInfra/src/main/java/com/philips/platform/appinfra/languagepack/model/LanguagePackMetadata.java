package com.philips.platform.appinfra.languagepack.model;

/**
 * Created by Yogesh on 3/27/17.
 */

public class LanguagePackMetadata {

    private String locale;
    private String version;
    private String url;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
