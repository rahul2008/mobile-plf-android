package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.AppIdentityInfo;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 9/6/2016.
 */
public class URDependanciesTest extends InstrumentationTestCase {
    Context mContext;
    URDependancies mURDependancies;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
    }
    public void testURDependencies(){
        synchronized(this){//synchronized block

            try{
                AppInfraSingleton.setInstance( new AppInfra.Builder().build(mContext));
                RegistrationHelper.getInstance().setAppInfraInstance(AppInfraSingleton.getInstance());
                mURDependancies = new URDependancies(RegistrationHelper.getInstance().getAppInfraInstance());
                assertNull(mURDependancies);
            }catch(Exception e){System.out.println(e);}
        }
    }
}