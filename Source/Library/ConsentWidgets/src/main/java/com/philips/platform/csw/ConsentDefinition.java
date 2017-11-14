package com.philips.platform.csw;

import java.util.Locale;

public class ConsentDefinition {
    private String text;
    private String helpText;
    private String type;
    private int version;
    private String locale;

    public ConsentDefinition(String text, String helpText, String type, int version, Locale locale) {
        this.text = text;
        this.helpText = helpText;
        this.type = type;
        this.version = version;
        this.locale = locale.toString().replace('_','-');
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
