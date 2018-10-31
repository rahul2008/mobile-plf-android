/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerslist;

import java.util.List;

public interface THSProviderListViewInterface {
    void updateProviderAdapterList(List<THSProviderInfo> providerInfos);
    void updateMainView(boolean isOnline);
    void showNoProviderErrorDialog();
}
