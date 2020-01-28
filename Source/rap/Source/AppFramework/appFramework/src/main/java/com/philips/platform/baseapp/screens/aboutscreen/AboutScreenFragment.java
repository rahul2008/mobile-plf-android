/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.aboutscreen;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.base.AppFrameworkTagging;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.uid.text.utils.UIDClickableSpan;
import com.philips.platform.uid.view.widget.AboutScreen;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.baseapp.screens.utility.Constants.TAGGING_CHURN;

/**
 * About screen to display content and version number
 * This class is for sutomising the about screen present from DLS Lib
 * Added custom titles
 * Background color
 * Latest version
 */

public class AboutScreenFragment extends AbstractAppFrameworkBaseFragment implements AboutScreenContract.View {

    public static final String TAG = AboutScreenFragment.class.getSimpleName();

    private AboutScreenContract.Action aboutScreenActionListener;

    private Label termsLabel;

    private Label privacyLabel;

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_AboutScreen_Title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutScreenActionListener = getPresenter();
    }

    protected AboutScreenContract.Action getPresenter() {
        return new AboutScreenPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_about_fragment, container, false);

        AboutScreen aboutScreen = (AboutScreen) view.findViewById(R.id.af_about_screen);

        aboutScreen.setAppVersion(getResources().getString(R.string.RA_About_App_Version) + BuildConfig.VERSION_NAME);

        termsLabel = (Label) view.findViewById(R.id.uid_about_screen_terms);
        termsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutScreenActionListener.loadTermsAndPrivacy(Constants.TERMS_AND_CONDITIONS,getContext().getString(R.string.USR_TermsAndConditionsText));
            }
        });
        privacyLabel = (Label) view.findViewById(R.id.uid_about_screen_privacy);
        privacyLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAppTagging();
                aboutScreenActionListener.loadTermsAndPrivacy(Constants.PRIVACY,getContext().getString(R.string.RA_DLS_about_privacy));
            }
        });

        addLinks(privacyLabel);
        addLinks(termsLabel);

        startAppTagging(TAG);
        return view;

    }

    private void addLinks(Label labelPlaceHolder) {
        UIDClickableSpan span = new UIDClickableSpan(null);
        SpannableString spannableString = SpannableString.valueOf(labelPlaceHolder.getText());
        spannableString.setSpan(span, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        labelPlaceHolder.setText(spannableString);
    }

    protected void startAppTagging() {
        AppFrameworkTagging.getInstance().trackAction(TAGGING_CHURN);
    }
}
