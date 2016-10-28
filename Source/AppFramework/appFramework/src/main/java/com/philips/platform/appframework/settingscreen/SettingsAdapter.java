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
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.philips.cdp.uikit.customviews.UIKitButton;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

/**
 * Adapter for setting List items
 * The following fields needs to be populated for the list
 */

public class SettingsAdapter extends BaseAdapter{
    private Context activityContext;
    private LayoutInflater inflater = null;
    private UserRegistrationState userRegistrationState;
    private ArrayList<SettingListItem> settingsItemList = null;
    private UIBasePresenter fragmentPresenter;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private PuiSwitch value;
    private TextView number,on_off,description,name;
    private FontIconTextView arrow;
    private SettingListItemType type;
    private View vi;
    private ProgressDialog progress;

    public SettingsAdapter(Context context, ArrayList<SettingListItem> settingsItemList,
                           UIBasePresenter fragmentPresenter) {
        activityContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userRegistrationState = new UserRegistrationState();
        this.settingsItemList = settingsItemList;
        this.fragmentPresenter = fragmentPresenter;
        sharedPreferenceUtility = new SharedPreferenceUtility(context);
    }

    @Override
    public int getCount() {
        return settingsItemList.size();
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
        if (settingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_login)).toString())
                || settingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_log_out)).toString())) {

            if (convertView == null) {
                vi = inflater.inflate(R.layout.af_settings_fragment_logout_button, null);
                UIKitButton btn_settings_logout = (UIKitButton) vi.findViewById(R.id.btn_settings_logout);
                if (userRegistrationState.getUserObject(activityContext).isUserSignIn()) {
                    btn_settings_logout.setText(getString(R.string.settings_list_item_log_out));
                } else {
                    btn_settings_logout.setText(getString(R.string.settings_list_item_login));
                }

                btn_settings_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userRegistrationState.getUserObject(activityContext).isUserSignIn()) {
                            logoutAlert();
                        } else {
                            fragmentPresenter.onLoad();
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
            type = settingsItemList.get(position).type;

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
            name.setText(settingsItemList.get(position).title);
            value.setVisibility(View.VISIBLE);

            if (userRegistrationState.getUserObject(activityContext).getReceiveMarketingEmail()) {
                value.setChecked(true);
            } else {
                value.setChecked(false);
            }
            value.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    progress = new ProgressDialog(activityContext);
                    progress.setTitle(activityContext.getResources().getString(R.string.settings_progress_title));
                    progress.setMessage(activityContext.getResources().getString(R.string.settings_progress_message));
                    progress.setCancelable(true);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                    if (isChecked) {
                        userRegistrationState.getUserObject(activityContext).updateReceiveMarketingEmail(new UpdateUserDetailsHandler() {
                            @Override
                            public void onUpdateSuccess() {
                                sharedPreferenceUtility.writePreferenceBoolean(Constants.isEmailMarketingEnabled, true);
                                progress.cancel();
                                Toast.makeText(activityContext,activityContext.getResources().getString(R.string.settings_update_success),Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onUpdateFailedWithError(int i) {
                                progress.cancel();
                                Toast.makeText(activityContext,activityContext.getResources().getString(R.string.settings_update_fail),Toast.LENGTH_LONG).show();

                            }
                        }, true);
                    } else {
                        userRegistrationState.getUserObject(activityContext).updateReceiveMarketingEmail(new UpdateUserDetailsHandler() {
                            @Override
                            public void onUpdateSuccess() {
                                sharedPreferenceUtility.writePreferenceBoolean(Constants.isEmailMarketingEnabled, false);
                                progress.cancel();
                                Toast.makeText(activityContext,activityContext.getResources().getString(R.string.settings_update_success),Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onUpdateFailedWithError(int i) {
                                progress.cancel();
                                Toast.makeText(activityContext,activityContext.getResources().getString(R.string.settings_update_fail),Toast.LENGTH_LONG).show();
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
            name.setText(settingsItemList.get(position).title);
            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            on_off.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
        }
    }

    private void headerSection(int position) {
        CharSequence titleText = null;
        titleText = settingsItemList.get(position).title;
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
        new AlertDialog.Builder(activityContext)
                .setTitle(getString(R.string.settings_list_item_log_out))
                .setMessage(activityContext.getResources().getString(R.string.settings_logout_alert))
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.settings_list_item_log_out),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                userRegistrationState.getUserObject(activityContext).logout(new LogoutHandler() {
                                    @Override
                                    public void onLogoutSuccess() {
                                        ((AppFrameworkBaseActivity)activityContext).setCartItemCount(0);
                                        fragmentPresenter.onClick(Constants.LOGOUT_BUTTON_CLICK_CONSTANT);
                                    }

                                    @Override
                                    public void onLogoutFailure(final int i, final String s) {

                                    }
                                });
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public boolean isEnabled(int position) {
        if (settingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_main)).toString())
                || settingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_purchases)).toString())
                || settingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_my_acc)).toString())) {
            return false;
        } else {
            return super.isEnabled(position);
        }
    }

    private String getString(int id) {
        return activityContext.getResources().getString(id);
    }


}
