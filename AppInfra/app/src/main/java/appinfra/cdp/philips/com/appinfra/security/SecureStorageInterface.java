package appinfra.cdp.philips.com.appinfra.security;

/**
 * Created by 310238114 on 4/11/2016.
 */
public  interface SecureStorageInterface {
    static final String DEVICE_FILE = "Device file";
    static final String CLOUD = "Cloud";
    static final String FILE_NAME = "shared_preference_file_name";

    public String storeValueForKey(String userKey,String valueToBeEncrypted);
    public String fetchValueForKey(String userKey);
    public boolean RemoveValueForKey(String userKey);
}
