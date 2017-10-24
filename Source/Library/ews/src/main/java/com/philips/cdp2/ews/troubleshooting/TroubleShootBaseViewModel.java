package com.philips.cdp2.ews.troubleshooting;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.util.StringProvider;

import javax.inject.Inject;

/*
 * Copyright (c) Mobiquityinc, 2017.
 * All rights reserved.
 */
public class TroubleShootBaseViewModel {

    @NonNull public final ObservableField<String> title;
    @NonNull public final ObservableField<String> description;
    @NonNull private StringProvider stringProvider;
    @NonNull public final Drawable resetConnectionImage;

    public TroubleShootBaseViewModel(@NonNull StringProvider stringProvider, @NonNull BaseContentConfiguration contentConfiguration,
                                                   @NonNull TroubleShootContentConfiguration troubleShootContentConfiguration) {
        this.stringProvider = stringProvider;
        this.title = new ObservableField<>(getTitle(troubleShootContentConfiguration, contentConfiguration));
        this.description = new ObservableField<>(getNote(contentConfiguration));
        this.resetConnectionImage = getResetConnectionImage(troubleShootContentConfiguration);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @NonNull
    Drawable getResetConnectionImage(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration) {
        return stringProvider.getImageResource(troubleShootContentConfiguration.getResetConnectionImage());
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @NonNull
    String getTitle(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                    @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getResetConnectionTitle(),
                baseConfig.getDeviceName());
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @NonNull
    String getNote(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_support_reset_connection_title_default,
                baseConfig.getDeviceName());
    }

    /*@NonNull
    public ObservableField<String> getTitle() {
        return title;
    }

    @NonNull
    public ObservableField<String> getDescription() {
        return description;
    }

    @NonNull
    public Drawable getResetConnectionImage() {
        return resetConnectionImage;
    }*/


}
