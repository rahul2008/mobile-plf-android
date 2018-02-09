package com.philips.platform.pif.chi.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.philips.platform.pif.chi.ConsentDefinitionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConsentDefinition implements Parcelable {
    private String text;
    private String helpText;
    private List<String> types;
    private int version;
    private String locale;
    private List<String> implicitConsents;

    public ConsentDefinition(String text, String helpText, List<String> types, int version, Locale locale) {
        validate(locale);
        this.text = text;
        this.helpText = helpText;
        this.types = types;
        this.version = version;
        this.locale = locale.toLanguageTag();
        this.implicitConsents = new ArrayList<>();
    }

    private void validate(Locale locale) {
        if (locale == null) {
            throw new ConsentDefinitionException("locale not defined");
        }
        String country = locale.getCountry();
        if (country == null || country.isEmpty()) {
            throw new ConsentDefinitionException("incorrect locale country");
        }
        String language = locale.getLanguage();
        if (language == null || language.isEmpty()) {
            throw new ConsentDefinitionException("incorrect locale language");
        }
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

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(String type) {
        this.types = types;
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

    public List<String> getImplicitConsents() {
        return implicitConsents;
    }

    public void setImplicitConsents(List<String> implicitConsents) {
        this.implicitConsents = implicitConsents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(helpText);
        parcel.writeStringList(types);
        parcel.writeInt(version);
        parcel.writeString(locale);
        parcel.writeStringList(implicitConsents);
    }

    protected ConsentDefinition(Parcel in) {
        text = in.readString();
        helpText = in.readString();
        types = new ArrayList<>();
        in.readStringList(types);
        version = in.readInt();
        locale = in.readString();
        implicitConsents = new ArrayList<>();
        in.readStringList(implicitConsents);
    }

    public static final Creator<ConsentDefinition> CREATOR = new Creator<ConsentDefinition>() {
        @Override
        public ConsentDefinition createFromParcel(Parcel in) {
            return new ConsentDefinition(in);
        }

        @Override
        public ConsentDefinition[] newArray(int size) {
            return new ConsentDefinition[size];
        }
    };

}
