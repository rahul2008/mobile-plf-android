package com.philips.platform.catk.network;

import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.ConsentAccessToolKitManipulator;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.listener.RequestListener;
import com.philips.platform.catk.mock.ModelDataLoadListenerMock;
import com.philips.platform.catk.mock.NetworkControllerMock;
import com.philips.platform.catk.model.GetConsentsModelRequest;
import com.philips.platform.catk.util.CustomRobolectricRunnerCATK;
import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(CustomRobolectricRunnerCATK.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class NetworkHelperTest {

    @Mock
    CatkComponent catkComponent;

    private NetworkControllerMock networkController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConsentAccessToolKitManipulator.setCatkComponent(catkComponent);
        networkHelper = NetworkHelper.getInstance();
        networkController = new NetworkControllerMock();
        NetworkHelperManipulator.setNetworkController(networkController);
        getConsentsModelRequest = new GetConsentsModelRequest(URL, "applicationName1", "propositionName1", new ModelDataLoadListenerMock());
    }

    @Test
    public void sendRequest() {
        givenModelRequest(getConsentsModelRequest);
        whenCallingSendRequest();
        thenNetworkControllerWasCallWith(givenModelRequest);
    }

    private void thenNetworkControllerWasCallWith(NetworkAbstractModel expectedModelRequest) {
        assertEquals(expectedModelRequest, networkController.sendConsentRequest_model);
    }

    private void givenModelRequest(NetworkAbstractModel modelRequest) {
        givenModelRequest = modelRequest;
    }

    private void whenCallingSendRequest() {
        networkHelper.sendRequest(givenModelRequest);
    }

    NetworkHelper networkHelper;
    NetworkAbstractModel givenModelRequest;
    GetConsentsModelRequest getConsentsModelRequest;
    private final String URL = "https://hdc-css-mst.cloud.pcftest.com/consent";
}