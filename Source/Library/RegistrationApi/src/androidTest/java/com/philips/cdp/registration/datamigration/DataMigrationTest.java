package com.philips.cdp.registration.datamigration;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.controller.RegisterSocial;
import com.philips.cdp.registration.controller.ResendVerificationEmail;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import org.json.JSONArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 310243576 on 8/11/2016.
 */
public class DataMigrationTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    DataMigration continueSocialProviderLogin;
    Context context;


    public DataMigrationTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        context = getInstrumentation().getContext();
        continueSocialProviderLogin = new DataMigration(context);
    }

    public void testCheckFileEncryptionStatus(){
        continueSocialProviderLogin.checkFileEncryptionStatus();
    }
    public void testReadDataAndDeleteFile(){
        Method method = null;
        String s= "sample";
        try {
            method = DataMigration.class.getDeclaredMethod("readDataAndDeleteFile", String.class);
            method.setAccessible(true);
            method.invoke(continueSocialProviderLogin, s);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void testMigrateFileData(){
        Method method = null;
        String s= "sample";
        try {
            method = DataMigration.class.getDeclaredMethod("migrateFileData", String.class);
            method.setAccessible(true);
            method.invoke(continueSocialProviderLogin, s);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    public void testWriteDataToFile(){
        Method method = null;
        String s= "sample";
        try {
            method = DataMigration.class.getDeclaredMethod("writeDataToFile", new Class[] { String.class, String.class });
            method.setAccessible(true);
            method.invoke(continueSocialProviderLogin,new Object[] { s,s });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }    }
}
