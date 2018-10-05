package com.philips.platform.ews.homewificonnection;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ews.R;
import com.philips.platform.ews.base.BaseFragment;
import com.philips.platform.ews.configuration.BaseContentConfiguration;

import com.philips.platform.ews.databinding.FragmentSelectWifiViewBinding;
import com.philips.platform.ews.microapp.EWSActionBarListener;

public class SelectWiFiFragment extends BaseFragment implements SelectWiFiViewModel.SelectWiFiViewCallback { //MultiFlowBaseFragment<SelectWiFiViewModel, FragmentSelectWifiViewBinding> {

    private static final String TAG = "SelectWiFiFragment";
    @Nullable
    SelectWiFiViewModel viewModel;

    /*@Override
    SelectWiFiViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_wifi_view;
    }

    @NonNull
    @Override
    protected String getPageName() {
        return Pages.CONFIRM_WIFI;
    }

    @Override
    protected void bindViewModel(final FragmentSelectWifiViewBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EWSActionBarListener actionBarListener = null;
        try {
            actionBarListener = ((EWSActionBarListener) getContext());
        } catch (ClassCastException e) {}

        if (actionBarListener != null) {
            actionBarListener.closeButton(true);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.select_wifi_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            viewModel.fetchWifiNodes();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSelectWifiViewBinding viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_wifi_view,
                container, false);
        viewModel = createViewModel();
        viewModel.setFragmentCallback(this);
        viewDataBinding.setViewModel(viewModel);

        return viewDataBinding.getRoot();
    }

    private SelectWiFiViewModel createViewModel() {
        return getEWSComponent().selectWiFiViewModel();
    }

    /*@Override
    public void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(@NonNull BroadcastReceiver receiver) {
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            if (viewModel != null){
                viewModel.getEwsLogger().e(TAG, e.toString());
            }
        }
    }*/


    @Override
    public void showTroubleshootSelectWifiDialog(@NonNull BaseContentConfiguration baseContentConfiguration) {
        Context context = getContext();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }


    @Override
    public boolean handleBackEvent() {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.fetchWifiNodes();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.cleanUp();
    }

    @Override
    protected void callTrackPageName() {
        if (viewModel != null) {
            viewModel.trackPageName();
        }
    }
}
