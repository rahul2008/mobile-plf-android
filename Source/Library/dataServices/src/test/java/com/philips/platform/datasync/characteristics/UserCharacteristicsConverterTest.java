package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserCharacteristicsConverterTest {

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
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        userCharacteristicsConvertor = new UserCharacteristicsConverter();
        userCharacteristicsConvertor.dataCreator = verticalDataCreater;
    }

    @Test
    public void ShouldReturnUCoreCharacteristics_WhenAppCharacteristicsIsPassedWithType() throws Exception {
        Characteristics userCharacteristics = userCharacteristicsConvertor.dataCreator.createCharacteristics("TYPE", "VALUE");
        Characteristics characteristics = userCharacteristicsConvertor.dataCreator.createCharacteristics("TYPE", "VALUE", userCharacteristics);
        Characteristics characteristicsDetail1 = userCharacteristicsConvertor.dataCreator.createCharacteristics("TYPE", "VALUE", characteristics);

        List<Characteristics> userCharacteristicsList = new ArrayList<>();
        //characteristics.setCharacteristicsDetail(characteristicsDetail1);
        //userCharacteristics.setCharacteristicsDetail(characteristics);
        userCharacteristics.setCharacteristicsDetail(characteristics);
        characteristics.setCharacteristicsDetail(characteristicsDetail1);
        userCharacteristicsList.add(characteristics);
        userCharacteristicsList.add(userCharacteristics);

        UCoreUserCharacteristics uCoreCharacteristicsList = userCharacteristicsConvertor.convertToUCoreUserCharacteristics(userCharacteristicsList);
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

        List<Characteristics> toUserCharacteristics = userCharacteristicsConvertor.convertToCharacteristics(userCharacteristics, "TEST_CREATORID");
        //assertThat(toUserCharacteristics.getCharacteristicsDetails()).isNotNull();
        assertThat(toUserCharacteristics).isNotNull();
        assertThat(toUserCharacteristics.get(0)).isInstanceOf(Characteristics.class);
    }
}