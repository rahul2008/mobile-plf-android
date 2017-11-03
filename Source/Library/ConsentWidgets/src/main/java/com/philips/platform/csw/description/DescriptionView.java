/*
 * Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * * in whole or in part is prohibited without the prior written
 * * consent of the copyright holder.
 * /
 */

package com.philips.platform.csw.description;

import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.mya.consentwidgets.R;

import android.os.Bundle;
import android.view.*;

public class DescriptionView extends CswBaseFragment implements
        DescriptionInterface {

    private DescriptionPresenter descriptionPresenter;

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {
    }

    @Override
    public int getTitleResourceId() {
        return R.string.csw_what_does_this_mean;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_permission_view, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        descriptionPresenter = new DescriptionPresenter(this, getContext());
    }
}
