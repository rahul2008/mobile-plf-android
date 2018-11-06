//package com.philips.cdp.registration.ui.utils;
//
//import android.content.Context;
//import android.widget.TextView;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import static junit.framework.Assert.assertNotNull;
//
//@RunWith(MockitoJUnitRunner.class)
//public class FontLoaderTest {
//
//    //@Mock
//    FontLoader fontLoader;
//
//    @Mock
//    Context context;
//
//    @Before
//    public void setUp() throws Exception {
//        // super.setUp();
//
//        assertNotNull(fontLoader.getInstance());
//
//        fontLoader = fontLoader.getInstance();
//
//    }
//
//    @Test
//    public void testGetInstance() throws Exception {
//        assertNotNull(fontLoader);
//        fontLoader.getInstance();
//    }
//
//    @Test
//    public void testSetTypeface() throws Exception {
//
//        TextView tv = new TextView(context);
//        fontLoader.setTypeface(tv, "PUIIcon.ttf");
//        fontLoader.setTypeface(tv, null);
//
//    }
//}