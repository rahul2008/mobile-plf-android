/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp.digitalcare.CcInterface;
import com.philips.cdp.digitalcare.CcLaunchInput;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.powersleep.R;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProductSupportViewModelTest {

    private ProductSupportViewModel viewModel;

    @Mock
    private CcLaunchInput ccLauncherInputMock;

    @Mock
    private CcInterface ccInterfaceMock;

    @Mock
    private UappDependencies uAppDependenciesMock;

    @Mock
    private UappSettings uAppSettingsMock;

    @Mock
    private ScreenFlowController sfcMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new ProductSupportViewModel(ccLauncherInputMock, ccInterfaceMock,
                uAppDependenciesMock, uAppSettingsMock, sfcMock);
    }

    @Test
    public void shouldSetToolbarTitleWhenTitleIDIsReceived() throws Exception {
        viewModel.updateActionBar(R.string.ews_title, false);

        verify(sfcMock).setToolbarTitle(R.string.ews_title);
    }

    @Test
    public void shouldSetToolbarTitleWhenTitleStringIsReceived() throws Exception {
        String support = "Support";
        viewModel.updateActionBar(support, false);

        verify(sfcMock).setToolbarTitle(support);
    }

    @Test
    public void shouldLaunchConsumerCareWithCorrectProductDetailsWhenRequested() throws Exception {
        ArgumentCaptor<ProductModelSelectionType> typeArgumentCaptor = ArgumentCaptor.forClass(ProductModelSelectionType.class);
        final UiLauncher uiLauncherMock = mock(UiLauncher.class);
        viewModel.showProductSupportScreen(uiLauncherMock);

        verify(ccLauncherInputMock).setProductModelSelectionType(typeArgumentCaptor.capture());
        String[] hardCodedProductList = typeArgumentCaptor.getValue().getHardCodedProductList();

        assertEquals(1, hardCodedProductList.length);
        assertEquals(ProductSupportViewModel.BRIGHT_EYES_CTN, hardCodedProductList[0]);

        verify(ccInterfaceMock).init(uAppDependenciesMock, uAppSettingsMock);
        verify(ccInterfaceMock).launch(uiLauncherMock, ccLauncherInputMock);
    }
}