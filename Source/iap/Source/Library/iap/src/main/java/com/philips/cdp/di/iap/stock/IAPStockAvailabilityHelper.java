/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.cdp.di.iap.stock;

public class IAPStockAvailabilityHelper {

    final String IN_STOCK = "inStock";

    public boolean isStockAvailable(String stockLevelStatus, int stockLevel) {
        if(stockLevelStatus!=null && stockLevelStatus.equalsIgnoreCase(IN_STOCK)){
            return true;
        }else if(stockLevel > 0){
            return true;
        }
        return false;
    }

    public boolean checkIfRequestedQuantityAvailable(String stockLevelStatus, int stockLevel, int quantity) {
        final boolean stockAvailable = isStockAvailable(stockLevelStatus, stockLevel);
        if(stockAvailable){
            if(stockLevel>quantity){
                return true;
            }
        }
        return false;
    }
}
