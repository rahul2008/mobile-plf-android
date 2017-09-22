/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.philips.cdp.digitalcare.CcInterface;
import com.philips.cdp.digitalcare.CcLaunchInput;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import javax.inject.Inject;

public class ProductSupportViewModel implements ActionBarListener {

    //Keeping it as is as, since as EWS becomes a separate component this would be injected in from the outside.
    static final String BRIGHT_EYES_CTN = "HF3505/01";
    private final ScreenFlowController screenFlowController;
    private CcLaunchInput ccLaunchInput;
    private CcInterface ccInterface;
    private UappDependencies dependencies;
    private UappSettings settings;

    @Inject
    public ProductSupportViewModel(@NonNull final CcLaunchInput ccLaunchInput, @NonNull final CcInterface ccInterface,
                                   @NonNull final UappDependencies uappDependencies, @NonNull final UappSettings uappSettings,
                                   @NonNull final ScreenFlowController screenFlowController) {
        this.ccLaunchInput = ccLaunchInput;
        this.ccInterface = ccInterface;
        this.dependencies = uappDependencies;
        this.settings = uappSettings;
        this.screenFlowController = screenFlowController;
    }

    @Override
    public void updateActionBar(@StringRes final int toolbarTitleId, final boolean b) {
        screenFlowController.setToolbarTitle(toolbarTitleId);
    }

    @Override
    public void updateActionBar(final String toolbarTitle, final boolean b) {
        screenFlowController.setToolbarTitle(toolbarTitle);
    }

    public void showProductSupportScreen(@NonNull final UiLauncher fragmentLauncher) {
        ProductModelSelectionType productsSelection = new HardcodedProductList(new String[]{BRIGHT_EYES_CTN});
        productsSelection.setCatalog(PrxConstants.Catalog.CARE);
        productsSelection.setSector(PrxConstants.Sector.B2C);
        ccLaunchInput.setProductModelSelectionType(productsSelection);
        ccInterface.init(dependencies, settings);
        ccInterface.launch(fragmentLauncher, ccLaunchInput);
    }
}