package com.philips.cdp.prodreg.launcher;

import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PRInterfaceTest extends TestCase {

    private PRInterface prInterface;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        prInterface = new PRInterface();
    }

    @Test
    public void testInit() throws Exception {
        final PRUiHelper prUiHelperMock = mock(PRUiHelper.class);
        prInterface = new PRInterface() {
            protected PRUiHelper getInstance() {
                return prUiHelperMock;
            }
        };
        UappDependencies uAppDependenciesMock = mock(UappDependencies.class);
        UappSettings uappSettings = mock(UappSettings.class);
//        prInterface.init(uAppDependenciesMock, uappSettings);
//        verify(prUiHelperMock).init(uAppDependenciesMock, uappSettings);
    }

    @Test
    public void IGNOREDtestLaunch() throws Exception {
        final PRUiHelper prUiHelperMock = mock(PRUiHelper.class);
        prInterface = new PRInterface() {
            protected PRUiHelper getInstance() {
                return prUiHelperMock;
            }
        };
        UappLaunchInput uappLaunchInput = mock(UappLaunchInput.class);
        UiLauncher uiLauncher = mock(UiLauncher.class);
        prInterface.launch(uiLauncher, uappLaunchInput);
        verify(prUiHelperMock).launch(uiLauncher, uappLaunchInput);
    }
}