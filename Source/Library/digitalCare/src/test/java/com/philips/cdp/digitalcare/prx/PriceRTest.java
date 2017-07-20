package com.philips.cdp.digitalcare.prx;

import com.philips.cdp.digitalcare.BuildConfig;
import com.philips.cdp.digitalcare.prx.subcategorymodel.Price;
import com.philips.cdp.digitalcare.util.CustomRobolectricRunnerCC;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;

/**
 * Created by philips on 7/15/17.
 */

@RunWith(CustomRobolectricRunnerCC.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PriceRTest {


    @Test
    public void testSetter_setProductPrice() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();

        pojo.setProductPrice("66$");

        final Field field = pojo.getClass().getDeclaredField("productPrice");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "66$");
    }

    @Test
    public void testGetter_getProductPrice() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();
        final Field field = pojo.getClass().getDeclaredField("productPrice");
        field.setAccessible(true);
        field.set(pojo, "66$");

        final String result = pojo.getProductPrice();

        assertEquals("field wasn't retrieved properly", result, "66$");
    }


    @Test
    public void testSetter_setDisplayPriceType() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();

        pojo.setDisplayPriceType("66$");

        final Field field = pojo.getClass().getDeclaredField("displayPriceType");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "66$");
    }

    @Test
    public void testGetter_getDisplayPriceType() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();
        final Field field = pojo.getClass().getDeclaredField("displayPriceType");
        field.setAccessible(true);
        field.set(pojo, "66$");

        final String result = pojo.getDisplayPriceType();

        assertEquals("field wasn't retrieved properly", result, "66$");
    }


    @Test
    public void testSetter_setCurrencyCode() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();

        pojo.setCurrencyCode("USD");

        final Field field = pojo.getClass().getDeclaredField("currencyCode");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "USD");
    }

    @Test
    public void testGetter_getCurrencyCode() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();
        final Field field = pojo.getClass().getDeclaredField("currencyCode");
        field.setAccessible(true);
        field.set(pojo, "USD");

        final String result = pojo.getCurrencyCode();

        assertEquals("field wasn't retrieved properly", result, "USD");
    }


    @Test
    public void testSetter_setFormattedPrice() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();

        pojo.setFormattedPrice("66$");

        final Field field = pojo.getClass().getDeclaredField("formattedPrice");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "66$");
    }

    @Test
    public void testGetter_getFormattedPrice() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();
        final Field field = pojo.getClass().getDeclaredField("formattedPrice");
        field.setAccessible(true);
        field.set(pojo, "66$");

        final String result = pojo.getFormattedPrice();

        assertEquals("field wasn't retrieved properly", result, "66$");
    }


    @Test
    public void testSetter_setDisplayPrice() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();

        pojo.setDisplayPrice("66$");

        final Field field = pojo.getClass().getDeclaredField("displayPrice");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "66$");
    }

    @Test
    public void testGetter_getDisplayPrice() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();
        final Field field = pojo.getClass().getDeclaredField("displayPrice");
        field.setAccessible(true);
        field.set(pojo, "66$");

        final String result = pojo.getDisplayPrice();

        assertEquals("field wasn't retrieved properly", result, "66$");
    }


    @Test
    public void testSetter_setFormattedDisplayPrice() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();

        pojo.setFormattedDisplayPrice("66$");

        final Field field = pojo.getClass().getDeclaredField("formattedDisplayPrice");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "66$");
    }

    @Test
    public void testGetter_getFormattedDisplayPrice() throws NoSuchFieldException, IllegalAccessException {
        final Price pojo = new Price();
        final Field field = pojo.getClass().getDeclaredField("formattedDisplayPrice");
        field.setAccessible(true);
        field.set(pojo, "66$");

        final String result = pojo.getFormattedDisplayPrice();

        assertEquals("field wasn't retrieved properly", result, "66$");
    }


}
