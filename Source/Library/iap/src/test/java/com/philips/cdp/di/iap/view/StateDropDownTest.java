package com.philips.cdp.di.iap.view;

import android.app.Application;
import android.view.View;
import android.widget.AdapterView;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.response.State.Region;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.platform.uid.view.widget.UIPicker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 9/22/17.
 */


@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class StateDropDownTest {

    StateDropDown stateDropDown;
    private Application mContext;

    View viewMock;

    @Mock
    UIPicker uiPickerMock;

    @Mock
    StateDropDown.StateListener stateListenerMock;

    @Before
    public void setUp() {
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        stateDropDown = new StateDropDown(stateListenerMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldshowPopUP() throws Exception {
        Mockito.when(uiPickerMock.isShowing()).thenReturn(false);
        stateDropDown.show();
    }

    @Mock
    RegionsList regionsListMock;

    @Spy
    private List<Region> regions = new ArrayList<>();

    @Test
    public void shouldCreateRowItems() throws Exception {
        Mockito.when(regionsListMock.getRegions()).thenReturn(regions);
        stateDropDown.createRowItems(regionsListMock);

    }

    @Mock
    AdapterView<?> adapterViewMock;

    @Test(expected = NullPointerException.class)
    public void shouldCallOnStateSelect() throws Exception {
        Mockito.when(regionsListMock.getRegions()).thenReturn(regions);
        stateDropDown.callOnStateSelect("abcdefg");

    }
}