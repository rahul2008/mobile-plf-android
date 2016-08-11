/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.settingscreen;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.philips.cdp.uikit.customviews.UIKitButton;
import com.philips.platform.appframework.R;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

public class SettingsAdapter extends BaseAdapter {
    private Context mActivity;
    private Bundle saveBundle = new Bundle();
    private LayoutInflater inflater = null;
    private User mUser = null;
    private LogoutHandler mLogoutHandler = null;
    private ArrayList<SettingListItem> mSettingsItemList = null;
    private UIBasePresenter fragmentPresenter;


    public SettingsAdapter(Context context, ArrayList<SettingListItem> settingsItemList,
                           LogoutHandler logoutHandler, UIBasePresenter fragmentPresenter) {
        mActivity = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mUser = new User(context);
        mSettingsItemList = settingsItemList;
        mLogoutHandler = logoutHandler;
        this.fragmentPresenter = fragmentPresenter;
    }

    @Override
    public int getCount() {
        return mSettingsItemList.size();
    }

    @Override
    public Object getItem(final int position) {
        return position;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        return getView(position, convertView);
    }

    @NonNull
    private View getView(int position, View convertView) {
        View vi = convertView;
        if( mSettingsItemList.get(position).title.equals(Html.fromHtml(getString(R.string.settings_list_item_login)))
                || mSettingsItemList.get(position).title.equals(Html.fromHtml(getString(R.string.settings_list_item_log_out)))) {

            if (convertView == null) {
                vi = inflater.inflate(R.layout.af_settings_fragment_logout_button, null);
                UIKitButton btn_settings_logout = (UIKitButton) vi.findViewById(R.id.btn_settings_logout);
                if (mUser.isUserSignIn()) {
                    btn_settings_logout.setText(getString(R.string.settings_list_item_log_out));
                } else {
                    btn_settings_logout.setText(getString(R.string.settings_list_item_login));
                }

                btn_settings_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mUser.isUserSignIn()) {
                            logoutAlert();
                        } else {
                            fragmentPresenter.onLoad(mActivity);
                        }
                    }
                });
            }

        }
        else {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.uikit_listview_without_icons, null);
            }
            TextView name = (TextView) vi.findViewById(R.id.ifo);
            PuiSwitch value = (PuiSwitch) vi.findViewById(R.id.switch_button);
            TextView number = (TextView) vi.findViewById(R.id.numberwithouticon);
            TextView on_off = (TextView) vi.findViewById(R.id.medium);
            FontIconTextView arrow = (FontIconTextView) vi.findViewById(R.id.arrowwithouticons);
            TextView description = (TextView) vi.findViewById(R.id.text_description_without_icons);
            SettingListItemType type = mSettingsItemList.get(position).type;

            switch (type) {
                case HEADER:
                    headerSection(position, name, value, number, on_off, arrow, description);
                    vi.setClickable(false);
                    vi.setEnabled(false);
                    vi.setActivated(false);
                    break;
                case CONTENT:
                    subSection(position, name, value, on_off, arrow, description);
                    break;
                case NOTIFICATION:
                    notificationSection(position, name, value, arrow, description);
                    break;
            }
        }
        return vi;
    }

    private void notificationSection(int position, TextView name, PuiSwitch value, FontIconTextView arrow, TextView description) {
        name.setVisibility(View.VISIBLE);
        name.setText(mSettingsItemList.get(position).title);
        value.setVisibility(View.VISIBLE);
        setSwitchState(value, "s1");

        value.setChecked(mUser.getReceiveMarketingEmail());
        value.setClickable(false);

        String descText = getString(R.string.settings_list_item_four_desc) + "\n" +
                getString(R.string.settings_list_item_four_term_cond);

        description.setVisibility(View.VISIBLE);
        description.setText(descText);
        arrow.setVisibility(View.GONE);
    }

    private void subSection(int position, TextView name, PuiSwitch value, TextView on_off, FontIconTextView arrow, TextView description) {
        name.setText(mSettingsItemList.get(position).title);

        value.setVisibility(View.GONE);
        description.setVisibility(View.GONE);
        on_off.setVisibility(View.GONE);
        arrow.setVisibility(View.VISIBLE);
    }

    private void headerSection(int position, TextView name, PuiSwitch value, TextView number, TextView on_off, FontIconTextView arrow, TextView description) {
        CharSequence titleText = null;
        titleText = mSettingsItemList.get(position).title;
        name.setText(titleText);

        value.setVisibility(View.GONE);
        description.setVisibility(View.GONE);
        number.setVisibility(View.GONE);
        on_off.setVisibility(View.GONE);
        arrow.setVisibility(View.INVISIBLE);
    }

    private void logoutAlert() {
        new AlertDialog.Builder(mActivity)
                .setTitle(getString(R.string.settings_list_item_log_out))
                .setMessage("Are you sure want to log out?")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.settings_list_item_log_out),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mUser.logout(mLogoutHandler);
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public Bundle getSavedBundle() {
        return saveBundle;
    }

    public void setSavedBundle(Bundle bundle) {
        saveBundle = bundle;
    }

    private void setSwitchState(CompoundButton toggleSwitch, String code) {
        if (saveBundle.containsKey(code)) {
            toggleSwitch.setChecked(saveBundle.getBoolean(code));
        }
    }

    private String getString(int id) {
        return mActivity.getResources().getString(id);
    }
}
