package com.philips.platform.appinfra.contentloader;

import com.philips.platform.appinfra.AppInfraInterface;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by 310238114 on 10/27/2016.
 */

@RunWith(RobolectricTestRunner.class)
public class ContentLoaderTest  {
    @Test
    public void publicMethods() throws Exception {
        assertNotNull(ContentLoader.class.getConstructor(String.class,int.class,Class.class,String.class, AppInfraInterface.class));
    }
}