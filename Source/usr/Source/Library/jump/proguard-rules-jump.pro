#Janrain lib
-keepclasseswithmembernames public class com.janrain.android.Jump {*;}
-keepclasseswithmembernames public class com.janrain.android.JumpConfig {*;}
-dontwarn com.janrain.android.**
-keep public class com.philips.cdp.security.SecurityHelper {
    public static void init(android.content.Context);
}
-keep public class com.philips.cdp.security.SecurityHelper {
    public static java.lang.String objectToString(java.io.Serializable);
}
-keep public class com.philips.cdp.security.SecurityHelper {
    public static java.lang.Object stringToObject(java.lang.String);
}
-keep public class com.philips.cdp.security.SecurityHelper {
    public static void migrateUserData(java.lang.String);
}
-keep public class com.philips.cdp.security.SecurityHelper {
    public static byte[] encrypt(java.lang.String);
}
-keep public class com.philips.cdp.security.SecurityHelper {
    public static byte[] decrypt(byte[]);
}
-keep public class com.philips.cdp.security.SecurityHelper {
    public static void generateSecretKey();
}