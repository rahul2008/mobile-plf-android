package com.philips.cdp.digitalcare;

import com.philips.cdp.digitalcare.util.CustomRobolectricRunnerCC;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;

/**
 * Created by philips on 7/6/17.
 */

@RunWith(CustomRobolectricRunnerCC.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ConsumerProductInfoRTest {

    @Test
    public void testSetter_setCtn() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();

        pojo.setCtn("HR1922_20");

        final Field field = pojo.getClass().getDeclaredField("mCtn");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "HR1922_20");
    }

    @Test
    public void testGetter_getCtn() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();
        final Field field = pojo.getClass().getDeclaredField("mCtn");
        field.setAccessible(true);
        field.set(pojo, "HR1922_20");

        final String result = pojo.getCtn();

        assertEquals("field wasn't retrieved properly", result, "HR1922_20");
    }

    @Test
    public void testSetter_setSubCategory() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();

        pojo.setSubCategory("JUICE_EXTRACTORS_SU2");

        final Field field = pojo.getClass().getDeclaredField("mSubCategory");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "JUICE_EXTRACTORS_SU2");
    }

    @Test
    public void testGetter_getSubCategory() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();
        final Field field = pojo.getClass().getDeclaredField("mSubCategory");
        field.setAccessible(true);
        field.set(pojo, "JUICE_EXTRACTORS_SU2");

        final String result = pojo.getSubCategory();

        assertEquals("field wasn't retrieved properly", result, "JUICE_EXTRACTORS_SU2");
    }

    @Test
    public void testSetter_setGroup() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();

        pojo.setGroup("HOUSEHOLD_PRODUCTS_GR");

        final Field field = pojo.getClass().getDeclaredField("mGroup");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "HOUSEHOLD_PRODUCTS_GR");
    }

    @Test
    public void testGetter_getGroup() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();
        final Field field = pojo.getClass().getDeclaredField("mGroup");
        field.setAccessible(true);
        field.set(pojo, "HOUSEHOLD_PRODUCTS_GR");

        final String result = pojo.getGroup();

        assertEquals("field wasn't retrieved properly", result, "HOUSEHOLD_PRODUCTS_GR");
    }

    @Test
    public void testSetter_setSector() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();

        pojo.setSector("B2C");

        final Field field = pojo.getClass().getDeclaredField("mSector");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "B2C");
    }

    @Test
    public void testGetter_getSector() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();
        final Field field = pojo.getClass().getDeclaredField("mSector");
        field.setAccessible(true);
        field.set(pojo, "B2C");

        final String result = pojo.getSector();

        assertEquals("field wasn't retrieved properly", result, "B2C");
    }

    @Test
    public void testSetter_setCatalog() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();

        pojo.setCatalog("CARE");

        final Field field = pojo.getClass().getDeclaredField("mCatalog");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "CARE");
    }

    @Test
    public void testGetter_getCatalog() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();
        final Field field = pojo.getClass().getDeclaredField("mCatalog");
        field.setAccessible(true);
        field.set(pojo, "CARE");

        final String result = pojo.getCatalog();

        assertEquals("field wasn't retrieved properly", result, "CARE");
    }

    @Test
    public void testSetter_setCategory() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();

        pojo.setCategory("FOOD_PREPARATION_CA2");

        final Field field = pojo.getClass().getDeclaredField("mCategory");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "FOOD_PREPARATION_CA2");
    }

    @Test
    public void testGetter_getCategory() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();
        final Field field = pojo.getClass().getDeclaredField("mCategory");
        field.setAccessible(true);
        field.set(pojo, "FOOD_PREPARATION_CA2");

        final String result = pojo.getCategory();

        assertEquals("field wasn't retrieved properly", result, "FOOD_PREPARATION_CA2");
    }

    @Test
    public void testSetter_setProductReviewUrl() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();

        pojo.setProductReviewUrl("Avance Collection Juicer");

        final Field field = pojo.getClass().getDeclaredField("mProductTitle");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "Avance Collection Juicer");
    }

    @Test
    public void testGetter_getProductTitle() throws NoSuchFieldException, IllegalAccessException {
        final ConsumerProductInfo pojo = new ConsumerProductInfo();
        final Field field = pojo.getClass().getDeclaredField("mProductTitle");
        field.setAccessible(true);
        field.set(pojo, "Avance Collection Juicer");

        final String result = pojo.getProductTitle();

        assertEquals("field wasn't retrieved properly", result, "Avance Collection Juicer");
    }

}
