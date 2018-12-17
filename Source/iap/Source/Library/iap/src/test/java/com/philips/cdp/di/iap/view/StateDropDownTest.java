/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.view;

import com.philips.cdp.di.iap.response.State.Region;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.platform.uid.view.widget.UIPicker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 9/22/17.
 */
@RunWith(RobolectricTestRunner.class)
public class StateDropDownTest {


    @Mock
    private UIPicker uiPickerMock;

    @Mock
    private StateDropDown.StateListener stateListenerMock;

    @Mock
    private RegionsList regionsListMock;

    @Spy
    private List<Region> regions = new ArrayList<>();

    private StateDropDown stateDropDown;

    @Before
    public void setUp() {
        initMocks(this);
        stateDropDown = new StateDropDown(stateListenerMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldshowPopUP() throws Exception {
        Mockito.when(uiPickerMock.isShowing()).thenReturn(false);
        stateDropDown.show();
    }

    @Test
    public void shouldCreateRowItems() throws Exception {
        Mockito.when(regionsListMock.getRegions()).thenReturn(regions);
        stateDropDown.createRowItems(regionsListMock);

    }

    @Test(expected = NullPointerException.class)
    public void shouldCallOnStateSelect() throws Exception {
        Mockito.when(regionsListMock.getRegions()).thenReturn(regions);
        stateDropDown.callOnStateSelect("abcdefg");

    }
}