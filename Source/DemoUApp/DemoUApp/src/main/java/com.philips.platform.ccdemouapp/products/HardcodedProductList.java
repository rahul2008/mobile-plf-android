package com.philips.platform.ccdemouapp.products;

import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;

/**
 * Created by naveen@philips.com on 12-Feb-16.
 */
public class HardcodedProductList extends ProductModelSelectionType {

    public String[] mCtnList = null;

    public HardcodedProductList(String[] ctnList) {
        this.mCtnList = ctnList;
    }

    @Override

    public String[] getHardCodedProductList() {
        return this.mCtnList;
    }
}
