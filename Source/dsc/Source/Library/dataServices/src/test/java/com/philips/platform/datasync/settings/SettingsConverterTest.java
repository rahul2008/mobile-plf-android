package com.philips.platform.datasync.settings;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 01/02/17.
 */
public class SettingsConverterTest {


    private BaseAppDataCreator verticalDataCreater;

    @Mock
    private AppComponent appComponantMock;

    SettingsConverter settingsConverter;

    @Before
    public void setUp() {
        initMocks(this);

        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        settingsConverter = new SettingsConverter();
        settingsConverter.dataCreator = verticalDataCreater;
    }

    @Test
    public void shouldReturnSettings_whenConvertUcoreToAppSettingsIsCalled() throws Exception {

        UCoreSettings uCoreSettings=new UCoreSettings();
        uCoreSettings.setLocale("en_US");
        uCoreSettings.setUnitSystem("metric");
        Settings settings=settingsConverter.convertUcoreToAppSettings(uCoreSettings);
        assertThat(settings).isNotNull();
    }

    @Test
    public void shouldReturnUcoreSettings_whenConvertAppToUcoreSettingsIsCalled() throws Exception {

       Settings settings=verticalDataCreater.createSettings("en_Us","metric", timeZone);
       UCoreSettings uCoreSettings=settingsConverter.convertAppToUcoreSettings(settings);
        assertThat(uCoreSettings).isNotNull();
    }
}