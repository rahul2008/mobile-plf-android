package com.philips.cdp.digitalcare.prx;

import com.philips.cdp.digitalcare.BuildConfig;
import com.philips.cdp.digitalcare.prx.subcategorymodel.Data;
import com.philips.cdp.digitalcare.util.CustomRobolectricRunnerCC;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by philips on 7/15/17.
 */


@RunWith(CustomRobolectricRunnerCC.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DataRTest {

    @Test
    public void testSetter_setCode() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setCode("SERIES_SHAVERS_SU");

        final Field field = pojo.getClass().getDeclaredField("code");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "SERIES_SHAVERS_SU");
    }

    @Test
    public void testGetter_getCode() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("code");
        field.setAccessible(true);
        field.set(pojo, "SERIES_SHAVERS_SU");

        final String result = pojo.getCode();

        assertEquals("field wasn't retrieved properly", result, "SERIES_SHAVERS_SU");
    }


    @Test
    public void testSetter_setName() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setName("Series shavers");

        final Field field = pojo.getClass().getDeclaredField("name");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "Series shavers");
    }

    @Test
    public void testGetter_getName() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("name");
        field.setAccessible(true);
        field.set(pojo, "Series shavers");

        final String result = pojo.getName();

        assertEquals("field wasn't retrieved properly", result, "Series shavers");
    }

    @Test
    public void testSetter_setDomain() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setDomain("http://www.philips.co.uk");

        final Field field = pojo.getClass().getDeclaredField("domain");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "http://www.philips.co.uk");
    }

    @Test
    public void testGetter_getDomain() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("domain");
        field.setAccessible(true);
        field.set(pojo, "http://www.philips.co.uk");

        final String result = pojo.getDomain();

        assertEquals("field wasn't retrieved properly", result, "http://www.philips.co.uk");
    }
    @Test
    public void testSetter_setSeoUrl() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setSeoURL("/c-s-pe/face-shavers/series-shavers");

        final Field field = pojo.getClass().getDeclaredField("seoURL");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "/c-s-pe/face-shavers/series-shavers");
    }

    @Test
    public void testGetter_getSeoUrl() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("seoURL");
        field.setAccessible(true);
        field.set(pojo, "/c-s-pe/face-shavers/series-shavers");

        final String result = pojo.getSeoURL();

        assertEquals("field wasn't retrieved properly", result, "/c-s-pe/face-shavers/series-shavers");
    }
    @Test
    public void testSetter_setPagePath() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setPagePath("/content/B2C/en_IN/support-catalog/pe/face-shavers/series-shavers");

        final Field field = pojo.getClass().getDeclaredField("pagePath");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "/content/B2C/en_IN/support-catalog/pe/face-shavers/series-shavers");
    }

    @Test
    public void testGetter_getPagePath() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("pagePath");
        field.setAccessible(true);
        field.set(pojo, "/content/B2C/en_IN/support-catalog/pe/face-shavers/series-shavers");

        final String result = pojo.getPagePath();

        assertEquals("field wasn't retrieved properly", result, "/content/B2C/en_IN/support-catalog/pe/face-shavers/series-shavers");
    }
    @Test
    public void testSetter_setParentCode() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setParentCode("MENS_SHAVING_CA");

        final Field field = pojo.getClass().getDeclaredField("parentCode");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "MENS_SHAVING_CA");
    }

    @Test
    public void testGetter_getParentCode() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("parentCode");
        field.setAccessible(true);
        field.set(pojo, "MENS_SHAVING_CA");

        final String result = pojo.getParentCode();

        assertEquals("field wasn't retrieved properly", result, "MENS_SHAVING_CA");
    }
    @Test
    public void testSetter_setParentName() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setParentName("FACE Shavers");

        final Field field = pojo.getClass().getDeclaredField("parentName");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "FACE Shavers");
    }

    @Test
    public void testGetter_getParentName() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("parentName");
        field.setAccessible(true);
        field.set(pojo, "FACE Shavers");

        final String result = pojo.getParentName();

        assertEquals("field wasn't retrieved properly", result, "FACE Shavers");
    }
    @Test
    public void testSetter_setSeoName() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setSeoName("series-shavers");

        final Field field = pojo.getClass().getDeclaredField("seoName");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "series-shavers");
    }

    @Test
    public void testGetter_getSeoName() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("seoName");
        field.setAccessible(true);
        field.set(pojo, "series-shavers");

        final String result = pojo.getSeoName();

        assertEquals("field wasn't retrieved properly", result, "series-shavers");
    }
    @Test
    public void testSetter_setStatus() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setStatus("NORMAL");

        final Field field = pojo.getClass().getDeclaredField("status");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "NORMAL");
    }

    @Test
    public void testGetter_getStatus() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("status");
        field.setAccessible(true);
        field.set(pojo, "NORMAL");

        final String result = pojo.getStatus();

        assertEquals("field wasn't retrieved properly", result, "NORMAL");
    }
    @Test
    public void testSetter_setLowestPrice() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setLowestPrice("HR1922_20");

        final Field field = pojo.getClass().getDeclaredField("lowestPrice");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "HR1922_20");
    }

    @Test
    public void testGetter_getLowestPrice() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("lowestPrice");
        field.setAccessible(true);
        field.set(pojo, "HR1922_20");

        final String result = pojo.getLowestPrice();

        assertEquals("field wasn't retrieved properly", result, "HR1922_20");
    }
    @Test
    public void testSetter_setNoOfProducts() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setNoOfProducts("10");

        final Field field = pojo.getClass().getDeclaredField("noOfProducts");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "10");
    }

    @Test
    public void testGetter_getNoOfProducts() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("noOfProducts");
        field.setAccessible(true);
        field.set(pojo, "10");

        final String result = pojo.getNoOfProducts();

        assertEquals("field wasn't retrieved properly", result, "10");
    }
    @Test
    public void testSetter_setRank() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setRank("14");

        final Field field = pojo.getClass().getDeclaredField("rank");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "14");
    }

    @Test
    public void testGetter_getRank() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("rank");
        field.setAccessible(true);
        field.set(pojo, "14");

        final String result = pojo.getRank();

        assertEquals("field wasn't retrieved properly", result, "14");
    }
    @Test
    public void testSetter_setImageUrl() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setImageURL("https://images.philips.com/is/image/PhilipsConsumer/RQ310_16-IMS-en_IN");

        final Field field = pojo.getClass().getDeclaredField("imageURL");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "https://images.philips.com/is/image/PhilipsConsumer/RQ310_16-IMS-en_IN");
    }

    @Test
    public void testGetter_getImageUrl() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("imageURL");
        field.setAccessible(true);
        field.set(pojo, "https://images.philips.com/is/image/PhilipsConsumer/RQ310_16-IMS-en_IN");

        final String result = pojo.getImageURL();

        assertEquals("field wasn't retrieved properly", result, "https://images.philips.com/is/image/PhilipsConsumer/RQ310_16-IMS-en_IN");
    }    @Test
    public void testSetter_setCtn() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setCtn("HR1922_20");

        final Field field = pojo.getClass().getDeclaredField("ctn");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "HR1922_20");
    }

    @Test
    public void testGetter_getCtn() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("ctn");
        field.setAccessible(true);
        field.set(pojo, "HR1922_20");

        final String result = pojo.getCtn();

        assertEquals("field wasn't retrieved properly", result, "HR1922_20");
    }    @Test
    public void testSetter_setDtn() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setDtn("HR1922_20");

        final Field field = pojo.getClass().getDeclaredField("dtn");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "HR1922_20");
    }

    @Test
    public void testGetter_getDtn() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("dtn");
        field.setAccessible(true);
        field.set(pojo, "HR1922_20");

        final String result = pojo.getDtn();

        assertEquals("field wasn't retrieved properly", result, "HR1922_20");
    }    @Test
    public void testSetter_setProductUrl() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setProductURL("/c-p/RQ1250_16/shaver-series-9000-sensotouch-wet-dry-electric-shaver-with-aquatec");

        final Field field = pojo.getClass().getDeclaredField("productURL");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "/c-p/RQ1250_16/shaver-series-9000-sensotouch-wet-dry-electric-shaver-with-aquatec");
    }

    @Test
    public void testGetter_getProductUrl() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("productURL");
        field.setAccessible(true);
        field.set(pojo, "/c-p/RQ1250_16/shaver-series-9000-sensotouch-wet-dry-electric-shaver-with-aquatec");

        final String result = pojo.getProductURL();

        assertEquals("field wasn't retrieved properly", result, "/c-p/RQ1250_16/shaver-series-9000-sensotouch-wet-dry-electric-shaver-with-aquatec");
    }    @Test
    public void testSetter_setProductPagePath() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setProductPagePath("/content/B2C/en_GB/product-catalog/RQ1250_16");

        final Field field = pojo.getClass().getDeclaredField("productPagePath");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "/content/B2C/en_GB/product-catalog/RQ1250_16");
    }

    @Test
    public void testGetter_getProductPagePath() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("productPagePath");
        field.setAccessible(true);
        field.set(pojo, "/content/B2C/en_GB/product-catalog/RQ1250_16");

        final String result = pojo.getProductPagePath();

        assertEquals("field wasn't retrieved properly", result, "/content/B2C/en_GB/product-catalog/RQ1250_16");
    }

    @Test
    public void testSetter_setVersion() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        List<String> version = new ArrayList<String>();
        version.add("1");
        version.add("2");
        version.add("3");

        pojo.setVersions(version);



        final Field field = pojo.getClass().getDeclaredField("versions");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), version);
    }


    @Test
    public void testGetter_getVersion() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("versions");
        field.setAccessible(true);

        List<String> version = new ArrayList<String>();
        version.add("1");
        version.add("2");
        version.add("3");

        field.set(pojo, version);

        final List<String> result = pojo.getVersions();

        assertEquals("field wasn't retrieved properly", result, version);
    }

    @Test
    public void testSetter_setType() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setType("subcategory");

        final Field field = pojo.getClass().getDeclaredField("type");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "subcategory");
    }

    @Test
    public void testGetter_getType() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("type");
        field.setAccessible(true);
        field.set(pojo, "subcategory");

        final String result = pojo.getType();

        assertEquals("field wasn't retrieved properly", result, "subcategory");
    }
    @Test
    public void testSetter_setCreatedDate() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setCreatedDate("2014-03-20T06:08:18.430+01:00");

        final Field field = pojo.getClass().getDeclaredField("createdDate");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "2014-03-20T06:08:18.430+01:00");
    }

    @Test
    public void testGetter_getCreatedDate() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("createdDate");
        field.setAccessible(true);
        field.set(pojo, "2014-03-20T06:08:18.430+01:00");

        final String result = pojo.getCreatedDate();

        assertEquals("field wasn't retrieved properly", result, "2014-03-20T06:08:18.430+01:00");
    }
    @Test
    public void testSetter_setIsDeleted() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();

        pojo.setIsDeleted("false");

        final Field field = pojo.getClass().getDeclaredField("isDeleted");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), "false");
    }

    @Test
    public void testGetter_getIsDeleted() throws NoSuchFieldException, IllegalAccessException {
        final Data pojo = new Data();
        final Field field = pojo.getClass().getDeclaredField("isDeleted");
        field.setAccessible(true);
        field.set(pojo, "false");

        final String result = pojo.getIsDeleted();

        assertEquals("field wasn't retrieved properly", result, "false");
    }









}
