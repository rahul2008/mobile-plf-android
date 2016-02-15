package com.philips.cdp.sampledigitalcareapp.products;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.multiproduct.ProductModelSelectionHelper;
import com.philips.multiproduct.base.ProductModelSelectionType;

/**
 * Created by naveen@philips.com on 12-Feb-16.
 */
public class HardcodedProductList extends ProductModelSelectionType {

    public String[] mCtnList = null;

    @Override

    public void setHardCodedProductList(String[] ctnList) {
        this.mCtnList = ctnList;
    }

    @Override

    public String[] getHardCodedProductList() {
        return this.mCtnList;
    }
}
