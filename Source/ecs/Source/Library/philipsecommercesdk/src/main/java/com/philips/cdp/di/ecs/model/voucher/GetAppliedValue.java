/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.voucher;


import java.util.List;

public class GetAppliedValue {

    private List<ECSVoucher> vouchers;

    public void setVouchers(List<ECSVoucher> vouchers) {
        this.vouchers = vouchers;
    }

    public List<ECSVoucher> getVouchers() {
        return vouchers;
    }
}
