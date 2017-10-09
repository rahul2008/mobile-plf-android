package com.philips.cdp2.ews.hotspotconnection;

import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

public class ConnectingPhoneToHotspotWifiViewModelTest {

    @InjectMocks private ConnectingPhoneToHotspotWifiViewModel subject;

    @Mock private WiFiConnectivityManager wiFiConnectivityManager;
    @Mock private ApplianceAccessManager applianceAccessManager;
    @Mock private WiFiUtil wiFiUtil;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }
}