package com.philips.cdp.sampledigitalcare.products;

import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
//import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
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
