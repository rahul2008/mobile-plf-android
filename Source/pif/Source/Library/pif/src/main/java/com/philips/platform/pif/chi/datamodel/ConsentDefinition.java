package com.philips.platform.pif.chi.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ConsentDefinition implements Parcelable, Serializable {
    private String identifier;
    private String text;
    private String helpText;
    private List<String> types;
    private int version;

    public ConsentDefinition(String text, String helpText, List<String> types, int version) {
        this.text = text;
        this.helpText = helpText;
        this.types = types;
        this.version = version;
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
    }

    protected ConsentDefinition(Parcel in) {
        identifier = in.readString();
        text = in.readString();
        helpText = in.readString();
        types = new ArrayList<>();
        in.readStringList(types);
        version = in.readInt();
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
        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null)
            return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (helpText != null ? !helpText.equals(that.helpText) : that.helpText != null)
            return false;
        return types != null ? types.equals(that.types) : that.types == null;
    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (helpText != null ? helpText.hashCode() : 0);
        result = 31 * result + (types != null ? types.hashCode() : 0);
        result = 31 * result + version;
        return result;
    }
}
