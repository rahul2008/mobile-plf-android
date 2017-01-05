package com.philips.platform.datasync.consent;

import android.content.Context;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
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

    private DataServicesManager dataServicesManager;

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() {
        initMocks(this);

        context = RuntimeEnvironment.application;

        dataServicesManager = DataServicesManager.getInstance();
        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        dataServicesManager.mAppComponent = appComponantMock;
        consentsConverter = new ConsentsConverter();
        consentsConverter.dataCreator = verticalDataCreater;
    }

    @Test
    public void ShouldReturnEmptyList_WhenUCoreListIsEmpty() throws Exception {
        Consent consent = consentsConverter.convertToAppConsentDetails(new ArrayList<UCoreConsentDetail>(), "TEST_CREATOR_ID");

        assertThat(consent.getConsentDetails()).isNotNull();
    }

    @Test
    public void ShouldReturnUCoreConsentDetailList_WhenAppConsentDetailListIsNotNull() throws Exception {
        ArrayList<UCoreConsentDetail> list = new ArrayList<>();
        list.add(0, new UCoreConsentDetail(TEMPERATURE, TEST_STATUS, TEST_VERSION, Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        list.add(1, new UCoreConsentDetail(TEMPERATURE, TEST_STATUS, TEST_VERSION, Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));

        Consent consent = consentsConverter.convertToAppConsentDetails(list, "TEST_CREATOR_ID");

        assertThat(consent.getConsentDetails()).isNotNull();
        assertThat(list).isNotEmpty();
    }

    @Test
    public void ShouldReturnUCoreConsentDetailList_WhenAppConsentDetailListIsPassed() throws Exception {
        Consent consent = verticalDataCreater.createConsent("TEST_CREATORID");
        ConsentDetail consentDetail = verticalDataCreater.createConsentDetail(TEMPERATURE, TEST_STATUS, TEST_VERSION, Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER, false, consent);

        List<UCoreConsentDetail> uCoreConsentDetailList = consentsConverter.convertToUCoreConsentDetails(Collections.singletonList(consentDetail));

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