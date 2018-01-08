package com.philips.platform.mya.settings;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SettingsModelTest {

    private SettingsModel settingsModel;

    @Before
    public void setUp(){
        settingsModel = new SettingsModel();
        settingsModel.setFirstItem("FirstItem");
        settingsModel.setSecondItem("SecondItem");
        settingsModel.setThirdItem("ThirdItem");

    }
    @Test
    public void shouldNotNull_ItemCount(){
        assertNotNull(settingsModel.getItemCount());
    }

    @Test
    public void shouldNotNull_FirstItem(){
        assertNotNull(settingsModel.getFirstItem());
    }

    @Test
    public void shouldNotNull_SecondItem(){
        assertNotNull(settingsModel.getSecondItem());
    }

    @Test
    public void shouldNotNull_ThirdItem(){
        assertNotNull(settingsModel.getThirdItem());
    }

    @Test
    public void testSettersAndGetters() {
        settingsModel.setFirstItem("first_item");
        settingsModel.setSecondItem("second_item");
        settingsModel.setThirdItem("third_item");
        settingsModel.setItemCount(10);

        assertEquals(settingsModel.getFirstItem(),"first_item");
        assertEquals(settingsModel.getSecondItem(),"second_item");
        assertEquals(settingsModel.getThirdItem(),"third_item");
        assertEquals(settingsModel.getItemCount(),10);
    }
}