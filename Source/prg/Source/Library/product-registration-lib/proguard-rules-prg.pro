

#Product Registration library

-keep class com.philips.cdp.prodreg.model.** {*;}
-keep class com.philips.cdp.prodreg.register.** {*;}
-keep class com.philips.cdp.prodreg.localcache.** {*;}
-dontwarn com.fasterxml.jackson.databind.ext.Java7SupportImpl
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}