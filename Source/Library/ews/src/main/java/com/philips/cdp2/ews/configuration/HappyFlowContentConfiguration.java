/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.philips.cdp2.ews.R;

public class HappyFlowContentConfiguration implements Parcelable {

    @StringRes private final int gettingStartedScreenTitle;

    @StringRes private final int plugInScreenTitle;
    @StringRes private final int plugInScreenBody;
    @DrawableRes private final int plugInScreenImage;

    @StringRes private final int verifyReadyScreenTitle;
    @StringRes private final int verifyReadyScreenQuestion;
    @StringRes private final int verifyReadyScreenYesButton;
    @StringRes private final int verifyReadyScreenNoButton;
    @DrawableRes private final int verifyReadyScreenImage;

    private HappyFlowContentConfiguration(int gettingStartedScreenTitle, int plugInScreenTitle,
                                         int plugInScreenBody, int plugInScreenImage,
                                         int verifyReadyScreenTitle, int verifyReadyScreenQuestion,
                                         int verifyReadyScreenYesButton,
                                         int verifyReadyScreenNoButton, int verifyReadyScreenImage) {
        this.gettingStartedScreenTitle = gettingStartedScreenTitle;
        this.plugInScreenTitle = plugInScreenTitle;
        this.plugInScreenBody = plugInScreenBody;
        this.plugInScreenImage = plugInScreenImage;
        this.verifyReadyScreenTitle = verifyReadyScreenTitle;
        this.verifyReadyScreenQuestion = verifyReadyScreenQuestion;
        this.verifyReadyScreenYesButton = verifyReadyScreenYesButton;
        this.verifyReadyScreenNoButton = verifyReadyScreenNoButton;
        this.verifyReadyScreenImage = verifyReadyScreenImage;
    }

    protected HappyFlowContentConfiguration(Parcel in) {
        gettingStartedScreenTitle = in.readInt();
        plugInScreenTitle = in.readInt();
        plugInScreenBody = in.readInt();
        plugInScreenImage = in.readInt();
        verifyReadyScreenTitle = in.readInt();
        verifyReadyScreenQuestion = in.readInt();
        verifyReadyScreenYesButton = in.readInt();
        verifyReadyScreenNoButton = in.readInt();
        verifyReadyScreenImage = in.readInt();
    }

    public int getGettingStartedScreenTitle() {
        return gettingStartedScreenTitle;
    }

    public int getPlugInScreenTitle() {
        return plugInScreenTitle;
    }

    public int getPlugInScreenBody() {
        return plugInScreenBody;
    }

    public int getPlugInScreenImage() {
        return plugInScreenImage;
    }

    public int getVerifyReadyScreenTitle() {
        return verifyReadyScreenTitle;
    }

    public int getVerifyReadyScreenQuestion() {
        return verifyReadyScreenQuestion;
    }

    public int getVerifyReadyScreenYesButton() {
        return verifyReadyScreenYesButton;
    }

    public int getVerifyReadyScreenNoButton() {
        return verifyReadyScreenNoButton;
    }

    public int getVerifyReadyScreenImage() {
        return verifyReadyScreenImage;
    }

    public static final Creator<HappyFlowContentConfiguration> CREATOR =
            new Creator<HappyFlowContentConfiguration>() {
                @Override
                public HappyFlowContentConfiguration createFromParcel(Parcel in) {
                    return new HappyFlowContentConfiguration(in);
                }

                @Override
                public HappyFlowContentConfiguration[] newArray(int size) {
                    return new HappyFlowContentConfiguration[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gettingStartedScreenTitle);
        dest.writeInt(plugInScreenTitle);
        dest.writeInt(plugInScreenBody);
        dest.writeInt(plugInScreenImage);
        dest.writeInt(verifyReadyScreenTitle);
        dest.writeInt(verifyReadyScreenQuestion);
        dest.writeInt(verifyReadyScreenYesButton);
        dest.writeInt(verifyReadyScreenNoButton);
        dest.writeInt(verifyReadyScreenImage);
    }

    public static class Builder {
        @StringRes private int gettingStartedScreenTitle;

        @StringRes private int plugInScreenTitle;
        @StringRes private int plugInScreenBody;
        @DrawableRes private int plugInScreenImage;

        @StringRes private int verifyReadyScreenTitle;
        @StringRes private int verifyReadyScreenQuestion;
        @StringRes private int verifyReadyScreenYesButton;
        @StringRes private int verifyReadyScreenNoButton;
        @DrawableRes private int verifyReadyScreenImage;

        public Builder() {
            gettingStartedScreenTitle = R.string.label_ews_get_started_title;

            plugInScreenTitle = R.string.label_ews_plug_in_title_default;
            plugInScreenBody = R.string.label_ews_plug_in_body_default;
            plugInScreenImage = R.drawable.ic_ews_device_apmode_blinking;

            verifyReadyScreenTitle = R.string.label_ews_verify_ready_title_default;
            verifyReadyScreenQuestion = R.string.label_ews_verify_ready_question_default;
            verifyReadyScreenYesButton = R.string.button_ews_verify_ready_yes_default;
            verifyReadyScreenNoButton = R.string.button_ews_verify_ready_no_default;
            verifyReadyScreenImage = R.drawable.ic_ews_device_apmode_blinking;
        }

        public Builder setGettingStartedScreenTitle(@StringRes int gettingStartedScreenTitle) {
            this.gettingStartedScreenTitle = gettingStartedScreenTitle;
            return this;
        }

        public Builder setPlugInScreenTitle(@StringRes int plugInScreenTitle) {
            this.plugInScreenTitle = plugInScreenTitle;
            return this;
        }

        public Builder setPlugInScreenBody(@StringRes int plugInScreenBody) {
            this.plugInScreenBody = plugInScreenBody;
            return this;
        }

        public Builder setPlugInScreenImage(@DrawableRes int plugInScreenImage) {
            this.plugInScreenImage = plugInScreenImage;
            return this;
        }

        public Builder setVerifyReadyScreenTitle(@StringRes int verifyReadyScreenTitle) {
            this.verifyReadyScreenTitle = verifyReadyScreenTitle;
            return this;
        }

        public Builder setVerifyReadyScreenQuestion(@StringRes int verifyReadyScreenQuestion) {
            this.verifyReadyScreenQuestion = verifyReadyScreenQuestion;
            return this;
        }

        public Builder setVerifyReadyScreenYesButton(@StringRes int verifyReadyScreenYesButton) {
            this.verifyReadyScreenYesButton = verifyReadyScreenYesButton;
            return this;
        }

        public Builder setVerifyReadyScreenNoButton(@StringRes int verifyReadyScreenNoButton) {
            this.verifyReadyScreenNoButton = verifyReadyScreenNoButton;
            return this;
        }

        public Builder setVerifyReadyScreenImage(@DrawableRes int verifyReadyScreenImage) {
            this.verifyReadyScreenImage = verifyReadyScreenImage;
            return this;
        }

        public HappyFlowContentConfiguration build() {
            return new HappyFlowContentConfiguration(gettingStartedScreenTitle, plugInScreenTitle,
                    plugInScreenBody, plugInScreenImage, verifyReadyScreenTitle,
                    verifyReadyScreenQuestion, verifyReadyScreenYesButton, verifyReadyScreenNoButton,
                    verifyReadyScreenImage);
        }
    }
}
