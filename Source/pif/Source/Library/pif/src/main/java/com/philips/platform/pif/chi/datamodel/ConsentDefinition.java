package com.philips.platform.pif.chi.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.philips.platform.pif.chi.ConsentDefinitionException;
import java.util.Locale;
public class ConsentDefinition implements Parcelable, Serializable {
    private String identifier;
    private String text;
    private String helpText;
    private List<String> types;
    private int version;
    private List<String> implicitConsents;

    public ConsentDefinition(String text, String helpText, List<String> types, int version) {
        this.text = text;
        this.helpText = helpText;
        this.types = types;
        this.version = version;
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
        parcel.writeString(identifier);
        parcel.writeString(text);
        parcel.writeString(helpText);
        parcel.writeStringList(types);
        parcel.writeInt(version);
        parcel.writeStringList(implicitConsents);
    }

    protected ConsentDefinition(Parcel in) {
        identifier = in.readString();
        text = in.readString();
        helpText = in.readString();
        types = new ArrayList<>();
        in.readStringList(types);
        version = in.readInt();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsentDefinition that = (ConsentDefinition) o;

        if (version != that.version) return false;
        if (!identifier.equals(that.identifier)) return false;
        if (!text.equals(that.text)) return false;
        if (!helpText.equals(that.helpText)) return false;
        if (!types.equals(that.types)) return false;
        return implicitConsents.equals(that.implicitConsents);
    }

    @Override
    public int hashCode() {
        int result = identifier.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + helpText.hashCode();
        result = 31 * result + types.hashCode();
        result = 31 * result + version;
        result = 31 * result + implicitConsents.hashCode();
        return result;
    }
}
