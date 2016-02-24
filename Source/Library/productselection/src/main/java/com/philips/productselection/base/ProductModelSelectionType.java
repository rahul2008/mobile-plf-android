package com.philips.productselection.base;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;

/**
 * Created by naveen@philips.com on 12-Feb-16.
 */
public abstract class ProductModelSelectionType {

    public Catalog mCatalog;
    public Sector mSector;
    public SummaryModel[] mSummaryModelList;

    public void setCatalog(Catalog catalog) {
        this.mCatalog = catalog;
    }

    public void setSector(Sector sector) {
        this.mSector = sector;
    }


    public String getSector() {
        return mSector.toString();
    }

    public String getCatalog() {
        return mCatalog.toString();
    }

    public void setProductModelList(SummaryModel[] summaryModelList) {
        this.mSummaryModelList = summaryModelList;
    }

    public SummaryModel[] getSummaryModelList() {
        return this.mSummaryModelList;
    }


    public String[] mCtnList = null;



    public void setHardCodedProductList(String[] ctnList) {
        this.mCtnList = ctnList;
    }

    public String[] getHardCodedProductList() {
        return this.mCtnList;
    }
}
