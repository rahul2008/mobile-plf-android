/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.response.addresses;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliveryModes implements Parcelable {
    private String code;
    private DeliveryCost deliveryCost;
    private String description;
    private String name;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    //Added to write test cases
    public DeliveryModes(){

    }

    protected DeliveryModes(Parcel in) {
        code = in.readString();
        description = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(description);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryModes> CREATOR = new Creator<DeliveryModes>() {
        @Override
        public DeliveryModes createFromParcel(Parcel in) {
            return new DeliveryModes(in);
        }

        @Override
        public DeliveryModes[] newArray(int size) {
            return new DeliveryModes[size];
        }
    };

    public void setCode(String code) {
        this.code = code;
    }

    public void setDeliveryCost(DeliveryCost deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public DeliveryCost getDeliveryCost() {
        return deliveryCost;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

}
