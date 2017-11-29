package com.philips.cdp.digitalcare;

import android.content.Context;

import com.philips.cdp.digitalcare.listeners.CcListener;
import com.philips.cdp.digitalcare.util.CustomRobolectricRunnerCC;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by philips on 29/11/17.
 */

@RunWith(CustomRobolectricRunnerCC.class)
@PrepareForTest(DigitalCareConfigManager.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class CcLaunchInputTest {

ProductModelSelectionType productModelSelectionType;
    CcListener ccListener;

    @Before
    public void setUp() throws Exception {

        productModelSelectionType = Mockito.mock(ProductModelSelectionType.class);
        ccListener = Mockito.mock(CcListener.class);

    }


    @Test
    public void testSetter_setProductModelSelectionType() throws NoSuchFieldException, IllegalAccessException {
        final CcLaunchInput pojo = new CcLaunchInput();

        pojo.setProductModelSelectionType(productModelSelectionType);

        final Field field = pojo.getClass().getDeclaredField("productModelSelectionType");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), productModelSelectionType);
    }

    @Test
    public void testGetter_getProductModelSelectionType() throws NoSuchFieldException, IllegalAccessException {
        final CcLaunchInput pojo = new CcLaunchInput();
        final Field field = pojo.getClass().getDeclaredField("productModelSelectionType");
        field.setAccessible(true);
        field.set(pojo, productModelSelectionType);

        final ProductModelSelectionType result = pojo.getProductModelSelectionType();

        assertEquals("field wasn't retrieved properly", result, productModelSelectionType);
    }

    @Test
    public void testSetter_setCcListener() throws NoSuchFieldException, IllegalAccessException {
        final CcLaunchInput pojo = new CcLaunchInput();

        pojo.setConsumerCareListener(ccListener);

        final Field field = pojo.getClass().getDeclaredField("consumerCareListener");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), ccListener);
    }

    @Test
    public void testGetter_getCcListener() throws NoSuchFieldException, IllegalAccessException {
        final CcLaunchInput pojo = new CcLaunchInput();
        final Field field = pojo.getClass().getDeclaredField("consumerCareListener");
        field.setAccessible(true);
        field.set(pojo, ccListener);

        final CcListener result = pojo.getConsumerCareListener();

        assertEquals("field wasn't retrieved properly", result, ccListener);
    }

    @Test
    public void testSetter_setLivechat() throws NoSuchFieldException, IllegalAccessException {
        final CcLaunchInput pojo = new CcLaunchInput();

        String livechaturl = "http://ph-china.livecom.cn/webapp/index.html?app_openid=ph_6idvd4fj&token=PhilipsTest";

        pojo.setLiveChatUrl("http://ph-china.livecom.cn/webapp/index.html?app_openid=ph_6idvd4fj&token=PhilipsTest");

        final Field field = pojo.getClass().getDeclaredField("liveChatUrl");
        field.setAccessible(true);
        Assert.assertEquals("Fields didn't match", field.get(pojo), livechaturl);
    }

    @Test
    public void testGetter_getLivechat() throws NoSuchFieldException, IllegalAccessException {
        final CcLaunchInput pojo = new CcLaunchInput();
        String livechaturl = "http://ph-china.livecom.cn/webapp/index.html?app_openid=ph_6idvd4fj&token=PhilipsTest";
        final Field field = pojo.getClass().getDeclaredField("liveChatUrl");
        field.setAccessible(true);
        field.set(pojo, "http://ph-china.livecom.cn/webapp/index.html?app_openid=ph_6idvd4fj&token=PhilipsTest");

        final String result = pojo.getLiveChatUrl();

        assertEquals("field wasn't retrieved properly", result, livechaturl );
    }

}
