/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    private FragmentNotificationBadgeBinding notificationBadgeBinding;
    public final ObservableField<String> badgeCount = new ObservableField<>("1");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notificationBadgeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_badge, container, false);
        notificationBadgeBinding.setFragment(this);
        notificationBadgeBinding.quietEmail.setVectorResource(R.drawable.ic_email_icon);
        notificationBadgeBinding.editInputNumber.addTextChangedListener(this);
        return notificationBadgeBinding.getRoot();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String savedBadgeCount = savedInstanceState.getString(BADGE_COUNT);
            if (!savedBadgeCount.isEmpty()) {
                notificationBadgeBinding.uidTextDefault.setVisibility(View.VISIBLE);
                notificationBadgeBinding.uidTextSmall.setVisibility(View.VISIBLE);
                badgeCount.set(savedBadgeCount);
            }
        } else {
            badgeCount.set(badgeCount.get());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BADGE_COUNT, badgeCount.get());
    }

    @Override
    public void onStop() {
        super.onStop();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(notificationBadgeBinding.getRoot().getWindowToken(), 0);
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
        String value = notificationBadgeBinding.editInputNumber.getText().toString();
        badgeCount.set(getBadgeCount(value));
        if (badgeCount.get().length() > 4) {
            badgeCount.set("9999+");
        }
    }

    @NonNull
    private String getBadgeCount(String s) {
        try {
            Integer badgeValue = Integer.valueOf(s);
            return badgeValue == 0 ? "" : "" + badgeValue;
        } catch (NumberFormatException nfe) {
            return "";
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}