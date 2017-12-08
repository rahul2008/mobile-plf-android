package com.philips.cdp.registration.ui.utils.test;

import android.content.Context;
import android.widget.TextView;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.ui.utils.FontLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class FontLoaderTest extends RegistrationApiInstrumentationBase {

    @Mock
    FontLoader fontLoader;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
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