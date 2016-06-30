/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.appframework.consumercare;

import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;

/**
 *
 * @author: ritesh.jha@philips.com
 * @since: June 30, 2016
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
