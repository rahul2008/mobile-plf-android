/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentAboutScreenBinding;
import com.philips.platform.uid.text.utils.UIDClickableSpan;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Label;


public class AboutScreenFragment extends BaseFragment implements View.OnClickListener {

    private FragmentAboutScreenBinding fragmentAboutScreenBinding;
    private AlertDialogFragment alertDialogFragment;
    public static final String ABOUT_DIALOG_TAG = "ABOUT_DIALOG_TAG";

    @Override
    public int getPageTitle() {
        return R.string.page_title_about_screen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAboutScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_screen, container, false);
        fragmentAboutScreenBinding.setFragment(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_cross_icon);
        addLinks();
        return fragmentAboutScreenBinding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem themeSettings = menu.findItem(R.id.menu_theme_settings);
        themeSettings.setVisible(false);
    }

    private void addLinks() {
        UIDClickableSpan termsSpan = new UIDClickableSpan(termsRunnable);
        Label termsLabel = (Label) fragmentAboutScreenBinding.catalogAboutScreen.findViewById(R.id.uid_about_screen_terms);
        SpannableString termsString = SpannableString.valueOf(termsLabel.getText());
        termsString.setSpan(termsSpan, 0, termsString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        termsLabel.setText(termsString);

        UIDClickableSpan privacySpan = new UIDClickableSpan(privacyRunnable);
        Label privacyLabel = (Label) fragmentAboutScreenBinding.catalogAboutScreen.findViewById(R.id.uid_about_screen_privacy);
        SpannableString privacyString = SpannableString.valueOf(privacyLabel.getText());
        privacyString.setSpan(privacySpan, 0, privacyString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        privacyLabel.setText(privacyString);
    }

    private Runnable termsRunnable = new Runnable() {
        @Override
        public void run() {
            showAlert(getResources().getString(R.string.about_screen_terms));
        }
    };

    private Runnable privacyRunnable = new Runnable() {
        @Override
        public void run() {
            showAlert(getResources().getString(R.string.about_screen_privacy));
        }
    };

    public void showAlert(CharSequence title) {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_ALERT)
                .setMessage(R.string.about_dialog_text)
                .setPositiveButton(R.string.about_dialog_button_text, this)
                .setDimLayer(DialogConstants.DIM_SUBTLE)
                .setCancelable(false)
                .setTitle(title);
        alertDialogFragment = builder.create();
        alertDialogFragment.show(getFragmentManager(), ABOUT_DIALOG_TAG);
    }

    @Override
    public void onClick(final View v) {
        if (alertDialogFragment != null) {
            alertDialogFragment.dismiss();
        } else {
            final AlertDialogFragment wtf = (AlertDialogFragment) getFragmentManager().findFragmentByTag(ABOUT_DIALOG_TAG);
            wtf.dismiss();
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AlertDialogFragment alertDialogFragment = (AlertDialogFragment) getFragmentManager().findFragmentByTag(ABOUT_DIALOG_TAG);
        if (alertDialogFragment != null) {
            alertDialogFragment.setPositiveButtonListener(this);
        }
    }
}