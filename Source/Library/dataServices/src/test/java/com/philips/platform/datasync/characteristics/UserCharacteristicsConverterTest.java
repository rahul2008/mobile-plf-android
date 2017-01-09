package com.philips.platform.datasync.characteristics;

import android.provider.ContactsContract;

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
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.converter.GsonConverter;

import static com.philips.platform.datasync.UCoreAdapter.API_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserCharacteristicsConverterTest {

    private final String TEST_CHARACTERISTICS = "TEST_CHARACTERISTICS";
    private UserCharacteristicsConverter userCharacteristicsConvertor;
    private BaseAppDataCreator verticalDataCreater;
    @Mock
    private UCoreCharacteristics uCoreCharacteristicsMock;
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
    public void ShouldReturnEmptyList_WhenUCoreListIsEmpty() throws Exception {
        UCoreUserCharacteristics characteristics = userCharacteristicsConvertor.convertToUCoreUserCharacteristics(new ArrayList<Characteristics>());

        assertThat(characteristics.getCharacteristics()).isNotNull();
    }

//    @Test
//    public void ShouldReturnCharacteristicsDetailList_WhenAppCharacteristicsDetailListIsNotNull() throws Exception {
////        UserCharacteristicsClient userCharacteristicsClient = uCoreAdapterMock.getAppFrameworkClient(UserCharacteristicsClient.class,
////                mUCoreAccessProviderMock.getAccessToken(), gsonConverterMock);
//        //when(userCharacteristicsClientMock.getUserCharacteristics(mUCoreAccessProviderMock.getUserId(),mUCoreAccessProviderMock.getUserId(), API_VERSION)).then()
//        UCoreUserCharacteristics uCoreUserCharacteristics = userCharacteristicsClientMock.getUserCharacteristics(mUCoreAccessProviderMock.getUserId(),
//                mUCoreAccessProviderMock.getUserId(), API_VERSION);
//        Characteristics characteristics = userCharacteristicsConvertor.convertToCharacteristics(uCoreUserCharacteristics);
//
//        assertThat(characteristics).isNotNull();
//    }

    @Test
    public void ShouldReturnUCoreCharacteristicslList_WhenAppCharacteristicsIsPassed() throws Exception {
        Characteristics characteristics = userCharacteristicsConvertor.dataCreator.createCharacteristics("TEST_CREATORID");
        CharacteristicsDetail characteristicsDetail = userCharacteristicsConvertor.dataCreator.createCharacteristicsDetails("TYPE", "VALUE", 0, characteristics);

        userCharacteristicsConvertor.convertToUCoreUserCharacteristics(new ArrayList<Characteristics>());
    }

    @Test
    public void ShouldReturnUCoreCharacteristics_WhenAppCharacteristicsIsPassedWithTypeNull() throws Exception {
        when(uCoreCharacteristicsMock.getType()).thenReturn(null);
        when(uCoreCharacteristicsMock.getValue()).thenReturn("VALUE");
        when(uCoreCharacteristicsMock.getCharacteristics()).thenReturn(new ArrayList<UCoreCharacteristics>());

        UCoreUserCharacteristics uCoreCharacteristicsList = userCharacteristicsConvertor.convertToUCoreUserCharacteristics(null);

        assertThat(uCoreCharacteristicsList).isNotNull();
    }

    @Test
    public void ShouldReturnUCoreCharacteristics_WhenAppCharacteristicsIsPassedWithType() throws Exception {
        Characteristics characteristics = userCharacteristicsConvertor.dataCreator.createCharacteristics("TEST_CREATORID");
        CharacteristicsDetail characteristicsDetail = userCharacteristicsConvertor.dataCreator.createCharacteristicsDetails("TYPE", "VALUE", 0, characteristics);
        CharacteristicsDetail characteristicsDetail1 = userCharacteristicsConvertor.dataCreator.createCharacteristicsDetails("TYPE", "VALUE", 0, characteristics, characteristicsDetail);

        List<Characteristics> characteristicsList = new ArrayList<>();
        characteristicsDetail.setCharacteristicsDetail(characteristicsDetail1);
        characteristics.addCharacteristicsDetail(characteristicsDetail);

        characteristics.addCharacteristicsDetail(characteristicsDetail1);
        characteristicsList.add(characteristics);
        userCharacteristicsConvertor.convertToUCoreUserCharacteristics(characteristicsList);
    }
}