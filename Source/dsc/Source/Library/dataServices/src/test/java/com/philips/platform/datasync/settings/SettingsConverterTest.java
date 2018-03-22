package com.philips.platform.datasync.settings;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsConverterTest {
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
       Settings settings= verticalDataCreator.createSettings("en_Us","metric", null);
       UCoreSettings uCoreSettings=settingsConverter.convertAppToUcoreSettings(settings);
        assertThat(uCoreSettings).isNotNull();
    }

    @Before
    public void setUp() {
        verticalDataCreator = new OrmCreatorTest(new UuidGenerator());
        settingsConverter = new SettingsConverter(verticalDataCreator);
    }

    private BaseAppDataCreator verticalDataCreator;
    private SettingsConverter settingsConverter;
}