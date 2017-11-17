/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.startconnectwithdevice;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.communication.EventingChannel;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.util.StringProvider;

import javax.inject.Inject;

public class StartConnectWithDeviceViewModel {

    @NonNull
    public final ObservableField<String> title;
    @NonNull
    public final ObservableField<String> note;
    @NonNull
    public final ObservableField<String> description;
    @NonNull
    public final Drawable image;
    @NonNull
    private final Navigator navigator;
    @NonNull
    private final StringProvider stringProvider;
    @VisibleForTesting
    @Inject
    EventingChannel<EventingChannel.ChannelCallback> ewsEventingChannel;

    @Inject
    public StartConnectWithDeviceViewModel(@NonNull final Navigator navigator,
                                           @NonNull final StringProvider stringProvider,
                                           @NonNull final HappyFlowContentConfiguration happyFlowConfig,
                                           @NonNull final BaseContentConfiguration baseConfig) {
        this.navigator = navigator;
        this.stringProvider = stringProvider;
        title = new ObservableField<>(getTitle(happyFlowConfig, baseConfig));
        note = new ObservableField<>(getNote(baseConfig));
        description = new ObservableField<>(getDescription(baseConfig));
        image = getImage(happyFlowConfig);
    }

    @VisibleForTesting
    @NonNull
    public String getTitle(@NonNull HappyFlowContentConfiguration happyFlowConfig,
                           @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(happyFlowConfig.getGettingStartedScreenTitle(),
                baseConfig.getDeviceName());
    }

    @VisibleForTesting
    @NonNull
    public String getDescription(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_get_started_description,
                baseConfig.getDeviceName());
    }

    @VisibleForTesting
    @NonNull
    public String getNote(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_get_started_note,
                baseConfig.getDeviceName());
    }

    @NonNull
    @VisibleForTesting
    Drawable getImage(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getImageResource(happyFlowContentConfiguration.getGettingStartedScreenImage());
    }

    public void onGettingStartedButtonClicked() {
        tapGetStarted();
        navigator.navigateToHomeNetworkConfirmationScreen();
    }

    private void tapGetStarted() {
        EWSTagger.trackActionSendData(Tag.KEY.SPECIAL_EVENTS, Tag.ACTION.GET_STARTED);
    }

    public void onBackPressed(EWSCallbackNotifier ewsCallbackNotifier) {
        ewsCallbackNotifier.onBackPressed();
    }

    public void trackPageName() {
        EWSTagger.trackPage(Page.GET_STARTED);
    }

    public void onDestroy(){
        if(navigator.getFragmentNavigator().shouldFinish()){
            EWSTagger.pauseLifecycleInfo();
            ewsEventingChannel.stop();
            EWSDependencyProvider.getInstance().clear();
        }

    }
}