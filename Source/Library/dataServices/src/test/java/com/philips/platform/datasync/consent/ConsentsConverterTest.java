package com.philips.platform.datasync.consent;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sangamesh on 02/12/16.
 */
@Ignore
public class ConsentsConverterTest {

    private final String TEST_DEVICE_ID = "manual";
    private ConsentsConverter consentsConverter;

    private BaseAppDataCreator uGrowDataCreator;

    private final String TEST_STATUS = "TEST_STATUS";

    private final String TEST_VERSION = "TEST_VERSION";

    public static final String TEMPERATURE="Temperature";

    @Mock
    private ConsentDetail consentDetailMock;

    @Mock
    private UuidGenerator generatorMock;

    @Mock
    DataServicesManager dataServicesManager;

    @Before
    public void setUp() {
        initMocks(this);

        uGrowDataCreator = dataServicesManager.getDataCreater();

        consentsConverter = new ConsentsConverter();
    }

    @Test
    public void ShouldReturnUCoreConsentDetailList_WhenAppConsentDetailListIsPassed() throws Exception {
        Consent consent = uGrowDataCreator.createConsent("TEST_CREATORID");
        ConsentDetail consentDetail = uGrowDataCreator.createConsentDetail(TEMPERATURE, TEST_STATUS, TEST_VERSION, Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER,false, consent);

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

   /* @Test
    public void ShouldReturnEmptyConsentDetailList_WhenEmptyIsPassed() throws Exception {

        List<UCoreConsentDetail> uCoreConsentDetailList = consentsConverter.convertToUCoreConsentDetails(new ArrayList<ConsentDetail>());

        assertThat(uCoreConsentDetailList).isEmpty();
    }

    @Test
    public void ShouldReturnAppConsentDetailList_WhenUCoreDetailListIsPassed() throws Exception {
        UCoreConsentDetail uCoreConsentDetail = new UCoreConsentDetail(ConsentDetailType.BOTTLE_FEEDING_SESSION.getDescription(), TEST_STATUS, TEST_VERSION, TEST_DEVICE_ID);
        UCoreConsentDetail uCoreConsentDetail1 = new UCoreConsentDetail(ConsentDetailType.ROOM_TEMPERATURE.getDescription(), TEST_STATUS, TEST_VERSION, Consent.SMART_BABY_MONITOR);

        List<UCoreConsentDetail> uCoreConsentDetailList = new ArrayList<>();
        uCoreConsentDetailList.add(uCoreConsentDetail);
        uCoreConsentDetailList.add(uCoreConsentDetail1);

        Consent consent = consentsConverter.convertToAppConsentDetails(uCoreConsentDetailList, "TEST_CREATOR_ID");

        Collection<? extends ConsentDetail> consentDetailList = consent.getConsentDetails();

        assertThat(consentDetailList).isNotNull();
        assertThat(consentDetailList).hasSize(2);

        Iterator<? extends ConsentDetail> iterator = consentDetailList.iterator();
        ConsentDetail bottleFeed = iterator.next();

        assertThat(bottleFeed.getStatus()).isEqualTo(TEST_STATUS);
        assertThat(bottleFeed.getType().name()).isEqualTo(ConsentDetailType.BOTTLE_FEEDING_SESSION.name());
        assertThat(bottleFeed.getVersion()).isEqualTo(TEST_VERSION);
        assertThat(bottleFeed.getDeviceIdentificationNumber()).isEqualTo(Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);

        ConsentDetail roomTemperature = iterator.next();

        assertThat(roomTemperature.getStatus()).isEqualTo(TEST_STATUS);
        assertThat(roomTemperature.getType().name()).isEqualTo(ConsentDetailType.ROOM_TEMPERATURE.name());
        assertThat(roomTemperature.getVersion()).isEqualTo(TEST_VERSION);
        assertThat(roomTemperature.getDeviceIdentificationNumber()).isEqualTo(Consent.SMART_BABY_MONITOR);
    }

    @Test
    public void ShouldReturnEmptyList_WhenUCoreListIsEmpty() throws Exception {
        Consent consent = consentsConverter.convertToAppConsentDetails(new ArrayList<UCoreConsentDetail>(), "TEST_CREATOR_ID");

        assertThat(consent.getConsentDetails()).isEmpty();
    }

    @Test
    public void ShouldGetDocumentVersionList_WhenAsked() throws Exception {
        List<String> documentVersionList = consentsConverter.getDocumentVersionList();

        assertThat(documentVersionList).hasSize(ConsentDetailType.values().length);
        for (String documentVersion : documentVersionList) {
            assertThat(documentVersion).isEqualTo(Consent.DEFAULT_DOCUMENT_VERSION);
        }
    }

    @Test
    public void ShouldGetDeviceIdentificationNumberList_WhenAsked() throws Exception {
        List<String> deviceIdentificationNumberList = consentsConverter.getDeviceIdentificationNumberList();

        ConsentDetailType[] values = ConsentDetailType.values();
        assertThat(deviceIdentificationNumberList).hasSize(values.length);
        for (int index = 0; index < values.length; index++) {
            if (ConsentDetailType.ROOM_TEMPERATURE == values[index] || ConsentDetailType.RELATIVE_HUMIDITY == values[index]) {
                assertThat(deviceIdentificationNumberList.get(index)).isEqualTo(Consent.SMART_BABY_MONITOR);
            } else {
                assertThat(deviceIdentificationNumberList.get(index)).isEqualTo(Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);
            }
        }
    }*/

}