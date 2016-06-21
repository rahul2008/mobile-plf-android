/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.appframework.settingscreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.cdp.appframework.R;
import com.philips.cdp.appframework.userregistrationscreen.UserRegistrationActivity;
import com.philips.cdp.appframework.utility.Logger;
import com.philips.cdp.registration.User;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.shamanland.fonticon.FontIconTextView;

/**
 * ListViewSettings is responsible for showing Settings Screen.
 *
 * @author: ritesh.jha@philips.com
 * @since: June 17, 2016
 */
public class ListViewSettings extends BaseAdapter {
    public Context mActivity;
    Bundle saveBundle = new Bundle();
    private LayoutInflater inflater = null;
    private User mUser = null;
    private String[] mSettingsItemList = null;
//    private static int mPosition = 0;

    public ListViewSettings(Context context) {
        mActivity = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mUser = new User(context);
        mSettingsItemList = context.getResources().getStringArray(R.array.settingsScreen_list);
    }

    @Override
    public int getCount() {
//        Logger.i("testing","mSettingsItemList.length -- " + mSettingsItemList.length);
        return 9/*mSettingsItemList.length*/;
    }

    @Override
    public Object getItem(final int position) {
//        Logger.i("testing",".position -- " + position);
        return position;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vi = convertView;

        if (convertView == null)
            vi = inflater.inflate(R.layout.uikit_listview_without_icons, null);


        TextView name = (TextView) vi.findViewById(R.id.ifo);
        PuiSwitch value = (PuiSwitch) vi.findViewById(R.id.switch_button);
        TextView number = (TextView) vi.findViewById(R.id.numberwithouticon);
        TextView on_off = (TextView) vi.findViewById(R.id.medium);
        FontIconTextView arrow = (FontIconTextView) vi.findViewById(R.id.arrowwithouticons);
        TextView description = (TextView) vi.findViewById(R.id.text_description_without_icons);

        CharSequence titleText = null;

        if (position == 0) {
            //name.setVisibility(View.VISIBLE);
            titleText = Html.fromHtml(getString(R.string.settings_list_item_main));
            name.setText(titleText);

            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            //  arrow.setVisibility(View.GONE);
            number.setVisibility(View.GONE);
            on_off.setVisibility(View.GONE);
            arrow.setVisibility(View.INVISIBLE);
        }

        if (position == 1) {
            name.setText(getString(R.string.settings_list_item_one));

            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            //  arrow.setVisibility(View.GONE);
            on_off.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
        }

        if (position == 2) {
            name.setText(getString(R.string.settings_list_item_two));

            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            //  arrow.setVisibility(View.GONE);
            on_off.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
        }

        if (position == 3) {
            name.setText(getString(R.string.settings_list_item_three));

            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            on_off.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
        }

        if (position == 4) {
            name.setVisibility(View.VISIBLE);
            name.setText(getString(R.string.settings_list_item_four));
            value.setVisibility(View.VISIBLE);
            setSwitchState(value, "s1");

            value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    saveBundle.putBoolean("s1", ((PuiSwitch) v).isChecked());
                }
            });

            String descText = getString(R.string.settings_list_item_four_desc) + "\n" +
                    getString(R.string.settings_list_item_four_term_cond);

            description.setVisibility(View.VISIBLE);
            description.setText(descText);
            //  mBadge.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.GONE);

//            if(!mUser.isUserSignIn()){
//                vi.setVisibility(View.GONE);
//            }
        }

//        if (mUser.isUserSignIn()) {
//            if(!mUser.isUserSignIn()){
//                vi.setVisibility(View.GONE);
//            }
            if (position == 5) {
                titleText = Html.fromHtml(getString(R.string.settings_list_item_purchases));
                //name.setVisibility(View.VISIBLE);
                name.setText(titleText);

                value.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                number.setVisibility(View.GONE);
                on_off.setVisibility(View.GONE);
                arrow.setVisibility(View.INVISIBLE);
            }

            if (position == 6) {
                name.setText(getString(R.string.settings_list_item_order_history));

                value.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                on_off.setVisibility(View.GONE);
                arrow.setVisibility(View.VISIBLE);
            }
//        }

        if (position == 7) {
            //name.setVisibility(View.VISIBLE);
            titleText = Html.fromHtml(getString(R.string.settings_list_item_my_acc));
            name.setText(titleText);

            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            number.setVisibility(View.GONE);
            on_off.setVisibility(View.GONE);
            arrow.setVisibility(View.INVISIBLE);
        }

        if (position == 8) {
            if (mUser.isUserSignIn()) {
                name.setText(getString(R.string.settings_list_item_log_out));
                logoutAlert();
            } else {
                name.setText(getString(R.string.settings_list_item_login));
                loginUserRegistration(vi);
            }

            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            on_off.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
        }
        return vi;
    }

    private void loginUserRegistration(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, UserRegistrationActivity.class));
            }
        });
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
                                mActivity.startActivity(new Intent(mActivity, UserRegistrationActivity.class));
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
