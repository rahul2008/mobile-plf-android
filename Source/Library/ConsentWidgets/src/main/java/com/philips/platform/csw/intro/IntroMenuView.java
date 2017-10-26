/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 *//*


package com.philips.platform.csw.intro;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.csw.CswFragment;

import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uid.view.widget.Button;

public class IntroMenuView extends CswBaseFragment implements View.OnClickListener {
    private Button mPermissionBtn;

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {

    }

    @Override
    public int getTitleResourceId() {
        return R.string.csw_intro;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_intro_menu, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mPermissionBtn = (Button) view.findViewById(R.id.launch_permissions);
        mPermissionBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ((CswFragment)getParentFragment()).inflatePermissionView();
    }
}
*/
