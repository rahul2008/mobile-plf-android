package com.philips.platform.datasync.consent;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.moments.MomentsSegregator;
import com.philips.testing.verticals.datatyes.MomentType;
import com.philips.testing.verticals.table.OrmConsent;
import com.philips.testing.verticals.table.OrmMoment;
import com.philips.testing.verticals.table.OrmMomentType;

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

/**
 * Created by sangamesh on 12/01/17.
 */
public class ConsentsSegregatorTest {

    ConsentsSegregator consentsSegregator;

    @Mock
    DBFetchingInterface mockDBDbFetchingInterface;

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().mAppComponent = appComponantMock;
        consentsSegregator = new ConsentsSegregator();
        consentsSegregator.dbFetchingInterface=mockDBDbFetchingInterface;
    }

    @Test
    public void shouldReturnDataToSyn_WhenPutConsentForSyncIsCalled() throws Exception {
        OrmConsent ormConsent=new OrmConsent(null);
        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync.put(Consent.class, Arrays.asList(ormConsent));
        consentsSegregator.putConsentForSync(dataToSync);
        verify(mockDBDbFetchingInterface).fetchNonSyncConsents();
    }
}