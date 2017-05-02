/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentNotificationBadgeBinding;


public class NotificationBadgeFragment extends BaseFragment implements TextWatcher {

    public static final String BADGE_COUNT = "BADGE_COUNT";
    private String badgeCount = "1";
    private FragmentNotificationBadgeBinding notificationBadgeBinding;
    private String savedBadgeCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notificationBadgeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_badge, container, false);
        notificationBadgeBinding.setFragment(this);
        notificationBadgeBinding.quietEmail.setVectorResource(R.drawable.ic_email_icon);
        notificationBadgeBinding.uidTextDefault.setVisibility(View.VISIBLE);
        notificationBadgeBinding.uidTextSmall.setVisibility(View.VISIBLE);
        notificationBadgeBinding.uidTextDefault.setText(badgeCount);
        notificationBadgeBinding.uidTextSmall.setText(badgeCount);
        notificationBadgeBinding.editInputNumber.addTextChangedListener(this);
        return notificationBadgeBinding.getRoot();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            savedBadgeCount = savedInstanceState.getString(BADGE_COUNT);
            notificationBadgeBinding.uidTextDefault.setText(savedBadgeCount);
            notificationBadgeBinding.uidTextSmall.setText(savedBadgeCount);

        } else {
            notificationBadgeBinding.uidTextDefault.setText(badgeCount);
            notificationBadgeBinding.uidTextSmall.setText(badgeCount);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("BADGE_COUNT", notificationBadgeBinding.uidTextDefault.getText().toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_notification_badge;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        badgeCount = notificationBadgeBinding.editInputNumber.getText().toString();
        if (badgeCount.length() > 4) {
            badgeCount = "9999+";
        }
        notificationBadgeBinding.uidTextDefault.setText(badgeCount);
        notificationBadgeBinding.uidTextSmall.setText(badgeCount);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}