/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.troubleshooting.connectionfailure;

import android.support.annotation.Nullable;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class ConnectionUnsuccessfulViewModel {

    public static final int HELP_NEEDED_RESULT = 1;
    public static final int HELP_NOT_NEEDED_RESULT = 2;

    @Nullable private UnsuccessfulConnectionCallback callback;

    @Inject
    public ConnectionUnsuccessfulViewModel() {
    }

    public void setCallback(@Nullable UnsuccessfulConnectionCallback callback) {
        this.callback = callback;
    }

    public void onNeedHelpButtonClicked() {
        if (callback != null) {
            callback.hideDialogWithResult(HELP_NEEDED_RESULT);
        }
        tagConnectionUnsuccessful();
    }

    public void onNoHelpNeededButtonClicked() {
        if (callback != null) {
            callback.hideDialogWithResult(HELP_NOT_NEEDED_RESULT);
        }
        tagConnectionUnsuccessful();
    }

    private void tagConnectionUnsuccessful() {
        Map<String, String> map = new HashMap<>();
        map.put(Tag.KEY.IN_APP_NOTIFICATION, Tag.VALUE.CONN_ERROR_NOTIFICATION);
        map.put(Tag.KEY.CONNECTED_PRODUCT_NAME, EWSDependencyProvider.getInstance().getProductName());
        map.put(Tag.KEY.TECHNICAL_ERROR, Tag.VALUE.WIFI_SINGLE_ERROR);

        EWSTagger.trackAction(Tag.ACTION.CONNECTION_UNSUCCESSFUL, map);
    }
}
