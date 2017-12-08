package com.philips.platform.mya.settings;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/23/17.
 */
public class SettingsModelTest {

    SettingsModel settingsModel;
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
}