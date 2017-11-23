package com.philips.platform.datasync.consent;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.testing.verticals.table.OrmConsentDetail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        consentsSegregator = new ConsentsSegregator();
        consentsSegregator.dbFetchingInterface = mockDBDbFetchingInterface;
    }

    @Test
    public void shouldReturnDataToSyn_WhenPutConsentForSyncIsCalled() throws Exception {
        OrmConsentDetail ormConsent = new OrmConsentDetail(null, null, null, null);
        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync.put(ConsentDetail.class, Arrays.asList(ormConsent));
        consentsSegregator.putConsentForSync(dataToSync);
        verify(mockDBDbFetchingInterface).fetchNonSyncConsentDetails();
    }

    @Test
    public void shouldThrowSqlException_WhenFetchNonSyncConsentDetailsfails() throws Exception {

        SQLException sqlException=new SQLException("sql exception");
        OrmConsentDetail ormConsent = new OrmConsentDetail(null, null, null, null);
        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync.put(ConsentDetail.class, Arrays.asList(ormConsent));
        consentsSegregator.putConsentForSync(dataToSync);
        when(mockDBDbFetchingInterface.fetchNonSyncConsentDetails()).thenThrow(sqlException);
    }
}