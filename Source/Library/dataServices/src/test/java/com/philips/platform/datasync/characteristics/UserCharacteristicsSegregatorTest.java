package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by indrajitkumar on 2/2/17.
 */
public class UserCharacteristicsSegregatorTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    UserCharacteristicsSegregator userCharacteristicsSegregator;
    @Mock
    DBFetchingInterface mockDBDbFetchingInterface;
    @Mock
    DBUpdatingInterface mockDbUpdatingInterface;

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        userCharacteristicsSegregator = new UserCharacteristicsSegregator();
        userCharacteristicsSegregator.dbFetchingInterface = mockDBDbFetchingInterface;
        userCharacteristicsSegregator.dbUpdatingInterface = mockDbUpdatingInterface;
    }


    @Test
    public void shouldReturnDataToSyn_WhenIsUserCharacteristicsSyncedIsCalled() throws Exception {
        userCharacteristicsSegregator.isUCSynced();
        verify(mockDBDbFetchingInterface).isSynced(SyncType.CHARACTERISTICS.getId());
    }
}