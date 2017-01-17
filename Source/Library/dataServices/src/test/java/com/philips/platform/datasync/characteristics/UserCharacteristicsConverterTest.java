package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserCharacteristicsConverterTest {

    private final String TEST_CHARACTERISTICS = "TEST_CHARACTERISTICS";
    private UserCharacteristicsConverter userCharacteristicsConvertor;

    private BaseAppDataCreator verticalDataCreater;
    @Mock
    private UCoreCharacteristics uCoreCharacteristicsMock;
    @Mock
    private UCoreUserCharacteristics uCoreUserCharacteristicsMock;
    @Mock
    private UuidGenerator generatorMock;

    @Mock
    private AppComponent appComponantMock;
    @Mock
    private UCoreAdapter uCoreAdapterMock;
    @Mock
    private UCoreAccessProvider mUCoreAccessProviderMock;

    @Mock
    UserCharacteristicsClient userCharacteristicsClientMock;

    @Mock
    GsonConverter gsonConverterMock;

    @Before
    public void setUp() {
        initMocks(this);

        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        DataServicesManager.getInstance().mAppComponent = appComponantMock;
        userCharacteristicsConvertor = new UserCharacteristicsConverter();
        userCharacteristicsConvertor.dataCreator = verticalDataCreater;
    }

    @Test
    public void ShouldReturnUCoreCharacteristics_WhenAppCharacteristicsIsPassedWithType() throws Exception {
        Characteristics characteristics = userCharacteristicsConvertor.dataCreator.createCharacteristics("TEST_CREATORID");
        CharacteristicsDetail characteristicsDetail = userCharacteristicsConvertor.dataCreator.createCharacteristicsDetails("TYPE", "VALUE", characteristics);
        CharacteristicsDetail characteristicsDetail1 = userCharacteristicsConvertor.dataCreator.createCharacteristicsDetails("TYPE", "VALUE", characteristics, characteristicsDetail);

        List<Characteristics> characteristicsList = new ArrayList<>();
        characteristicsDetail.setCharacteristicsDetail(characteristicsDetail1);
        characteristics.addCharacteristicsDetail(characteristicsDetail);

        characteristics.addCharacteristicsDetail(characteristicsDetail1);
        characteristicsList.add(characteristics);
        UCoreUserCharacteristics uCoreCharacteristicsList = userCharacteristicsConvertor.convertToUCoreUserCharacteristics(characteristicsList);
        assertThat(uCoreCharacteristicsList).isNotNull();
        assertThat(uCoreCharacteristicsList).isInstanceOf(UCoreUserCharacteristics.class);
    }


    @Test
    public void ShouldReturnUCoreCharacteristics_WhenAppUCoreCharacteristicsIsNotNull() throws Exception {
        List<UCoreCharacteristics> list = new ArrayList<UCoreCharacteristics>();
        List<UCoreCharacteristics> list1 = new ArrayList<UCoreCharacteristics>();
        List<UCoreCharacteristics> list2 = new ArrayList<UCoreCharacteristics>();
        UCoreCharacteristics uCoreCharacteristics = new UCoreCharacteristics();
        uCoreCharacteristics.setType("Type");
        uCoreCharacteristics.setValue("Valuse");
        uCoreCharacteristics.setCharacteristics(list1);

        UCoreCharacteristics uCoreCharacteristics1 = new UCoreCharacteristics();
        uCoreCharacteristics1.setType("Type");
        uCoreCharacteristics1.setValue("Valuse");
        uCoreCharacteristics1.setCharacteristics(list2);

        list.add(uCoreCharacteristics);
        list1.add(uCoreCharacteristics1);
       // list1.add(new UCoreCharacteristics());
        UCoreUserCharacteristics userCharacteristics = new UCoreUserCharacteristics();
        userCharacteristics.setCharacteristics(list);

        Characteristics toCharacteristics = userCharacteristicsConvertor.convertToCharacteristics(userCharacteristics, "TEST_CREATORID");

        assertThat(toCharacteristics.getCharacteristicsDetails()).isNotNull();
        assertThat(toCharacteristics).isNotNull();
        assertThat(toCharacteristics).isInstanceOf(Characteristics.class);
    }
}