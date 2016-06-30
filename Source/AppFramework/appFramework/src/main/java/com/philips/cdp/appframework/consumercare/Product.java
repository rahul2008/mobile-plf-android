/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.cdp.appframework.consumercare;

/**
 *
 * @author: ritesh.jha@philips.com
 * @since: June 30, 2016
 */
public class Product {

    private String mCtn = null;
    private String mCategory = null;
    private String mCatalog = null;


    public String getmCtn() {
        return mCtn;
    }

    public void setmCtn(String mCtn) {
        this.mCtn = mCtn;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmCatalog() {
        return mCatalog;
    }

    public void setmCatalog(String mCatalog) {
        this.mCatalog = mCatalog;
    }
}

