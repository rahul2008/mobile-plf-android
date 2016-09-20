/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.settingscreen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.UpdateReceiveMarketingEmailHandler;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.philips.cdp.uikit.customviews.UIKitButton;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

/**
 * Adapter for setting List items
 * The following fields needs to be populated for the list
 * These are standar items but they can e changed according to the vertical requirements
 *
 * name = (TextView) vi.findViewById(R.id.ifo);
 * value = (PuiSwitch) vi.findViewById(R.id.switch_button);
 * number = (TextView) vi.findViewById(R.id.numberwithouticon);
 * on_off = (TextView) vi.findViewById(R.id.medium);
 * arrow = (FontIconTextView) vi.findViewById(R.id.arrowwithouticons);
 * description = (TextView) vi.findViewById(R.id.text_description_without_icons);
 * type = mSettingsItemList.get(position).type;

 */

public class SettingsAdapter extends BaseAdapter{
    private Context mActivity;
    private LayoutInflater inflater = null;
    private UserRegistrationState userRegistrationState;
    private LogoutHandler mLogoutHandler = null;
    private ArrayList<SettingListItem> mSettingsItemList = null;
    private UIBasePresenter fragmentPresenter;
    private SharedPreferenceUtility sharedPreferenceUtility;
    public static final int iapHistoryLaunch = 5454;
    TextView name;
    PuiSwitch value;
    TextView number;
    TextView on_off;
    FontIconTextView arrow;
    TextView description;
    SettingListItemType type;
    View vi;
    ProgressDialog progress;

    public SettingsAdapter(Context context, ArrayList<SettingListItem> settingsItemList,
                           LogoutHandler logoutHandler, UIBasePresenter fragmentPresenter) {
        mActivity = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userRegistrationState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);

    //    mUser = new User(context);
        mSettingsItemList = settingsItemList;
        mLogoutHandler = logoutHandler;
        this.fragmentPresenter = fragmentPresenter;
        sharedPreferenceUtility = new SharedPreferenceUtility(context);
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
        vi = convertView;
        if (mSettingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_login)).toString())
                ||mSettingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_log_out)).toString())) {

            if (convertView == null) {
                vi = inflater.inflate(R.layout.af_settings_fragment_logout_button, null);
                UIKitButton btn_settings_logout = (UIKitButton) vi.findViewById(R.id.btn_settings_logout);
                if (userRegistrationState.getUserObject(mActivity).isUserSignIn()) {
                    btn_settings_logout.setText(getString(R.string.settings_list_item_log_out));
                } else {
                    btn_settings_logout.setText(getString(R.string.settings_list_item_login));
                }

                btn_settings_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userRegistrationState.getUserObject(mActivity).isUserSignIn()) {
                            logoutAlert();
                        } else {
                            fragmentPresenter.onLoad(mActivity);
                        }
                    }
                });
            }

        } else {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.uikit_listview_without_icons, null);
            }
            name = (TextView) vi.findViewById(R.id.ifo);
            value = (PuiSwitch) vi.findViewById(R.id.switch_button);
            number = (TextView) vi.findViewById(R.id.numberwithouticon);
            on_off = (TextView) vi.findViewById(R.id.medium);
            arrow = (FontIconTextView) vi.findViewById(R.id.arrowwithouticons);
            description = (TextView) vi.findViewById(R.id.text_description_without_icons);
            type = mSettingsItemList.get(position).type;

            switch (type) {
                case HEADER:
                    headerSection(position);
                    break;
                case CONTENT:
                    subSection(position);
                    break;
                case NOTIFICATION:
                    notificationSection(position);
                    break;
            }
        }

        return vi;
    }

    private void notificationSection(int position) {
        if(null != name && null != value && null != description && null != number && null != on_off && null != arrow) {
            name.setVisibility(View.VISIBLE);
            name.setText(mSettingsItemList.get(position).title);
            value.setVisibility(View.VISIBLE);

            if (userRegistrationState.getUserObject(mActivity).getReceiveMarketingEmail()) {
                value.setChecked(true);
            } else {
                value.setChecked(false);
            }
            value.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    progress = new ProgressDialog(mActivity);
                    progress.setTitle("Please Wait!!");
                    progress.setMessage("Wait!!");
                    progress.setCancelable(true);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                    if (isChecked) {
                        userRegistrationState.getUserObject(mActivity).updateReceiveMarketingEmail(new UpdateReceiveMarketingEmailHandler() {
                            @Override
                            public void onUpdateReceiveMarketingEmailSuccess() {
                                sharedPreferenceUtility.writePreferenceBoolean(Constants.isEmailMarketingEnabled, true);
                                progress.cancel();
                                Toast.makeText(mActivity,"Update suceess",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onUpdateReceiveMarketingEmailFailedWithError(int i) {
                                progress.cancel();
                                Toast.makeText(mActivity,"Update FAIL",Toast.LENGTH_LONG).show();

                            }
                        }, true);
                    } else {
                        userRegistrationState.getUserObject(mActivity).updateReceiveMarketingEmail(new UpdateReceiveMarketingEmailHandler() {
                            @Override
                            public void onUpdateReceiveMarketingEmailSuccess() {
                                sharedPreferenceUtility.writePreferenceBoolean(Constants.isEmailMarketingEnabled, false);
                                progress.cancel();
                                Toast.makeText(mActivity,"Update success",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onUpdateReceiveMarketingEmailFailedWithError(int i) {
                                progress.cancel();
                                Toast.makeText(mActivity,"Update FAIL",Toast.LENGTH_LONG).show();
                            }
                        }, false);
                    }
                }
            });

            String descText = getString(R.string.settings_list_item_four_desc) + "\n" +
                    getString(R.string.settings_list_item_four_term_cond);

            description.setVisibility(View.VISIBLE);
            description.setText(descText);
            arrow.setVisibility(View.GONE);
        }
    }

    private void subSection(int position) {
        if(null != name && null != value && null != description && null != number && null != on_off && null != arrow) {
            name.setText(mSettingsItemList.get(position).title);
            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            on_off.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
        }
    }

    private void headerSection(int position) {
        CharSequence titleText = null;
        titleText = mSettingsItemList.get(position).title;
        if(null != name && null != value && null != description && null != number && null != on_off && null != arrow) {
            name.setText(titleText);
            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            number.setVisibility(View.GONE);
            on_off.setVisibility(View.GONE);
            arrow.setVisibility(View.INVISIBLE);
        }

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
                                userRegistrationState.getUserObject(mActivity).logout(mLogoutHandler);
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public boolean isEnabled(int position) {
        if (mSettingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_main)).toString())
                || mSettingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_purchases)).toString())
                || mSettingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_my_acc)).toString())) {
            return false;
        } else {
            return super.isEnabled(position);
        }
    }

    private String getString(int id) {
        return mActivity.getResources().getString(id);
    }


}
