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

}