/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentNotificationBadgeBinding;
import com.philips.platform.uid.view.widget.NotificationBadge;


public class NotificationBadgeFragment extends BaseFragment {

    private NotificationBadge defaultBadge;
    private NotificationBadge smallBadge;
    private EditText enterNumberField;
    public ObservableBoolean isAccentColor = new ObservableBoolean(Boolean.TRUE);
    private String badgeCount = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNotificationBadgeBinding notificationBadgeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_badge, container, false);
        notificationBadgeBinding.setFragment(this);
        defaultBadge = (NotificationBadge) notificationBadgeBinding.getRoot().findViewById(R.id.uid_text_default);
        smallBadge = (NotificationBadge) notificationBadgeBinding.getRoot().findViewById(R.id.uid_text_small);
        enterNumberField = (EditText) notificationBadgeBinding.getRoot().findViewById(R.id.edit_input_number);
        defaultBadge.setVisibility(View.VISIBLE);
        smallBadge.setVisibility(View.VISIBLE);
        defaultBadge.setText(enterNumberField.getText());
        smallBadge.setText(enterNumberField.getText());
        enterNumberField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                badgeCount = enterNumberField.getText().toString();
                if (badgeCount.length() > 4) {
                    badgeCount = "9999+";
                }
                defaultBadge.setText(badgeCount);
                smallBadge.setText(badgeCount);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });
        return notificationBadgeBinding.getRoot();
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

    private void enableRecyclerViewSeparator() {
        if (isAccentColor.get()) {
            //  defaultBadge.setVisibility(View.VISIBLE);

        } else {
            //defaultBadge.setVisibility(View.INVISIBLE);
        }
    }

    public void setNotificationBadge(boolean isSeparatorEnabled) {
        this.isAccentColor.set(isSeparatorEnabled);
        enableRecyclerViewSeparator();

    }
}