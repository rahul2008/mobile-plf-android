package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.consent.ConsentsConverter;
import com.philips.platform.datasync.consent.UCoreConsentDetail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by indrajitkumar on 1/3/17.
 */
public class UserCharacteristicsConvertorTest {


    private final String TEST_CHARACTERISTICS = "TEST_CHARACTERISTICS";
    private UserCharacteristicsConvertor userCharacteristicsConvertor;

    private BaseAppDataCreator uDataCreator;
    @Mock
    private UCoreCharacteristics uCoreCharacteristicsMock;

    @Mock
    DataServicesManager dataServicesManager;
    @Before
    public void setUp() {
        initMocks(this);

        uDataCreator = dataServicesManager.getDataCreater();

        userCharacteristicsConvertor = new UserCharacteristicsConvertor();
    }


    @Test
    public void ShouldReturnUCoreCharacteristicslList_WhenAppCharacteristicsIsPassed() throws Exception {
        Characteristics characteristics = uDataCreator.createCharacteristics("TEST_CREATORID");
        CharacteristicsDetail characteristicsDetail = uDataCreator.createCharacteristicsDetails("TYPE", "VALUE", 0, characteristics);

        List<UCoreCharacteristics> uCoreCharacteristicsList = (List<UCoreCharacteristics>) userCharacteristicsConvertor.convertToUCoreCharacteristics(Collections.singletonList(characteristicsDetail));

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

        List<UCoreCharacteristics> uCoreCharacteristicsList = (List<UCoreCharacteristics>) userCharacteristicsConvertor.convertToUCoreCharacteristics(new ArrayList<CharacteristicsDetail>());

        assertThat(uCoreCharacteristicsList).isNull();
    }
}