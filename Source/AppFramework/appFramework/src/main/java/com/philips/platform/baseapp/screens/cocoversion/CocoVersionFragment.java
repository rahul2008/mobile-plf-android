package com.philips.platform.baseapp.screens.cocoversion;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.uikit.BuildConfig;
import com.philips.platform.appframework.R;


import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;
import com.philips.platform.baseapp.screens.consumercare.SupportFragmentState;
import com.philips.platform.baseapp.screens.dataservices.DataServicesState;
import com.philips.platform.baseapp.screens.inapppurchase.IAPRetailerFlowState;
import com.philips.platform.baseapp.screens.inapppurchase.IAPState;
import com.philips.platform.baseapp.screens.productregistration.ProductRegistrationState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.modularui.stateimpl.ConnectivityFragmentState;

import java.util.ArrayList;


/**
 * Created by philips on 4/18/17.
 */

public class CocoVersionFragment extends AppFrameworkBaseFragment {
    public static final String TAG = CocoVersionFragment.class.getSimpleName();
    private RecyclerView  recyclerViewCoco ;
    IAPState iapState;
    SupportFragmentState supportFragmentState;
    ProductRegistrationState productRegistrationState;
    DataServicesState dataServicesState;
    AppInfraInterface appInfra;
    ConnectivityFragmentState connectivityFragmentState;
    UserRegistrationState userRegistrationState ;
    private ArrayList<CocoVersionItem> cocoVersionItemList = new ArrayList<CocoVersionItem>();

    private CocoVersionAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        updateActionBar();
    }

    protected void updateActionBar() {
        ((AppFrameworkBaseActivity) getActivity()).updateActionBarIcon(false);
    }


    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_Coco_Version);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.af_coco_version_information, container, false);
        recyclerViewCoco = (RecyclerView) view.findViewById(R.id.coco_version_view);
        recyclerViewCoco.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCOCOVersion();
        adapter = new CocoVersionAdapter(getActivity(), cocoVersionItemList);
        recyclerViewCoco.setAdapter(adapter);
    }

    public void getCOCOVersion()
    {
        CocoVersionItem ai = new CocoVersionItem();
        appInfra = ((AppFrameworkApplication) getActivity().getApplicationContext()).getAppInfra();
        ai.title = getResources().getString(R.string.RA_COCO_AppInfra);
        ai.Version = (com.philips.platform.appinfra.BuildConfig.VERSION_NAME);

        CocoVersionItem UserReg = new CocoVersionItem();
        userRegistrationState= new UserRegistrationSettingsState();
        UserReg.title= getResources().getString(R.string.RA_COCO_UR);
        UserReg.Version=  com.philips.cdp.registration.BuildConfig.VERSION_NAME;

        CocoVersionItem uikit  = new CocoVersionItem();

        uikit.title= getResources().getString(R.string.RA_COCO_UIKIT);
        uikit.Version= (BuildConfig.VERSION_NAME);

        CocoVersionItem connectivity  = new CocoVersionItem();
        connectivityFragmentState = new ConnectivityFragmentState();
        connectivity.title= getResources().getString(R.string.RA_COCO_Connectivity);
        connectivity.Version= com.philips.cdp.dicommclient.BuildConfig.VERSION_NAME;

        CocoVersionItem iap  = new CocoVersionItem();
        iapState = new IAPRetailerFlowState();
        iap.title= getResources().getString(R.string.RA_COCO_IAP);
        iap.Version= com.philips.cdp.di.iap.BuildConfig.VERSION_NAME;

        CocoVersionItem digitalCare  = new CocoVersionItem();
        supportFragmentState= new SupportFragmentState();
        digitalCare.title= getResources().getString(R.string.RA_COCO_CC);
        digitalCare.Version= com.philips.cdp.digitalcare.BuildConfig.VERSION_NAME;

        CocoVersionItem prodReg  = new CocoVersionItem();
        productRegistrationState= new ProductRegistrationState();
        prodReg.title= getResources().getString(R.string.RA_COCO_PR);
        prodReg.Version=com.philips.cdp.product_registration_lib.BuildConfig.VERSION_NAME;

        CocoVersionItem dataService  = new CocoVersionItem();
        dataServicesState= new DataServicesState();
        dataService.title= getResources().getString(R.string.RA_COCO_DS);
        dataService.Version=com.philips.platform.dataservices.BuildConfig.VERSION_NAME;


        cocoVersionItemList.add(ai);
        cocoVersionItemList.add(UserReg);
        cocoVersionItemList.add(uikit);
        cocoVersionItemList.add(connectivity);
        cocoVersionItemList.add(iap);
        cocoVersionItemList.add(digitalCare);
        cocoVersionItemList.add(prodReg);
        cocoVersionItemList.add(dataService);

    }
}
