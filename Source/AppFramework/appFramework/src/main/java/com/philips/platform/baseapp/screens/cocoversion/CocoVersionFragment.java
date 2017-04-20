package com.philips.platform.baseapp.screens.cocoversion;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.philips.platform.appframework.R;


import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;

import java.util.ArrayList;


/**
 * Created by philips on 4/18/17.
 */

public class CocoVersionFragment extends AppFrameworkBaseFragment {
    public static final String TAG = CocoVersionFragment.class.getSimpleName();
    private RecyclerView  recyclerViewCoco ;
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
        AppInfraInterface appInfra = ((AppFrameworkApplication) getActivity().getApplicationContext()).getAppInfra();
        ai.title =getResources().getString(R.string.RA_COCO_APPINFRA);
        ai.Version = ((AppInfra)appInfra).getVersion();

        CocoVersionItem UserReg = new CocoVersionItem();
        UserReg.title=getResources().getString(R.string.RA_COCO_UR);
        userRegistrationState= new UserRegistrationSettingsState();
        UserReg.Version=userRegistrationState.getVersion();

        CocoVersionItem uikit  = new CocoVersionItem();
        uikit.title=getResources().getString(R.string.RA_COCO_UIKIT);
        uikit.Version=getResources().getString(R.string.RA_COCO_UIKIT_VERSION);

        CocoVersionItem connectivity  = new CocoVersionItem();
        connectivity.title=getResources().getString(R.string.RA_COCO_Connectivity);
        connectivity.Version=getResources().getString(R.string.RA_COCO_Connectivity_version);;

        CocoVersionItem iap  = new CocoVersionItem();
        iap.title=getResources().getString(R.string.RA_COCO_IAP);
        iap.Version=getResources().getString(R.string.RA_COCO_IAP_VERSION);;

        CocoVersionItem digitalCare  = new CocoVersionItem();
        digitalCare.title=getResources().getString(R.string.RA_COCO_CC);
        digitalCare.Version=getResources().getString(R.string.RA_COCO_CC_VERSION);


        CocoVersionItem prodReg  = new CocoVersionItem();
        prodReg.title=getResources().getString(R.string.RA_COCO_PR);
        prodReg.Version=getResources().getString(R.string.RA_COCO_PR_VERSION);

        CocoVersionItem dataService  = new CocoVersionItem();
        dataService.title=getResources().getString(R.string.RA_COCO_DS);
        dataService.Version=getResources().getString(R.string.RA_COCO_DS_VERSION);


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
