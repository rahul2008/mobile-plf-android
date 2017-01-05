package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.consent.ConsentsConverter;
import com.philips.platform.datasync.consent.UCoreConsentDetail;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by indrajitkumar on 1/3/17.
 */
public class UserCharacteristicsConvertorTest {


    private final String TEST_CHARACTERISTICS = "TEST_CHARACTERISTICS";
    private UserCharacteristicsConverter userCharacteristicsConvertor;
    private BaseAppDataCreator verticalDataCreater;
    @Mock
    private UCoreCharacteristics uCoreCharacteristicsMock;
    @Mock
    private UuidGenerator generatorMock;

    private DataServicesManager dataServicesManager;

    @Mock
    private AppComponent appComponantMock;
    @Before
    public void setUp() {
        initMocks(this);

        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        userCharacteristicsConvertor = new UserCharacteristicsConverter();
        dataServicesManager.mAppComponent = appComponantMock;
        userCharacteristicsConvertor.dataCreator = verticalDataCreater;
    }

    @Test
    public void ShouldReturnEmptyList_WhenUCoreListIsEmpty() throws Exception {
        UCoreUserCharacteristics characteristics = userCharacteristicsConvertor.convertToUCoreUserCharacteristics(new ArrayList<Characteristics>());

        assertThat(characteristics.getCharacteristics()).isNotNull();
    }

//    @Test
//    public void ShouldReturnUCoreConsentDetailList_WhenAppConsentDetailListIsNotNull() throws Exception {
//        ArrayList<UCoreConsentDetail> list = new ArrayList<>();
//        list.add(0, new UCoreConsentDetail(TEMPERATURE, TEST_STATUS, TEST_VERSION, Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
//        list.add(1, new UCoreConsentDetail(TEMPERATURE, TEST_STATUS, TEST_VERSION, Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
//
//        Consent consent = consentsConverter.convertToAppConsentDetails(list, "TEST_CREATOR_ID");
//
//        assertThat(consent.getConsentDetails()).isNotNull();
//        assertThat(list).isNotEmpty();
//    }

    @Test
    public void ShouldReturnUCoreCharacteristicslList_WhenAppCharacteristicsIsPassed() throws Exception {
        Characteristics characteristics =  userCharacteristicsConvertor.dataCreator.createCharacteristics("TEST_CREATORID");
        CharacteristicsDetail characteristicsDetail =  userCharacteristicsConvertor.dataCreator.createCharacteristicsDetails("TYPE", "VALUE", 0, characteristics);

        List<UCoreCharacteristics> uCoreCharacteristicsList = (List<UCoreCharacteristics>) userCharacteristicsConvertor.convertToUCoreUserCharacteristics(Collections.singletonList(characteristics));

        assertThat(uCoreCharacteristicsList).isNotNull();
        assertThat(uCoreCharacteristicsList).hasSize(1);

        UCoreCharacteristics uCoreCharacteristics = uCoreCharacteristicsList.get(0);

        assertThat(uCoreCharacteristics.getType()).isEqualTo("TYPE");
        assertThat(uCoreCharacteristics.getValue()).isEqualTo("VALUE");
        assertThat(uCoreCharacteristics.getCharacteristics()).isEqualTo(Collections.singletonList(UCoreCharacteristics.class));
    }

    @Test
    public void ShouldReturnUCoreCharacteristics_WhenAppCharacteristicsIsPassedWithTypeNull() throws Exception {
        when(uCoreCharacteristicsMock.getType()).thenReturn(null);
        when(uCoreCharacteristicsMock.getValue()).thenReturn("VALUE");
        when(uCoreCharacteristicsMock.getCharacteristics()).thenReturn(new ArrayList<UCoreCharacteristics>());

        List<UCoreCharacteristics> uCoreCharacteristicsList = (List<UCoreCharacteristics>) userCharacteristicsConvertor.convertToUCoreUserCharacteristics(new ArrayList<Characteristics>());

        assertThat(uCoreCharacteristicsList).isNull();
    }
}