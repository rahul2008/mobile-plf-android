package com.philips.cdp.di.ecs.model.cart;


import com.philips.cdp.di.ecs.model.voucher.AppliedValue;

import java.io.Serializable;

/**
 * The type Applied voucher entity which contains voucher related data.
 */
public class AppliedVoucherEntity  implements Serializable {

    String description;
    String freeShipping;
    String name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(String freeShipping) {
        this.freeShipping = freeShipping;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueFormatted() {
        return valueFormatted;
    }

    public void setValueFormatted(String valueFormatted) {
        this.valueFormatted = valueFormatted;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    String valueFormatted;
    String valueString;
    String voucherCode;

    AppliedValue appliedValue;

    public AppliedValue getAppliedValue() {
        return appliedValue;
    }
}
