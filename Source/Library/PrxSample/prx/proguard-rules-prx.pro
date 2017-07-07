
#General and network
-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses,Exceptions



#GSM
-keep  class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**


#gson
-keep interface com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer


#prxclient
-keep public class com.philips.cdp.prxclient.** {*;}
-keep interface com.philips.cdp.prxclient.** { *; }


#appinfra
-keep public class com.philips.platform.appinfra.AppInfra.** {*;}
-keep public interface com.philips.platform.appinfra.AppInfraInterface.** {*;}
-keep public interface com.philips.platform.appinfra.logging.LoggingInterface.** {*;}
-keep public class com.philips.platform.appinfra.rest.request.GsonCustomRequest.** {*;}
-keep public interface com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface.** {*;}


#volley
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }




