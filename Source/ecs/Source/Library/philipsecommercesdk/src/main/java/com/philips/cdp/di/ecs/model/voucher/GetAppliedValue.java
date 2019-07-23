/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.voucher;


import java.util.List;

public class GetAppliedValue {

    private List<Vouchers> vouchers;

    public void setVouchers(List<Vouchers> vouchers) {
        this.vouchers = vouchers;
    }

    public List<Vouchers> getVouchers() {
        return vouchers;
    }
}
