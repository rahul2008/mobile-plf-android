
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.locatephilips.fragments;

import android.content.res.Configuration;
import android.view.View;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.util.DigitalCareTestMock;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest(DigitalCareConfigManager.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*", "org.apache.xerces", "javax.xml.*", "org.xml.sax.*", "org.w3c.dom.*", "org.springframework.context.*", "org.apache.log4j.*"})
public class ServiceLocatorFragmentTest {

    private ServiceLocatorFragment fragment;

    private View rootView;

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private DigitalCareConfigManager mockDigitalCareConfigManager;

    @Mock
    private AppTaggingInterface mockAppTaggingInterface;

    @Mock
    private AppInfraInterface appInfraInterfaceMock;

    @Mock
    private ServiceDiscoveryInterface serviceDiscoveryInterfaceMock;

    @Before
    public void setUp() {
        Robolectric.buildActivity(DigitalCareTestMock.class).create().get();
        MockitoAnnotations.initMocks(this);

        fragment = new ServiceLocatorFragment();

        PowerMockito.mockStatic(DigitalCareConfigManager.class);
        when(DigitalCareConfigManager.getInstance()).thenReturn(mockDigitalCareConfigManager);
        when(DigitalCareConfigManager.getInstance().getTaggingInterface()).thenReturn(mockAppTaggingInterface);
        when(DigitalCareConfigManager.getInstance().getAPPInfraInstance()).thenReturn(appInfraInterfaceMock);

        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);

        SupportFragmentTestUtil.startFragment(fragment, DigitalCareTestMock.class);
        rootView = fragment.getView();
    }

    @After
    public void tearDown() throws Exception {
        fragment = null;
        mockDigitalCareConfigManager = null;
        mockAppTaggingInterface = null;
        rootView = null;
    }

    @Test
    public void testInitView() throws Exception {
        Assert.assertNotNull(rootView);
    }

    @Test
    public void testSetPreviousPageName() {
        String previousPageName = fragment.setPreviousPageName();
        Assert.assertNotNull(previousPageName);
    }

    @Test
    public void testOnConfigurationChanged() {
        Configuration configuration = new Configuration();
        fragment.onConfigurationChanged(configuration);
    }

    @Test
    public void testDestroyMethod() {
        fragment.onDestroy();
    }

}
