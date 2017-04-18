package com.philips.platform.baseapp.screens.settingscreen;/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
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
import com.philips.platform.appframework.R;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkTagging;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.SharedPreferenceUtility;
import com.philips.platform.baseapp.screens.utility.TaggingConstants;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

/**
 * Adapter for setting List items
 * The following fields needs to be populated for the list
 */

public class SettingsAdapter extends BaseAdapter {
    private Context activityContext;
    private LayoutInflater inflater = null;
    private UserRegistrationState userRegistrationState;
    private ArrayList<SettingListItem> settingsItemList = null;
    private UIBasePresenter fragmentPresenter;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private ProgressDialog progress, progressDialog;
    private int LOGIN_VIEW = 0;
    private int VERTICAL_SETTING_VIEW = 1;
    private boolean isUserLoggedIn = false;
    private boolean isMarketingEnabled = false;
    private AlertDialog logoutAlertDialog;
    private UIKitButton btn_settings_logout = null;
    private String viewHolderTag = null;

    public SettingsAdapter(Context context, ArrayList<SettingListItem> settingsItemList,
                           UIBasePresenter fragmentPresenter, UserRegistrationState userRegistrationState, boolean isUserLoggedIn, boolean isMarketingEnabled) {
        activityContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.settingsItemList = settingsItemList;
        this.fragmentPresenter = fragmentPresenter;
        this.isUserLoggedIn = isUserLoggedIn;
        this.isMarketingEnabled = isMarketingEnabled;
        this.userRegistrationState = userRegistrationState;
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
    public int getViewTypeCount() {
        return 2;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getItemViewType(int position) {
        if (settingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.RA_Settings_Login)).toString())
                || settingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.RA_Settings_Logout)).toString())) {
            return LOGIN_VIEW;
        }
        return VERTICAL_SETTING_VIEW;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        int type = getItemViewType(position);
        VerticalViewHolder verticalViewHolder = null;
        if (convertView == null) {
            if (type == LOGIN_VIEW) {
                convertView = inflater.inflate(R.layout.af_settings_fragment_logout_button, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.uikit_listview_without_icons, parent, false);
                verticalViewHolder = new VerticalViewHolder();
                verticalViewHolder.name = (TextView) convertView.findViewById(R.id.ifo);
                verticalViewHolder.value = (PuiSwitch) convertView.findViewById(R.id.switch_button);
                verticalViewHolder.number = (TextView) convertView.findViewById(R.id.numberwithouticon);
                verticalViewHolder.on_off = (TextView) convertView.findViewById(R.id.medium);
                verticalViewHolder.arrow = (FontIconTextView) convertView.findViewById(R.id.arrowwithouticons);
                verticalViewHolder.description = (TextView) convertView.findViewById(R.id.text_description_without_icons);
                convertView.setTag(verticalViewHolder);
            }
        }
        if (type == LOGIN_VIEW) {
            loginButtonView(convertView);
        } else {
            verticalViewHolder = (VerticalViewHolder) convertView.getTag();
            verticalAppView(position, verticalViewHolder);
        }
        return convertView;
    }

    protected void verticalAppView(int position, VerticalViewHolder viewHolder) {

        SettingListItemType type = settingsItemList.get(position).type;

        switch (type) {
            case HEADER:
                headerSection(position, viewHolder);
                break;
            case CONTENT:
                subSection(position, viewHolder);
                break;
            case NOTIFICATION:
                notificationSection(position, viewHolder);
                break;
        }
    }

    public static class VerticalViewHolder {
        public TextView number, on_off, description, name;
        public PuiSwitch value;
        public FontIconTextView arrow;
    }

    protected UIKitButton getUrButton() {
        return btn_settings_logout;
    }

    protected String getViewHolderTag() {
        return viewHolderTag;
    }

    protected void loginButtonView(View vi) {
        btn_settings_logout = (UIKitButton) vi.findViewById(R.id.btn_settings_logout);
        if (isUserLoggedIn) {
            btn_settings_logout.setText(getString(R.string.RA_Settings_Logout));
        } else {
            btn_settings_logout.setText(getString(R.string.RA_Settings_Login));
        }

        btn_settings_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_settings_logout.setEnabled(false);
                if (userRegistrationState.getUserObject(activityContext).isUserSignIn()) {
                    logoutAlert();
                } else {
                    fragmentPresenter.onEvent(Constants.LOGIN_BUTTON_CLICK_CONSTANT);
                }
                btn_settings_logout.setEnabled(true);
            }
        });
    }

    private void notificationSection(int position, VerticalViewHolder viewHolder) {
        if (null != viewHolder.name && null != viewHolder.value && null != viewHolder.description && null != viewHolder.number && null != viewHolder.on_off && null != viewHolder.arrow) {
            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.name.setText(settingsItemList.get(position).title);
            viewHolder.value.setVisibility(View.VISIBLE);
            if (isMarketingEnabled) {
                viewHolder.value.setChecked(true);
            } else {
                viewHolder.value.setChecked(false);
            }
            viewHolder.value.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    progress = new ProgressDialog(activityContext);
                    progress.setTitle(activityContext.getResources().getString(R.string.RA_Settings_Progress_Title));
                    progress.setMessage(activityContext.getResources().getString(R.string.RA_Settings_Progress_Message));
                    progress.setCancelable(true);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                    if (isChecked) {
                        userRegistrationState.getUserObject(activityContext).updateReceiveMarketingEmail(new UpdateUserDetailsHandler() {
                            @Override
                            public void onUpdateSuccess() {
                                isMarketingEnabled = true;
                                sharedPreferenceUtility.writePreferenceBoolean(Constants.isEmailMarketingEnabled, true);
                                progress.cancel();
                                Toast.makeText(activityContext, activityContext.getResources().getString(R.string.RA_Settings_Update_Success), Toast.LENGTH_LONG).show();
                                AppFrameworkTagging.getInstance().trackAction(TaggingConstants.MARKETING_OPT_IN);
                            }

                            @Override
                            public void onUpdateFailedWithError(int i) {
                                progress.cancel();
                                Toast.makeText(activityContext, activityContext.getResources().getString(R.string.RA_Settings_Update_Fail), Toast.LENGTH_LONG).show();

                            }
                        }, true);
                    } else {
                        userRegistrationState.getUserObject(activityContext).updateReceiveMarketingEmail(new UpdateUserDetailsHandler() {
                            @Override
                            public void onUpdateSuccess() {
                                isMarketingEnabled = false;
                                sharedPreferenceUtility.writePreferenceBoolean(Constants.isEmailMarketingEnabled, false);
                                progress.cancel();
                                Toast.makeText(activityContext, activityContext.getResources().getString(R.string.RA_Settings_Update_Success), Toast.LENGTH_LONG).show();
                                AppFrameworkTagging.getInstance().trackAction(TaggingConstants.MARKETING_OPT_OUT);
                            }

                            @Override
                            public void onUpdateFailedWithError(int i) {
                                progress.cancel();
                                Toast.makeText(activityContext, activityContext.getResources().getString(R.string.RA_Settings_Update_Fail), Toast.LENGTH_LONG).show();
                            }
                        }, false);
                    }
                }
            });

            String descText = getString(R.string.RA_Setting_Philips_Promo_PartTwo) + "\n" +
                    getString(R.string.RA_Settings_Promo_Question_Text);

            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(descText);
            viewHolder.arrow.setVisibility(View.GONE);

            viewHolderTag = String.valueOf(SettingListItemType.NOTIFICATION);
        }
    }

    private void subSection(int position, VerticalViewHolder viewHolder) {
        if (null != viewHolder.name && null != viewHolder.value && null != viewHolder.description && null != viewHolder.number && null != viewHolder.on_off && null != viewHolder.arrow) {
            viewHolder.name.setText(settingsItemList.get(position).title);
            viewHolder.value.setVisibility(View.GONE);
            viewHolder.description.setVisibility(View.GONE);
            viewHolder.on_off.setVisibility(View.GONE);
            viewHolder.arrow.setVisibility(View.VISIBLE);
            viewHolderTag = String.valueOf(SettingListItemType.CONTENT);
        }
    }

    private void headerSection(int position, VerticalViewHolder viewHolder) {
        CharSequence titleText;
        titleText = settingsItemList.get(position).title;
        if (null != viewHolder.name && null != viewHolder.value && null != viewHolder.description && null != viewHolder.number && null != viewHolder.on_off && null != viewHolder.arrow) {
            viewHolder.name.setText(titleText);
            viewHolder.value.setVisibility(View.GONE);
            viewHolder.description.setVisibility(View.GONE);
            viewHolder.number.setVisibility(View.GONE);
            viewHolder.on_off.setVisibility(View.GONE);
            viewHolder.arrow.setVisibility(View.INVISIBLE);
            viewHolderTag = String.valueOf(SettingListItemType.HEADER);
        }

    }

    protected void logoutAlert() {
        if (logoutAlertDialog != null && logoutAlertDialog.isShowing()) {
            return;
        }
        logoutAlertDialog = new AlertDialog.Builder(activityContext, R.style.alertDialogStyle)
                .setTitle(getString(R.string.RA_Settings_Logout))
                .setMessage(activityContext.getResources().getString(R.string.RA_Settings_Logout_Alert))
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.RA_Settings_Logout),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog = new ProgressDialog(activityContext);
                                progressDialog.setTitle(activityContext.getResources().getString(R.string.RA_Settings_Progress_Title));
                                progressDialog.setMessage(activityContext.getResources().getString(R.string.RA_Settings_Progress_Message));
                                progressDialog.setCancelable(false);
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                String isAutoLogoutEnabled= (String) ((AppFrameworkApplication)activityContext.getApplicationContext()).getAppInfra().getConfigInterface().getPropertyForKey("PushNotification.autoLogout","ReferenceApp",new AppConfigurationInterface.AppConfigurationError());
                                if(!TextUtils.isEmpty(isAutoLogoutEnabled) && Boolean.parseBoolean(isAutoLogoutEnabled)){
                                    PushNotificationManager.getInstance().deregisterTokenWithBackend(activityContext.getApplicationContext(), new PushNotificationManager.DeregisterTokenListener() {
                                        @Override
                                        public void onSuccess() {
                                            doLogout();
                                        }

                                        @Override
                                        public void onError() {
                                            doLogout();
                                        }
                                    });
                                }else{
                                    doLogout();
                                }

                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isEnabled(int position) {
        if (settingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.RA_Settings_Screen_Header_Title)).toString())
                || settingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.RA_Settings_Purchases)).toString())
                || settingsItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.RA_Settings_MyAccount)).toString())) {
            return false;
        } else {
            return super.isEnabled(position);
        }
    }

    private String getString(int id) {
        return activityContext.getResources().getString(id);
    }

    public void doLogout() {
        userRegistrationState.getUserObject(activityContext).logout(new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {
                //    ((AppFrameworkBaseActivity)activityContext).setCartItemCount(0);
                //TODO:Need to call UserRegistrationState on logout call. Now its crashingif we do so.
                progressDialog.cancel();
                if (activityContext instanceof IndexSelectionListener) {
                    ((IndexSelectionListener) activityContext).updateSelectionIndex(0);
                }
                fragmentPresenter.onEvent(Constants.LOGOUT_BUTTON_CLICK_CONSTANT);

            }

            @Override
            public void onLogoutFailure(final int i, final String s) {
                progressDialog.cancel();
                Toast.makeText(activityContext, getString(R.string.RA_Logout_Failed), Toast.LENGTH_LONG).show();
            }
        });
    }

}