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

/**
 * HappyFlowContentConfiguration is a model class for Happy Flow configuration,
 * Which is setting different string and drawables to happy Flow Screens.
 */
public class HappyFlowContentConfiguration implements Parcelable {

    // below properties align to EWS - start screen
    @StringRes private final int gettingStartedScreenTitle;
    @DrawableRes private final int gettingStartedScreenImage;

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
    private HappyFlowContentConfiguration(int gettingStartedScreenTitle, int gettingStartedScreenImage,
                                          int setUpScreenTitle,
                                          int setUpScreenBody, int setUpScreenImage,
                                          int setUpVerifyScreenTitle, int setUpVerifyScreenQuestion,
                                          int setUpVerifyScreenYesButton,
                                          int setUpVerifyScreenNoButton, int setUpVerifyScreenImage) {
        this.gettingStartedScreenTitle = gettingStartedScreenTitle;
        this.gettingStartedScreenImage = gettingStartedScreenImage;
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
        gettingStartedScreenImage = in.readInt();
        setUpScreenTitle = in.readInt();
        setUpScreenBody = in.readInt();
        setUpScreenImage = in.readInt();
        setUpVerifyScreenTitle = in.readInt();
        setUpVerifyScreenQuestion = in.readInt();
        setUpVerifyScreenYesButton = in.readInt();
        setUpVerifyScreenNoButton = in.readInt();
        setUpVerifyScreenImage = in.readInt();
    }

    /**
     * Return GettingStartedScreenTitle
     * @return @StringRes GettingStartedScreenTitle
     */
    public int getGettingStartedScreenTitle() {
        return gettingStartedScreenTitle;
    }

    /**
     * Return GettingStartedScreenImage
     * @return @DrawableRes GettingStartedScreenImage
     */
    public int getGettingStartedScreenImage() {
        return gettingStartedScreenImage;
    }

    /**
     * Return SetUpScreenTitle
     * @return @StringRes SetUpScreenTitle
     */
    public int getSetUpScreenTitle() {
        return setUpScreenTitle;
    }

    /**
     * Return SetUpScreenBody
     * @return @StringRes SetUpScreenBody
     */
    public int getSetUpScreenBody() {
        return setUpScreenBody;
    }

    /**
     * Return SetUpScreenImage
     * @return @DrawableRes SetUpScreenImage
     */
    public int getSetUpScreenImage() {
        return setUpScreenImage;
    }

    /**
     * Return SetUpVerifyScreenTitle
     * @return @StringRes SetUpVerifyScreenTitle
     */
    public int getSetUpVerifyScreenTitle() {
        return setUpVerifyScreenTitle;
    }

    /**
     * Return SetUpVerifyScreenQuestion
     * @return @StringRes SetUpVerifyScreenQuestion
     */
    public int getSetUpVerifyScreenQuestion() {
        return setUpVerifyScreenQuestion;
    }

    /**
     * Return SetUpVerifyScreenYesButton
     * @return @StringRes SetUpVerifyScreenYesButton
     */
    public int getSetUpVerifyScreenYesButton() {
        return setUpVerifyScreenYesButton;
    }

    /**
     * Return SetUpVerifyScreenNoButton
     * @return @StringRes SetUpVerifyScreenNoButton
     */
    public int getSetUpVerifyScreenNoButton() {
        return setUpVerifyScreenNoButton;
    }

    /**
     * Return SetUpVerifyScreenImage
     * @return @DrawableRes SetUpVerifyScreenImage
     */
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
        dest.writeInt(gettingStartedScreenImage);
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
        @DrawableRes private int gettingStartedScreenImage;

        @StringRes private int setUpScreenTitle;
        @StringRes private int setUpScreenBody;
        @DrawableRes private int setUpScreenImage;

        @StringRes private int setUpVerifyScreenTitle;
        @StringRes private int setUpVerifyScreenQuestion;
        @StringRes private int setUpVerifyScreenYesButton;
        @StringRes private int setUpVerifyScreenNoButton;
        @DrawableRes private int setUpVerifyScreenImage;

        /**
         * Build HappyFlow with default strings and drawables.
         */
        public Builder() {
            gettingStartedScreenTitle = R.string.label_ews_get_started_title;
            gettingStartedScreenImage = R.drawable.philips_logo_default;

            setUpScreenTitle = R.string.label_ews_plug_in_title_default;
            setUpScreenBody = R.string.label_ews_plug_in_body_default;
            setUpScreenImage = R.drawable.power_button_default;

            setUpVerifyScreenTitle = R.string.label_ews_verify_ready_title_default;
            setUpVerifyScreenQuestion = R.string.label_ews_verify_ready_question_default;
            setUpVerifyScreenYesButton = R.string.button_ews_verify_ready_yes_default;
            setUpVerifyScreenNoButton = R.string.button_ews_verify_ready_no_default;
            setUpVerifyScreenImage = R.drawable.power_button_default;
        }

