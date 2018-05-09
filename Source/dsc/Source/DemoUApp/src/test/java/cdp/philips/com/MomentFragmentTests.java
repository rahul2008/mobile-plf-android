package cdp.philips.com;

import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.moments.MomentFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MomentFragmentTests {

    private MomentFragment sut;

    @Mock
    private DataServicesManager dataServicesManagerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        sut = new MomentFragmentTestable(dataServicesManagerMock);
    }

    @Test
    public void registerForDBChanges() {
        sut.onResume();
        verify(dataServicesManagerMock).registerDBChangeListener(sut);
    }

    @Test
    public void synchronize() {
        sut.onResume();
        verify(dataServicesManagerMock).synchronize();
    }

    @Test
    public void unregisterForDBChanges() {
        sut.onPause();
        verify(dataServicesManagerMock).unRegisterDBChangeListener();
    }

    private class MomentFragmentTestable extends MomentFragment {

        public MomentFragmentTestable(DataServicesManager dataServicesManager) {
            this.mDataServicesManager = dataServicesManager;
        }
    }
}


