package com.philips.platform.datasync.consent;

import android.content.Context;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConsentsConverterTest {

    private final String TEST_DEVICE_ID = "manual";
    private ConsentsConverter consentsConverter;

    private BaseAppDataCreator verticalDataCreater;

    private final String TEST_STATUS = "TEST_STATUS";

    private final String TEST_VERSION = "TEST_VERSION";

    public static final String TEMPERATURE = "Temperature";

    @Mock
    private ConsentDetail consentDetailMock;

    Context context;

    @Mock
    private UuidGenerator generatorMock;


    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() {
        initMocks(this);

        DataServicesManager dataServicesManager = DataServicesManager.getInstance();
        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        consentsConverter = new ConsentsConverter();
        consentsConverter.dataCreator = verticalDataCreater;
    }

    @Test
    public void ShouldReturnEmptyList_WhenUCoreListIsEmpty() throws Exception {
        List<ConsentDetail> consentDetails = consentsConverter.convertToAppConsentDetails(new ArrayList<UCoreConsentDetail>());

        assertThat(consentDetails).isNotNull();
    }

    @Test
    public void ShouldReturnUCoreConsentDetailList_WhenAppConsentDetailListIsNotNull() throws Exception {
        ArrayList<UCoreConsentDetail> list = new ArrayList<>();
        list.add(0, new UCoreConsentDetail(TEMPERATURE, TEST_STATUS, TEST_VERSION, ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        list.add(1, new UCoreConsentDetail(TEMPERATURE, TEST_STATUS, TEST_VERSION, ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));

        List<ConsentDetail> consentDetails = consentsConverter.convertToAppConsentDetails(list);

        assertThat(consentDetails).isNotNull();
        assertThat(consentDetails).isNotEmpty();
        assertThat(list).isNotEmpty();
    }

    @Test
    public void ShouldReturnUCoreConsentDetailList_WhenAppConsentDetailListIsPassed() throws Exception {
        ConsentDetail consentDetailDetail = verticalDataCreater.createConsentDetail(TEMPERATURE, TEST_STATUS, TEST_VERSION, ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);

        List<UCoreConsentDetail> uCoreConsentDetailList = consentsConverter.convertToUCoreConsentDetails(Collections.singletonList(consentDetailDetail));

        assertThat(uCoreConsentDetailList).isNotNull();
        assertThat(uCoreConsentDetailList).hasSize(1);

        UCoreConsentDetail uCoreConsentDetail = uCoreConsentDetailList.get(0);

        assertThat(uCoreConsentDetail.getStatus()).isEqualTo(TEST_STATUS);
        assertThat(uCoreConsentDetail.getDocumentVersion()).isEqualTo(TEST_VERSION);
        assertThat(uCoreConsentDetail.getName()).isEqualTo(TEMPERATURE);
    }

    @Test
    public void ShouldReturnUCoreConsentDetailList_WhenAppConsentDetailListIsPassedWithTypeNull() throws Exception {
        when(consentDetailMock.getType()).thenReturn(null);
        when(consentDetailMock.getId()).thenReturn(1);
        when(consentDetailMock.getStatus()).thenReturn(TEST_STATUS);
        when(consentDetailMock.getVersion()).thenReturn(TEST_VERSION);

        List<UCoreConsentDetail> uCoreConsentDetailList = consentsConverter.convertToUCoreConsentDetails(Collections.singletonList(consentDetailMock));

        assertThat(uCoreConsentDetailList).isEmpty();
    }

    @Test
    public void ShouldReturnUCoreConsentDetailList_WhenAppConsentDetailListIsPassedWithTypeNotNull() throws Exception {
        when(consentDetailMock.getType()).thenReturn(TEMPERATURE);
        when(consentDetailMock.getId()).thenReturn(1);
        when(consentDetailMock.getStatus()).thenReturn(TEST_STATUS);
        when(consentDetailMock.getVersion()).thenReturn(TEST_VERSION);

        List<UCoreConsentDetail> uCoreConsentDetailList = consentsConverter.convertToUCoreConsentDetails(Collections.singletonList(consentDetailMock));

        assertThat(uCoreConsentDetailList).isNotEmpty();
    }

}