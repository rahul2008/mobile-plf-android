/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.voucher;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetAppliedValue implements Serializable {

    private List<ECSVoucher> vouchers = new ArrayList<>();

    public void setVouchers(List<ECSVoucher> vouchers) {
        this.vouchers = vouchers;
    }

    public List<ECSVoucher> getVouchers() {
        return vouchers;
    }
}
