/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.aboutscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.screens.termsandconditions.TermsAndPrivacyStateData;
import com.philips.platform.baseapp.screens.utility.RALog;

/**
 * About screen to display content and version number
 * This class is for sutomising the about screen present from UiKit Lib
 * Added custom titles
 * Background color
 * Latest version
 */

public class AboutScreenFragment extends AbstractAppFrameworkBaseFragment implements AboutScreenContract.View {

    public static final String TAG = AboutScreenFragment.class.getSimpleName();

    private AboutScreenContract.Action aboutScreenActionListener;

    @Override
    public void onResume() {
        RALog.d(TAG, " onResume");
        super.onResume();
        updateActionBar();
    }

    protected void updateActionBar() {
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBarIcon(false);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_AboutScreen_Title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutScreenActionListener=new AboutScreenPresenter(getActivity(),this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uikit_about_screen, container, false);
        TextView version = (TextView) view.findViewById(R.id.about_version);
        version.setText(getResources().getString(R.string.RA_About_App_Version) + BuildConfig.VERSION_NAME);
        TextView content = (TextView) view.findViewById(R.id.about_content);
        content.setText(R.string.RA_About_Description);
        TextView termsAndConditionsTextView = (TextView) view.findViewById(R.id.about_terms);
        termsAndConditionsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutScreenActionListener.loadTermsAndPrivacy(TermsAndPrivacyStateData.TermsAndPrivacyEnum.TERMS_CLICKED);
            }
        });
        TextView privacyPolicyTextView=(TextView)view.findViewById(R.id.about_policy);
        privacyPolicyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutScreenActionListener.loadTermsAndPrivacy(TermsAndPrivacyStateData.TermsAndPrivacyEnum.PRIVACY_CLICKED);
            }
        });
        startAppTagging(TAG);
        return view;

    }
}
