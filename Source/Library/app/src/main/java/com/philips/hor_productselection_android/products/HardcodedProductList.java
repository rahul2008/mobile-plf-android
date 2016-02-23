package com.philips.hor_productselection_android.products;

import com.philips.productselection.base.ProductModelSelectionType;

/**
 * Created by naveen@philips.com on 12-Feb-16.
 */
public class HardcodedProductList extends ProductModelSelectionType {

    public String[] mCtnList = null;



    public void setHardCodedProductList(String[] ctnList) {
        this.mCtnList = ctnList;
    }

    @Override

    public String[] getHardCodedProductList() {
        return this.mCtnList;
    }
}
