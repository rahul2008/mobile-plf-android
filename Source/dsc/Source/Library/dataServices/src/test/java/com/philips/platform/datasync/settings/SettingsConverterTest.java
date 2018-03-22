/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync.settings;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.testing.verticals.OrmCreatorTest;
import com.philips.testing.verticals.table.OrmSettings;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SettingsConverterTest {
    @Test
    public void convertToAppSettingsConvertsTimeZone() {
        givenDtoSettingsTimeZone("Europe/Paris");
        whenConvertingToAppSettings();
        thenAppSettingsTimeZoneIs("Europe/Paris");
    }

    @Test
    public void convertToAppSettingsConvertsLocale() {
        givenDtoSettingsLocale("nl_NL");
        whenConvertingToAppSettings();
        thenAppSettingsLocaleIs("nl_NL");
    }

    @Test
    public void convertToAppSettingsConvertsUnitSystem() {
        givenDtoSettingsUnitSystem("imperial");
        whenConvertingToAppSettings();
        thenAppSettingsUnitSystemIs("imperial");
    }

    @Test
    public void convertAppSettingsConvertsUnitSystem() {
        givenAppSettingsUnitSystem("us-customary");
        whenConvertingToDtoSettings();
        thenDtoSettingsUnitSystemIs("us-customary");
    }

    @Test
    public void convertAppSettingsConvertsLocale() {
        givenAppSettingsLocale("de_DE");
        whenConvertingToDtoSettings();
        thenDtoSettingsLocaleIs("de_DE");
    }

    private void whenConvertingToDtoSettings() {
        dtoSettings = settingsConverter.convertAppToUcoreSettings(appSettings);
    }

    private void givenDtoSettingsTimeZone(final String timeZone) {
        dtoSettings.setTimeZone(timeZone);
    }

    private void givenDtoSettingsLocale(final String locale) {
        dtoSettings.setLocale(locale);
    }

    private void givenDtoSettingsUnitSystem(final String unitSystem) {
        dtoSettings.setUnitSystem(unitSystem);
    }

    private void givenAppSettingsUnitSystem(final String unitSystem) {
        appSettings.setUnit(unitSystem);
    }

    private void givenAppSettingsLocale(final String locale) {
        appSettings.setLocale(locale);
    }

    private void whenConvertingToAppSettings() {
        appSettings = settingsConverter.convertUcoreToAppSettings(dtoSettings);
    }

    private void thenAppSettingsTimeZoneIs(final String expectedTimeZone) {
        assertEquals(expectedTimeZone, appSettings.getTimeZone());
    }

    private void thenAppSettingsLocaleIs(final String expectedLocale) {
        assertEquals(expectedLocale, appSettings.getLocale());
    }

    private void thenAppSettingsUnitSystemIs(final String expectedUnitSystem) {
        assertEquals(expectedUnitSystem, appSettings.getUnit());
    }

    private void thenDtoSettingsUnitSystemIs(final String expectedUnitSystem) {
        assertEquals(expectedUnitSystem, dtoSettings.getUnitSystem());
    }

    private void thenDtoSettingsLocaleIs(final String expectedLocale) {
        assertEquals(expectedLocale, dtoSettings.getLocale());
    }

    @Before
    public void setUp() {
        verticalDataCreator = new OrmCreatorTest(new UuidGenerator());
        settingsConverter = new SettingsConverter(verticalDataCreator);
        dtoSettings = new UCoreSettings();
        appSettings = new OrmSettings();
    }

    private UCoreSettings dtoSettings;
    private Settings appSettings;

    private BaseAppDataCreator verticalDataCreator;
    private SettingsConverter settingsConverter;
}