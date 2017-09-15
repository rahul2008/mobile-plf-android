/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.VisibleForTesting;
import android.support.v4.app.DialogFragment;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class ConnectionUnsuccessfulViewModel {

    private DialogFragment dialogDismissListener;

    @Inject
    ConnectionUnsuccessfulViewModel() {
    }

    public void dismissDialog() {
        tagConnectionUnsuccessful();
        dialogDismissListener.dismissAllowingStateLoss();
    }

    private void tagConnectionUnsuccessful() {
        Map<String, String> map = new HashMap<>();
        map.put(Tag.KEY.IN_APP_NOTIFICATION, Tag.VALUE.CONN_ERROR_NOTIFICATION);
        map.put(Tag.KEY.CONNECTED_PRODUCT_NAME, EWSDependencyProvider.getInstance().getProductName());
        map.put(Tag.KEY.TECHNICAL_ERROR, Tag.VALUE.WIFI_SINGLE_ERROR);

        EWSTagger.trackAction(Tag.ACTION.CONNECTION_UNSUCCESSFUL, map);
    }

    public void setDialogDismissListener(final DialogFragment dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }

    public void removeDialogDismissListener() {
        this.dialogDismissListener = null;
    }

    @VisibleForTesting
    public DialogFragment getDialogDismissListener() {
        return dialogDismissListener;
    }
}
