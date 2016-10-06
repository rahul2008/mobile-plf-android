package com.philips.platform.appframework.homescreen;

import com.philips.platform.modularui.statecontroller.FragmentView;
import com.philips.platform.modularui.statecontroller.UIState;

import junit.framework.TestCase;

import org.mockito.Mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HomeActivityPresenterTest extends TestCase {

    @Mock
    FragmentView fragmentViewMock;
    private HomeActivityPresenter homeActivityPresenter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        homeActivityPresenter = new HomeActivityPresenter(fragmentViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(UIState.UI_HOME_STATE);
            }
        };
    }
}