        /**
         * Setter for settingStartedScreenTitle
         * @param gettingStartedScreenTitle settingStartedScreenTitle
         * @return  Builder
         */
        public Builder setGettingStartedScreenTitle(@StringRes int gettingStartedScreenTitle) {
            this.gettingStartedScreenTitle = gettingStartedScreenTitle;
            return this;
        }

        /**
         * Setter for StartedScreenImage
         * @param gettingStartedScreenImage StartedScreenImage
         * @return Builder
         */
        public Builder setGettingStartedScreenImage(@DrawableRes int gettingStartedScreenImage) {
            this.gettingStartedScreenImage = gettingStartedScreenImage;
            return this;
        }

        /**
         * Setter for SetUpScreenTitle
         * @param plugInScreenTitle SetUpScreenTitle
         * @return Builder
         */
        public Builder setSetUpScreenTitle(@StringRes int plugInScreenTitle) {
            this.setUpScreenTitle = plugInScreenTitle;
            return this;
        }

        /**
         * Setter for SetUpScreenBody
         * @param setUpScreenBody SetUpScreenBody
         * @return Builder
         */
        public Builder setSetUpScreenBody(@StringRes int setUpScreenBody) {
            this.setUpScreenBody = setUpScreenBody;
            return this;
        }

        /**
         * Setter for SetUpScreenImage
         * @param setUpScreenImage SetUpScreenImage
         * @return Builder
         */
        public Builder setSetUpScreenImage(@DrawableRes int setUpScreenImage) {
            this.setUpScreenImage = setUpScreenImage;
            return this;
        }

        /**
         * Setter for SetUpVerifyScreenTitle
         * @param setUpVerifyScreenTitle SetUpVerifyScreenTitle
         * @return Builder
         */
        public Builder setSetUpVerifyScreenTitle(@StringRes int setUpVerifyScreenTitle) {
            this.setUpVerifyScreenTitle = setUpVerifyScreenTitle;
            return this;
        }

        /**
         * Setter for SetUpVerifyScreenQuestion
         * @param setUpVerifyScreenQuestion SetUpVerifyScreenQuestion
         * @return Builder
         */
        public Builder setSetUpVerifyScreenQuestion(@StringRes int setUpVerifyScreenQuestion) {
            this.setUpVerifyScreenQuestion = setUpVerifyScreenQuestion;
            return this;
        }

        /**
         * Setter for SetUpVerifyScreenYesButton
         * @param setUpVerifyScreenYesButton SetUpVerifyScreenYesButton
         * @return Builder
         */
        public Builder setSetUpVerifyScreenYesButton(@StringRes int setUpVerifyScreenYesButton) {
            this.setUpVerifyScreenYesButton = setUpVerifyScreenYesButton;
            return this;
        }

        /**
         * Setter for SetUpVerifyScreenNoButton
         * @param setUpVerifyScreenNoButton SetUpVerifyScreenNoButton
         * @return Builder
         */
        public Builder setSetUpVerifyScreenNoButton(@StringRes int setUpVerifyScreenNoButton) {
            this.setUpVerifyScreenNoButton = setUpVerifyScreenNoButton;
            return this;
        }

        /**
         * Setter for SetUpVerifyScreenImage
         * @param setUpVerifyScreenImage SetUpVerifyScreenImage
         * @return Builder
         */
        public Builder setSetUpVerifyScreenImage(@DrawableRes int setUpVerifyScreenImage) {
            this.setUpVerifyScreenImage = setUpVerifyScreenImage;
            return this;
        }

        /**
         * Return HappyFlowContentConfiguration.
         * @return HappyFlowContentConfiguration
         */
        public HappyFlowContentConfiguration build() {
            return new HappyFlowContentConfiguration(gettingStartedScreenTitle, gettingStartedScreenImage, setUpScreenTitle,
                    setUpScreenBody, setUpScreenImage, setUpVerifyScreenTitle,
                    setUpVerifyScreenQuestion, setUpVerifyScreenYesButton, setUpVerifyScreenNoButton,
                    setUpVerifyScreenImage);
        }
    }
}
