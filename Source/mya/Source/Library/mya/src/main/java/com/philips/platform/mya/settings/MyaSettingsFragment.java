/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philips.cdp.registration.dao.UserDataProvider;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.MyaLocalizationHandler;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBaseFragment;
import com.philips.platform.mya.base.MyaBasePresenter;
import com.philips.platform.mya.catk.ConsentsClient;
import com.philips.platform.mya.csw.CswDependencies;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.CswLaunchInput;
import com.philips.platform.mya.dialogs.DialogView;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.text.utils.UIDClickableSpanWrapper;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarButton;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.philips.platform.mya.settings.MyaPhilipsLinkFragment.PHILIPS_LINK;


public class MyaSettingsFragment extends MyaBaseFragment implements View.OnClickListener,MyaSettingsContract.View {


    public static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";
    private MyaSettingsContract.Presenter presenter;
    private RecyclerView recyclerView;
    private String SETTINGS_BUNDLE = "settings_bundle";
    private boolean isDialogOpen = false;
    private String DIALOG_TITLE = "dialog_title", DIALOG_MESSAGE = "dialog_message", DIALOG_OPEN = "dialog_open";
    private String dialogTitle,dialogMessage;
    private DefaultItemAnimator defaultItemAnimator;
    private RecyclerViewSeparatorItemDecoration recyclerViewSeparatorItemDecoration;
    private LinearLayoutManager linearLayoutManager;
    private AppConfigurationInterface.AppConfigurationError error;
    private ProgressBarButton logout;
    private Label philipsWebsite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_settings_fragment, container, false);
        UIDHelper.injectCalligraphyFonts();
        setRetainInstance(true);
        initViews(view);
        presenter = new MyaSettingsPresenter(this);
        init(new DefaultItemAnimator(),new RecyclerViewSeparatorItemDecoration(getContext()),
                new LinearLayoutManager(getContext()));
        error = new AppConfigurationInterface.AppConfigurationError();
        return view;
    }

    void init(DefaultItemAnimator defaultItemAnimator, RecyclerViewSeparatorItemDecoration recyclerViewSeparatorItemDecoration, LinearLayoutManager linearLayoutManager) {
        this.defaultItemAnimator=defaultItemAnimator;
        this.recyclerViewSeparatorItemDecoration = recyclerViewSeparatorItemDecoration;
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        dismissDialog();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.mya_settings_recycler_view);
        Button logOutButton = view.findViewById(R.id.mya_settings_logout_btn);
        philipsWebsite = view.findViewById(R.id.philips_website);
        philipsWebsite.setSpanClickInterceptor(new UIDClickableSpanWrapper.ClickInterceptor() {
            @Override
            public boolean interceptClick(CharSequence tag) {
                MyaPhilipsLinkFragment fragment = new MyaPhilipsLinkFragment();
                Bundle args = new Bundle();
                args.putCharSequence(PHILIPS_LINK,tag);
                fragment.setArguments(args);
                showFragment(fragment);
                return true;
            }
        });
        logOutButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(DIALOG_OPEN)) {
                dismissDialog();
                showDialog(savedInstanceState.getString(DIALOG_TITLE), savedInstanceState.getString(DIALOG_MESSAGE));
            } else {
                dismissDialog();
            }
            presenter.getSettingItems(MyaHelper.getInstance().getAppInfra(), error, savedInstanceState.getBundle(SETTINGS_BUNDLE));
        } else {
            presenter.getSettingItems(MyaHelper.getInstance().getAppInfra(), error, getArguments());
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(SETTINGS_BUNDLE, getArguments());
        outState.putString(DIALOG_MESSAGE, dialogMessage);
        outState.putString(DIALOG_TITLE, dialogTitle);
        outState.putBoolean(DIALOG_OPEN, isDialogOpen);
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getActionbarTitleResId() {
        return R.string.MYA_My_account;
    }

    @Override
    public String getActionbarTitle(Context context) {
        return context.getString(R.string.MYA_My_account);
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }

    @Override
    public void onClick(View view) {
        int viewType = view.getId();
        if (viewType == R.id.mya_settings_logout_btn) {
            showDialog(getString(R.string.MYA_logout_title), getString(R.string.MYA_logout_message));
        }
    }

    @Override
    public void showOfflineDialog(String title, String message) {
        new DialogView(title, message).showDialog(getActivity());
    }

    private void showDialog(String title, String message) {
        this.dialogTitle = title;
        this.dialogMessage = message;
        this.isDialogOpen = true;
        LayoutInflater inflater = LayoutInflater.from(getContext()).cloneInContext(UIDHelper.getPopupThemedContext(getContext()));
        View dialogView = inflater.inflate(R.layout.mya_dialog_layout, null);
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDialogView(dialogView)
                .setDimLayer(DialogConstants.DIM_SUBTLE)
                .setCancelable(false)
                .setDividers(false);

        Label textView = dialogView.findViewById(R.id.message_label);
        Label title_label = dialogView.findViewById(R.id.title_label);
        logout = dialogView.findViewById(R.id.mya_dialog_logout_btn);
        Button cancel = dialogView.findViewById(R.id.mya_dialog_cancel_btn);
        textView.setText(message);
        title_label.setText(title);
        AlertDialogFragment alertDialogFragment = builder.create();
        alertDialogFragment.show(getChildFragmentManager(), ALERT_DIALOG_TAG);

        logout.setOnClickListener(onClickLogOut(logout));
        cancel.setOnClickListener(handleOnClickCancel());
    }

    private View.OnClickListener handleOnClickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDialogOpen = false;
                dismissDialog();
            }
        };
    }

    private void dismissDialog() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isDestroyed()) {
            Fragment prev = getChildFragmentManager().findFragmentByTag(ALERT_DIALOG_TAG);
            if (prev != null && prev instanceof AlertDialogFragment) {
                AlertDialogFragment alertDialogFragment = (AlertDialogFragment) prev;
                alertDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    private View.OnClickListener onClickLogOut(final ProgressBarButton logout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* boolean onLogOut = MyaHelper.getInstance().getMyaListener().onClickMyaItem(view.getContext().getString(R.string.mya_log_out));
                if(!onLogOut) {
                    presenter.logOut(getArguments());
                }*/
               //TODO - need to invoke above commented code when introduced call back for log out success
               logout.showProgressIndicator();
               presenter.logOut(getArguments());
            }
        };
    }

    @Override
    public void showSettingsItems(Map<String, SettingsModel> dataModelLinkedHashMap) {
        MyaSettingsAdapter myaSettingsAdaptor = new MyaSettingsAdapter(dataModelLinkedHashMap);
        RecyclerView.LayoutManager mLayoutManager = getLinearLayoutManager();
        RecyclerViewSeparatorItemDecoration contentThemedRightSeparatorItemDecoration = getRecyclerViewSeparatorItemDecoration();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(contentThemedRightSeparatorItemDecoration);
        recyclerView.setItemAnimator(getAnimator());
        recyclerView.setAdapter(myaSettingsAdaptor);
        myaSettingsAdaptor.setOnClickListener(getOnClickListener(dataModelLinkedHashMap));
    }

    protected DefaultItemAnimator getAnimator() {
        return defaultItemAnimator;
    }

    protected RecyclerViewSeparatorItemDecoration getRecyclerViewSeparatorItemDecoration() {
        return recyclerViewSeparatorItemDecoration;
    }

    protected LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    @Override
    public void onLogOutSuccess() {
        dismissDialog();
        exitMyAccounts();
        MyaHelper.getInstance().getMyaListener().onClickMyaItem(getContext().getString(R.string.mya_log_out));
    }

    @Override
    public void hideProgressIndicator() {
        if (logout != null)
            logout.hideProgressIndicator();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setLinkUrl(String url) {
        philipsWebsite.setClickable(true);
        philipsWebsite.setMovementMethod (LinkMovementMethod.getInstance());
        philipsWebsite.setText(Html.fromHtml(url));
    }


    private View.OnClickListener getOnClickListener(final Map<String, SettingsModel> profileList) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewType = recyclerView.indexOfChild(view);
                String key = (String) profileList.keySet().toArray()[viewType];
                SettingsModel value = profileList.get(key);
                boolean handled = presenter.handleOnClickSettingsItem(key, getFragmentLauncher());
                if (!handled) {
                    boolean onClickMyaItem = MyaHelper.getInstance().getMyaListener().onClickMyaItem(key);
                    if (!onClickMyaItem)
                        presenter.onClickRecyclerItem(key, value);
                }

            }
        };
    }

    AppConfigurationInterface.AppConfigurationError getError() {
        return error;
    }

    static class MyaSettingsPresenter extends MyaBasePresenter<MyaSettingsContract.View> implements MyaSettingsContract.Presenter {

        private MyaSettingsContract.View view;

        MyaSettingsPresenter(MyaSettingsContract.View view) {
            this.view = view;
        }

        @Override
        public void getSettingItems(AppInfraInterface appInfra, AppConfigurationInterface.AppConfigurationError error, Bundle arguments) {
            appInfra.getServiceDiscovery().getServiceUrlWithLanguagePreference("userreg.landing.myphilips", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    view.setLinkUrl(url.toString());
                }

                @Override
                public void onError(ERRORVALUES error, String message) {

                }
            });
            view.showSettingsItems(getSettingsMap(appInfra, arguments, error));
        }

        @Override
        public void onClickRecyclerItem(String key, SettingsModel settingsModel) {
        }

        @Override
        public void logOut(Bundle bundle) {
            UserDataProvider userDataProvider = (UserDataProvider) MyaHelper.getInstance().getUserDataInterface();
            if (userDataProvider != null) {
                userDataProvider.logOut(getLogoutListener());
            }
        }

        @Override
        public boolean handleOnClickSettingsItem(String key, FragmentLauncher fragmentLauncher) {
            if (key.equals("Mya_Privacy_Settings")) {
                RestInterface restInterface = getRestClient();
                if (restInterface.isInternetReachable()) {
                    MyaDependencies myaDeps = getDependencies();
                    CswDependencies dependencies = new CswDependencies(myaDeps.getAppInfra(), myaDeps.getConsentConfigurationList());
                    CswInterface cswInterface = getCswInterface();
                    UappSettings uappSettings = new UappSettings(view.getContext());
                    cswInterface.init(dependencies, uappSettings);
                    cswInterface.launch(fragmentLauncher, buildLaunchInput(true, view.getContext()));
                    return true;
                } else {
                    String title = getContext().getString(R.string.MYA_Offline_title);
                    String message = getContext().getString(R.string.MYA_Offline_message);
                    view.showOfflineDialog(title, message);
                }
            }
            return false;
        }

        ConsentsClient getConsentsClient() {
            return ConsentsClient.getInstance();
        }

        CswInterface getCswInterface() {
            return new CswInterface();
        }

        protected LogoutListener getLogoutListener(){
            return new LogoutListener() {
                @Override
                public void onLogoutSuccess() {
                    view.onLogOutSuccess();
                }

                @Override
                public void onLogoutFailure(int errorCode, String errorMessage) {
                    Toast.makeText(view.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    view.hideProgressIndicator();
                }
            };
        }

        CswLaunchInput buildLaunchInput(boolean addToBackStack, Context context) {
            CswLaunchInput cswLaunchInput = new CswLaunchInput(context);
            cswLaunchInput.addToBackStack(addToBackStack);
            return cswLaunchInput;
        }

        private Map<String, SettingsModel> getSettingsMap(AppInfraInterface appInfraInterface, Bundle arguments, AppConfigurationInterface.AppConfigurationError error) {
            String settingItems = "settings.menuItems";
            List<?> list = null;
            if (arguments != null)
                list = MyaHelper.getInstance().getMyaLaunchInput().getSettingsMenuList();

            if (list == null || list.isEmpty()) {
                try {
                    list = (ArrayList<?>) appInfraInterface.getConfigInterface().getPropertyForKey(settingItems, "mya", error);
                } catch (IllegalArgumentException exception) {
                    exception.getMessage();
                }
            }
            return getLocalisedList(list, appInfraInterface);
        }

        private LinkedHashMap<String, SettingsModel> getLocalisedList(List<?> propertyForKey, AppInfraInterface appInfraInterface) {
            LinkedHashMap<String, SettingsModel> profileList = new LinkedHashMap<>();
            MyaLocalizationHandler myaLocalizationHandler = new MyaLocalizationHandler();
            SettingsModel privacySettingsModel = new SettingsModel();
            privacySettingsModel.setFirstItem(view.getContext().getString(R.string.Mya_Privacy_Settings));
            profileList.put("Mya_Privacy_Settings", privacySettingsModel);
            if (propertyForKey != null && propertyForKey.size() != 0) {
                for (int i = 0; i < propertyForKey.size(); i++) {
                    SettingsModel settingsModel = new SettingsModel();
                    String key = (String) propertyForKey.get(i);
                    settingsModel.setFirstItem(myaLocalizationHandler.getStringResourceByName(view.getContext(), key));
                    if (key.equals("MYA_Country")) {
                        settingsModel.setItemCount(2);
                        settingsModel.setSecondItem(appInfraInterface.getServiceDiscovery().getHomeCountry());
                    }
                    profileList.put(key, settingsModel);
                }
            }
            return profileList;
        }

        private Context getContext() {
            return view.getContext();
        }

        // Visible for testing
        protected RestInterface getRestClient() {
            return getDependencies().getAppInfra().getRestClient();
        }

        protected MyaDependencies getDependencies() {
            return MyaInterface.get().getDependencies();
        }
    }
}
