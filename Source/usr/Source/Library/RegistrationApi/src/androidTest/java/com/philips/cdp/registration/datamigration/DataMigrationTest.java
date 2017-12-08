package com.philips.cdp.registration.datamigration;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class DataMigrationTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    DataMigration continueSocialProviderLogin;
    Context context;


    public DataMigrationTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
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
