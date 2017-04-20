/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentNotificationBadgeBinding;
import com.philips.platform.uid.view.widget.NotificationBadge;


public class NotificationBadgeFragment extends BaseFragment {

    private NotificationBadge mDefaultText;
    private NotificationBadge mSmallText;
    private EditText mEnterNumber;
    private FragmentNotificationBadgeBinding notificationBadgeBinding;
    public ObservableBoolean isAccentColor = new ObservableBoolean(Boolean.TRUE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notificationBadgeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_badge, container, false);
        notificationBadgeBinding.setFragment(this);
        mDefaultText = (NotificationBadge) notificationBadgeBinding.getRoot().findViewById(R.id.uid_text_default);
        mSmallText = (NotificationBadge) notificationBadgeBinding.getRoot().findViewById(R.id.uid_text_small);
        mEnterNumber = (EditText) notificationBadgeBinding.getRoot().findViewById(R.id.edit_input_number);

        mEnterNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mDefaultText.setErrorMessage(mEnterNumber.getText().toString());
                mSmallText.setErrorMessage(mEnterNumber.getText().toString());


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
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_notification_badge;
    }

    private void enableRecyclerViewSeparator() {
        if (isAccentColor.get()) {
            mDefaultText.setVisibility(View.VISIBLE);

        } else {
            mDefaultText.setVisibility(View.INVISIBLE);
        }
    }

    public void setNotificationBadge(boolean isSeparatorEnabled) {
        this.isAccentColor.set(isSeparatorEnabled);
        enableRecyclerViewSeparator();

    }

}