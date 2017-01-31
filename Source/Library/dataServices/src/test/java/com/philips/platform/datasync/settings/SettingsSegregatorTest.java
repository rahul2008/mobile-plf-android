package com.philips.platform.datasync.settings;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.consent.ConsentsSegregator;
import com.philips.testing.verticals.table.OrmConsent;
import com.philips.testing.verticals.table.OrmSettings;
import com.squareup.okhttp.internal.framed.Settings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import com.philips.testing.verticals.table.OrmConsent;

/**
 * Created by sangamesh on 31/01/17.
 */
public class SettingsSegregatorTest {

    SettingsSegregator settingsSegregator;

    @Mock
    DBFetchingInterface mockDBDbFetchingInterface;

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        settingsSegregator = new SettingsSegregator();
        settingsSegregator.dbFetchingInterface=mockDBDbFetchingInterface;
    }

    @Test
    public void shouldReturnDataToSyn_WhenPutSettingsForSyncIsCalled() throws Exception {
        OrmSettings ormSettings=new OrmSettings("Metric","en_US");
        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync.put(Settings.class, Arrays.asList(ormSettings));
        settingsSegregator.putSettingsForSync(dataToSync);
        verify(mockDBDbFetchingInterface).fetchNonSyncSettings();
    }

}