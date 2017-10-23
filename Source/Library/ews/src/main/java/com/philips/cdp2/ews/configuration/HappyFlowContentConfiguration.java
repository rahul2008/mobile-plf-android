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

    // below properties align to EWS - start screen
    @StringRes private final int gettingStartedScreenTitle;

    // below properties align to EWS - setup screen
    @StringRes private final int setUpScreenTitle;
    @StringRes private final int setUpScreenBody;
    @DrawableRes private final int setUpScreenImage;

    // below properties align to EWS - setup screen step02
    @StringRes private final int setUpVerifyScreenTitle;
    @StringRes private final int setUpVerifyScreenQuestion;
    @StringRes private final int setUpVerifyScreenYesButton;
    @StringRes private final int setUpVerifyScreenNoButton;
    @DrawableRes private final int setUpVerifyScreenImage;

    private HappyFlowContentConfiguration(int gettingStartedScreenTitle, int setUpScreenTitle,
                                          int setUpScreenBody, int setUpScreenImage,
                                          int setUpVerifyScreenTitle, int setUpVerifyScreenQuestion,
                                          int setUpVerifyScreenYesButton,
                                          int setUpVerifyScreenNoButton, int setUpVerifyScreenImage) {
        this.gettingStartedScreenTitle = gettingStartedScreenTitle;
        this.setUpScreenTitle = setUpScreenTitle;
        this.setUpScreenBody = setUpScreenBody;
        this.setUpScreenImage = setUpScreenImage;
        this.setUpVerifyScreenTitle = setUpVerifyScreenTitle;
        this.setUpVerifyScreenQuestion = setUpVerifyScreenQuestion;
        this.setUpVerifyScreenYesButton = setUpVerifyScreenYesButton;
        this.setUpVerifyScreenNoButton = setUpVerifyScreenNoButton;
        this.setUpVerifyScreenImage = setUpVerifyScreenImage;
    }

    protected HappyFlowContentConfiguration(Parcel in) {
        gettingStartedScreenTitle = in.readInt();
        setUpScreenTitle = in.readInt();
        setUpScreenBody = in.readInt();
        setUpScreenImage = in.readInt();
        setUpVerifyScreenTitle = in.readInt();
        setUpVerifyScreenQuestion = in.readInt();
        setUpVerifyScreenYesButton = in.readInt();
        setUpVerifyScreenNoButton = in.readInt();
        setUpVerifyScreenImage = in.readInt();
    }

    public int getGettingStartedScreenTitle() {
        return gettingStartedScreenTitle;
    }

    public int getSetUpScreenTitle() {
        return setUpScreenTitle;
    }

    public int getSetUpScreenBody() {
        return setUpScreenBody;
    }

    public int getSetUpScreenImage() {
        return setUpScreenImage;
    }

    public int getSetUpVerifyScreenTitle() {
        return setUpVerifyScreenTitle;
    }

    public int getSetUpVerifyScreenQuestion() {
        return setUpVerifyScreenQuestion;
    }

    public int getSetUpVerifyScreenYesButton() {
        return setUpVerifyScreenYesButton;
    }

    public int getSetUpVerifyScreenNoButton() {
        return setUpVerifyScreenNoButton;
    }

    public int getSetUpVerifyScreenImage() {
        return setUpVerifyScreenImage;
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
        dest.writeInt(setUpScreenTitle);
        dest.writeInt(setUpScreenBody);
        dest.writeInt(setUpScreenImage);
        dest.writeInt(setUpVerifyScreenTitle);
        dest.writeInt(setUpVerifyScreenQuestion);
        dest.writeInt(setUpVerifyScreenYesButton);
        dest.writeInt(setUpVerifyScreenNoButton);
        dest.writeInt(setUpVerifyScreenImage);
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
