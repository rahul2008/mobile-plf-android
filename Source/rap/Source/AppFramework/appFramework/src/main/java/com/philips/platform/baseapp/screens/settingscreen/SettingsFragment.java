/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.settingscreen;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.customviews.CircularProgressbar;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.base.AppFrameworkTagging;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Fragment is used for showing the account settings that verticals provide
 */
public class SettingsFragment extends AbstractAppFrameworkBaseFragment implements SettingsView {

    public static final String TAG = SettingsFragment.class.getSimpleName();
    private SettingsAdapter adapter = null;
    private ListView list = null;
    private UserRegistrationState userRegistrationState;
    private Handler handler = new Handler();
    private ArrayList<SettingListItem> settingScreenItemList;
    private WeakReference<SettingsFragment> settingsFragmentWeakReference;
    private ProgressBar settingsProgressBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private ArrayList<SettingListItem> buildSettingsScreenList() {
        RALog.d(TAG , "buildSettingsScreenList ");
        ArrayList<SettingListItem> settingScreenItemList = new ArrayList<SettingListItem>();
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Settings_Screen_Header_Title), SettingListItemType.HEADER, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Vertical_App_Setting_A), SettingListItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Vertical_App_Setting_B), SettingListItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Vertical_App_Setting_C), SettingListItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Setting_Philips_Promo_Title_Only), SettingListItemType.NOTIFICATION, true));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Settings_MyAccount), SettingListItemType.HEADER, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Settings_Login), SettingListItemType.CONTENT, false));
        return settingScreenItemList;
    }

    private String getResourceString(int resId) {
        if (isAdded() && getActivity() != null) {
            return getActivity().getApplicationContext().getString(resId);
        }
        return "";
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_SettingsScreen_Title);
    }

    @Override
    public void onResume() {
        RALog.d(TAG," OnResume called ");
        super.onResume();
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBarIcon(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_settings_fragment, container, false);
        fragmentPresenter = new SettingsFragmentPresenter(this);
        list = (ListView) view.findViewById(R.id.listwithouticon);
        settingsProgressBar = (CircularProgressbar) view.findViewById(R.id.settings_progress_bar);
        settingsFragmentWeakReference = new WeakReference<SettingsFragment>(this);


        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(settingScreenItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_order_history)).toString())){
                    fragmentPresenter.onEvent(Constants.IAP_PURCHASE_HISTORY);
                }
            }
        });
*/
        AppFrameworkTagging.getInstance().trackPage(TAG);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setProgressBarVisibility(true);
        startThread();
    }

    protected void startThread() {
        Thread t = new Thread(new BuildModel());
        t.start();
        RALog.d(TAG," BuildModel thread started ");
    }

    public class BuildModel implements Runnable {

        @Override
        public void run() {
            if (getActivity() != null && settingsFragmentWeakReference != null && isAdded()) {
                userRegistrationState = new UserRegistrationSettingsState();
                final boolean isUserLoggedIn = userRegistrationState.getUserObject(getActivity()).isUserSignIn();
                final boolean isMarketingEnabled = userRegistrationState.getUserObject(getActivity()).getReceiveMarketingEmail();
                settingScreenItemList = filterSettingScreenItemList(buildSettingsScreenList());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setProgressBarVisibility(false);
                        if (getActivity() != null && settingsFragmentWeakReference != null && isAdded()) {
                            setAdapterData(isUserLoggedIn, isMarketingEnabled, settingScreenItemList);
                        }
                    }
                });
            }
        }
    }

    protected void setAdapterData(boolean isUserLoggedIn, boolean isMarketingEnabled, ArrayList<SettingListItem> settingScreenItemList) {
        adapter = new SettingsAdapter(getActivity(), settingScreenItemList, fragmentPresenter, userRegistrationState, isUserLoggedIn, isMarketingEnabled);
        list.setAdapter(adapter);
    }

    protected ArrayList<SettingListItem> filterSettingScreenItemList(ArrayList<SettingListItem> settingScreenItemList) {
        userRegistrationState    = new UserRegistrationSettingsState();
        if (getActivity() != null && userRegistrationState.getUserObject(getActivity()).isUserSignIn()) {
            return settingScreenItemList;
        }
        ArrayList<SettingListItem> newSettingScreenItemList = new ArrayList<SettingListItem>();
        for (int i = 0; i < settingScreenItemList.size(); i++) {
            if (!settingScreenItemList.get(i).userRegistrationRequired) {
                newSettingScreenItemList.add(settingScreenItemList.get(i));
            }
        }
        return newSettingScreenItemList;
    }

    @Override
    public void onDetach() {
        RALog.d(TAG, " onDetach");
        super.onDetach();
        settingsFragmentWeakReference = null;
        adapter = null;
    }

    @Override
    public void onDestroyView() {
        RALog.d(TAG, " onDestroyView ");
        super.onDestroyView();
        settingsFragmentWeakReference = null;
        adapter = null;
    }

    /*
    * 'Android N' doesn't support single parameter in "Html.fromHtml". So adding the if..else condition and
    * suppressing "deprecation" for 'else' block.
    */
    @SuppressWarnings("deprecation")
    private SettingListItem formDataSection(String settingsItem, SettingListItemType type, boolean userRegistrationRequired) {
        SettingListItem settingScreenItem = new SettingListItem();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            settingScreenItem.title = Html.fromHtml(settingsItem, Html.FROM_HTML_MODE_LEGACY);
        }
        else{
            settingScreenItem.title = Html.fromHtml(settingsItem);
        }

        settingScreenItem.type = type;
        settingScreenItem.userRegistrationRequired = userRegistrationRequired;
        return settingScreenItem;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public ActionBarListener getActionBarListener() {
        return (AbstractAppFrameworkBaseActivity) getActivity();
    }

    @Override
    public int getContainerId() {
        final AbstractAppFrameworkBaseActivity activity = (AbstractAppFrameworkBaseActivity) getActivity();
        return activity.getContainerId();
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public void finishActivityAffinity() {
        getActivity().finishAffinity();
    }

    public void setProgressBarVisibility(boolean isVisible) {
        RALog.d(TAG,"setProgressBarVisibility called ");
        if (isVisible) {
            settingsProgressBar.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            settingsProgressBar.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }
}