package com.philips.cdp.registration.settings;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;

/**
 * Created by 310243576 on 9/16/2016.
 */
public class RegistrationSettingsURLTest extends InstrumentationTestCase{

    RegistrationSettingsURL registrationSettingsURL;
    Context mContext;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        registrationSettingsURL = new RegistrationSettingsURL();
        mContext = getInstrumentation().getTargetContext();
    }

    public void testInitialiseConfigParameters(){
        synchronized(this){//synchronized block

            try{
                RegistrationHelper.getInstance().
                        setAppInfraInstance(new AppInfra.Builder().build(mContext));
            }catch(Exception e){
                System.out.println(e);}
        }
        RLog.init(mContext);
        try{
            registrationSettingsURL.initialiseConfigParameters("en-US");}
        catch(Exception e){

        }

        assertNotNull(registrationSettingsURL);
    }
}