/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;

import javax.inject.Inject;

public class WIFIConnectionUnsuccessfulViewModel {

    @NonNull
    public final ObservableField<String> description;

    @NonNull
    public final ObservableField<String> notes;

    @NonNull
    private final Navigator navigator;

    @Inject
    public WIFIConnectionUnsuccessfulViewModel(@NonNull Navigator navigator) {
        this.navigator = navigator;
        description = new ObservableField<>();
        notes = new ObservableField<>();
    }

    public void setDescription(@NonNull String description) {
        this.description.set(description);
    }

    public void setNotes(@NonNull String notes) {
        this.notes.set(notes);
    }

    public void onTryAgainClicked() {
        navigator.navigateToHomeNetworkConfirmationScreen();
    }

    void trackPageName() {
        EWSTagger.trackPage(Page.CONNECTION_UNSUCCESSFUL);
    }
}
