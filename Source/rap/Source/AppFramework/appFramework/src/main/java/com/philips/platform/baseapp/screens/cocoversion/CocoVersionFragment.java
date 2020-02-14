/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.cocoversion;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;

import java.util.ArrayList;


/**
 * Created by philips on 4/18/17.
 */

public class CocoVersionFragment extends AbstractAppFrameworkBaseFragment {
    public static final String TAG = CocoVersionFragment.class.getSimpleName();
    private RecyclerView recyclerViewCoco;
    private ArrayList<CocoVersionItem> cocoVersionItemList = new ArrayList<CocoVersionItem>();

    private CocoVersionAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        updateActionBar();
    }

    protected void updateActionBar() {
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBarIcon(false);
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
        startAppTagging(TAG);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCOCOVersion();
        adapter = new CocoVersionAdapter(getActivity(), cocoVersionItemList);
        recyclerViewCoco.setAdapter(adapter);
    }

    public void getCOCOVersion() {
        CocoVersionItem ai = new CocoVersionItem();
        ai.setTitle(getResources().getString(R.string.RA_COCO_AppInfra));
        ai.setDescription(getResources().getString(R.string.RA_COCO_AppInfra_desc));
        ai.setVersion(com.philips.platform.appinfra.BuildConfig.VERSION_NAME);

        CocoVersionItem UserReg = new CocoVersionItem();
        UserReg.setTitle(getResources().getString(R.string.RA_COCO_UR));
        UserReg.setDescription(getResources().getString(R.string.RA_COCO_UR_desc));
        UserReg.setVersion(com.philips.cdp.registration.BuildConfig.VERSION_NAME);

        CocoVersionItem iap = new CocoVersionItem();
        iap.setTitle(getResources().getString(R.string.RA_COCO_IAP));
        iap.setDescription(getResources().getString(R.string.RA_COCO_IAP_desc));
        iap.setVersion(com.philips.cdp.di.iap.BuildConfig.VERSION_NAME);

        CocoVersionItem digitalCare = new CocoVersionItem();
        digitalCare.setTitle(getResources().getString(R.string.RA_COCO_CC));
        digitalCare.setDescription(getResources().getString(R.string.RA_COCO_CC_desc));
        digitalCare.setVersion(com.philips.cdp.digitalcare.BuildConfig.VERSION_NAME);

        CocoVersionItem prodReg = new CocoVersionItem();
        prodReg.setTitle(getResources().getString(R.string.RA_COCO_PR));
        prodReg.setDescription(getResources().getString(R.string.RA_COCO_PR_desc));
        prodReg.setVersion(com.philips.cdp.product_registration_lib.BuildConfig.VERSION_NAME);




        cocoVersionItemList.add(ai);
        cocoVersionItemList.add(UserReg);
        cocoVersionItemList.add(iap);
        cocoVersionItemList.add(digitalCare);
        cocoVersionItemList.add(prodReg);


    }
}
