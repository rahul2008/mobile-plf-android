package com.philips.appinfra;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.appinfra.securestorage.SecureStorage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by 310238114 on 4/7/2016.
 */
public class SecureStorageTest extends MockitoTestCase {
    SecureStorage mSecureStorage=null;
   // Context context = Mockito.mock(Context.class);

    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();

        assertNotNull(context);
        mSecureStorage = new SecureStorage(context, SecureStorage.DEVICE_FILE);
        assertNotNull(mSecureStorage);

    }




    public void testStoreValueForKey() throws Exception {

        SecureStorage secureStorageMock = mock(SecureStorage.class);



        assertNull(mSecureStorage.storeValueForKey("", "value"));
        assertNull(mSecureStorage.storeValueForKey("", ""));
        assertNull(mSecureStorage.storeValueForKey("key", null));
        assertNull(mSecureStorage.storeValueForKey(null, "value"));
        assertNull(mSecureStorage.storeValueForKey(null, null));

        assertNotNull(mSecureStorage.storeValueForKey("key", "value")); // true condition

        // value passed by user should not be same as that of its encrypted equivalent

        }

    public void testFetchValuetForKey() throws Exception {

        assertNull(mSecureStorage.fetchValueForKey(null));
        assertNull(mSecureStorage.fetchValueForKey(""));
        //assertNotNull(mSecureStorage.fetchValueForKey("key"));
    }

    public void testSharedPreferences(){
        final SharedPreferences sharedPreferencesMock = mock(SharedPreferences.class);
        when(sharedPreferencesMock.getString("key",null)).thenReturn("value");
        when(sharedPreferencesMock.getString("",null)).thenReturn(null);
        when(sharedPreferencesMock.getString(null,null)).thenReturn(null);
        SecureStorage secureStorage = new SecureStorage(context,SecureStorage.DEVICE_FILE){
            @Override
            protected SharedPreferences getSharedPreferences() {
                return sharedPreferencesMock;
            }
        };
    }

    public void testRemoveValueForKey() throws Exception {

        assertFalse(mSecureStorage.RemoveValueForKey(""));
        assertFalse(mSecureStorage.RemoveValueForKey(null));

        //assertEquals(mSecureStorage.RemoveValueForKey("key"),mSecureStorage.deleteEncryptedData("key"));


    }
}