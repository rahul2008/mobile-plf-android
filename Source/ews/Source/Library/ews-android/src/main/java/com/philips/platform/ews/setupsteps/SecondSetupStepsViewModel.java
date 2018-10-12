/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.setupsteps;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.configuration.HappyFlowContentConfiguration;
import com.philips.platform.ews.logger.EWSLogger;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.tagging.Tag;
import com.philips.platform.ews.util.StringProvider;
import javax.inject.Inject;
import static com.philips.platform.ews.EWSActivity.EWS_STEPS;

@SuppressWarnings("WeakerAccess")
public class SecondSetupStepsViewModel {

    @NonNull
    public final ObservableField<String> title;
    @NonNull
    public final ObservableField<String> question;
    @NonNull
    public final Drawable image;
    @NonNull
    public final ObservableField<String> yesButton;
    @NonNull
    public final ObservableField<String> noButton;
    @NonNull
    protected final Navigator navigator;
    @NonNull
    private final BaseContentConfiguration baseContentConfiguration;
    @NonNull
    private final StringProvider stringProvider;
    @NonNull private final EWSTagger ewsTagger;
    @NonNull private final EWSLogger ewsLogger;

    @Inject
    public SecondSetupStepsViewModel(@NonNull final Navigator navigator,
                                     @NonNull final StringProvider stringProvider,
                                     @NonNull final HappyFlowContentConfiguration happyFlowContentConfiguration,
                                     @NonNull final BaseContentConfiguration baseContentConfiguration,
                                     @NonNull final EWSTagger ewsTagger,
                                     @NonNull final EWSLogger ewsLogger) {
        this.stringProvider = stringProvider;
        this.question = new ObservableField<>(getQuestion(happyFlowContentConfiguration));
        this.title = new ObservableField<>(getTitle(happyFlowContentConfiguration));
        this.image = getImage(happyFlowContentConfiguration);
        this.yesButton = new ObservableField<>(getYesButton(happyFlowContentConfiguration));
        this.noButton = new ObservableField<>(getNoButton(happyFlowContentConfiguration));
        this.navigator = navigator;
        this.baseContentConfiguration = baseContentConfiguration;
        this.ewsTagger = ewsTagger;
        this.ewsLogger = ewsLogger;
    }

    @VisibleForTesting
    @NonNull
    String getTitle(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getString(happyFlowContentConfiguration.getSetUpVerifyScreenTitle());
    }

    @VisibleForTesting
    @NonNull
    String getQuestion(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getString(happyFlowContentConfiguration.getSetUpVerifyScreenQuestion());
    }

    @NonNull
    @VisibleForTesting
    Drawable getImage(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getImageResource(happyFlowContentConfiguration.getSetUpVerifyScreenImage());
    }

    @VisibleForTesting
    @NonNull
    String getYesButton(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getString(happyFlowContentConfiguration.getSetUpVerifyScreenYesButton());
    }

    @VisibleForTesting
    @NonNull
    String getNoButton(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getString(happyFlowContentConfiguration.getSetUpVerifyScreenNoButton());
    }

    public void onNextButtonClicked() {
        tapWifiBlinking();
        connectPhoneToDeviceHotspotWifi();
    }

    public void onNoButtonClicked() {
        tapWifiNotBlinking();
        navigator.navigateToResetConnectionTroubleShootingScreen();
    }

    private void tapWifiNotBlinking() {
        ewsTagger.trackActionSendData(Tag.KEY.SPECIAL_EVENTS, Tag.ACTION.WIFI_NOT_BLINKING);
    }

    private void tapWifiBlinking() {
        ewsTagger.trackActionSendData(Tag.KEY.SPECIAL_EVENTS, Tag.ACTION.WIFI_BLINKING);
    }

    protected void startConnection() {

        navigator.navigateToConnectingPhoneToHotspotWifiScreen();
    }

    void trackPageName() {
        ewsTagger.trackPage(Page.SETUP_STEP2);
    }

    public void connectPhoneToDeviceHotspotWifi() {
        ewsLogger.d(EWS_STEPS, "Step 1 : Trying to connect to appliance hot spot");
        startConnection();
    }

    public void showPasswordEntryScreenEvent() {
        navigator.navigateToSelectWiFiScreen();
    }
}