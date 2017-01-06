package com.philips.cdp.registration.ui.utils.test;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;
import android.widget.TextView;

import com.philips.cdp.registration.ui.utils.FontLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by 310243576 on 8/17/2016.
 */
public class FontLoaderTest extends InstrumentationTestCase {

    @Mock
    FontLoader fontLoader;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();

        assertNotNull(fontLoader.getInstance());

        fontLoader = fontLoader.getInstance();
        context = getInstrumentation().getTargetContext();

    }

    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(fontLoader);
        fontLoader.getInstance();
    }

    @Test
    public void testSetTypeface() throws Exception {

        TextView tv = new TextView(context);
        fontLoader.setTypeface(tv,"PUIIcon.ttf");
        fontLoader.setTypeface(tv,null);

    }
}