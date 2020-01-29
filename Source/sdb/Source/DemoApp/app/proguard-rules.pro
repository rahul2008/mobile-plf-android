
#GMS
-keep  class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

-keepattributes InnerClasses,Exceptions


#ormlite
-keep public class com.j256.ormlite.** {*;}
-keep class com.j256.ormlite.** { *; }
-keep interface com.j256.ormlite.** { *; }
-dontwarn com.j256.ormlite.**
-dontwarn org.slf4j.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
-dontwarn javax.persistence.**
-dontwarn javax.lang.**
-dontwarn javax.annotation.**
-dontwarn javax.tools.**

#sqlcipher
-keep class net.sqlcipher.** {*;}
-keep interface net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** {*;}
-keep interface net.sqlcipher.database.** { *; }
-keep enum net.sqlcipher.**
-keepclassmembers enum net.sqlcipher.** { *; }


#Demo App
-keep public class com.philips.platform.database.**{*;}

#appinfra
-keep public class com.philips.platform.appinfra.rest.request.GsonCustomRequest.** { *; }
-keep public class com.philips.platform.appinfra.languagepack.model.** { *; }



