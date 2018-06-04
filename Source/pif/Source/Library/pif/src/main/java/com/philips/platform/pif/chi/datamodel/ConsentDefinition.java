package com.philips.platform.pif.chi.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsentDefinition implements Parcelable, Serializable {
    private String identifier;
    private int text;
    private int helpText;
    private List<String> types;
    private int version;
    private int revokeWarningTextRes;

    public ConsentDefinition(int textRes, int helpTextRes, List<String> types, int version) {
        this.text = textRes;
        this.helpText = helpTextRes;
        this.types = types;
        this.version = version;
        this.revokeWarningTextRes = 0;
    }

    public ConsentDefinition(int text, int helpText, List<String> types, int version, int revokeWarningText) {
        this(text, helpText, types, version);
        this.revokeWarningTextRes = revokeWarningText;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    public int getHelpText() {
        return helpText;
    }

    public void setHelpText(int helpText) {
        this.helpText = helpText;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
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
        parcel.writeInt(text);
        parcel.writeInt(helpText);
        parcel.writeStringList(types);
        parcel.writeInt(version);
    }

    protected ConsentDefinition(Parcel in) {
        identifier = in.readString();
        text = in.readInt();
        helpText = in.readInt();
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

    public boolean hasRevokeWarningText() {
        return this.revokeWarningTextRes > 0;
    }

    public int getRevokeWarningText() {
        return revokeWarningTextRes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsentDefinition that = (ConsentDefinition) o;

        if (version != that.version) return false;
        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null)
            return false;
        if (text != that.text) return false;
        if (helpText != that.helpText)
            return false;
        return types != null ? types.equals(that.types) : that.types == null;
    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + text;
        result = 31 * result + helpText;
        result = 31 * result + (types != null ? types.hashCode() : 0);
        result = 31 * result + version;
        return result;
    }
}
