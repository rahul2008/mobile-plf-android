/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.cdp.di.iap.stock;

public interface IStockInterface {
    boolean isStockAvailable(String stockLevelStatus, int stockLevel);
    boolean checkIfRequestedQuantityAvailable(String stockLevelStatus, int stockLevel, int pQuantity);
}
