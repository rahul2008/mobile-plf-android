#Pojo classes required by Retorfit to reflect the response

#Data-Services Moments
-keep class com.philips.platform.datasync.moments.UCoreMoment { *; }
-keep class com.philips.platform.datasync.moments.UCoreDetail { *; }
-keep class com.philips.platform.datasync.moments.UCoreMeasurement { *; }
-keep class com.philips.platform.datasync.moments.UCoreMomentsHistory { *; }
-keep class com.philips.platform.datasync.moments.UCoreMomentSaveResponse { *; }
-keep class com.philips.platform.datasync.moments.UCoreMeasurementGroupDetail { *; }
-keep class com.philips.platform.datasync.moments.UCoreMeasurementGroups { *; }

#Data-Services Consent
-keep class com.philips.platform.datasync.consent.UCoreConsentDetail { *; }

#Data-Services Characteristics
-keep class com.philips.platform.datasync.characteristics.UCoreCharacteristics { *; }
-keep class com.philips.platform.datasync.characteristics.UCoreUserCharacteristics { *; }

#For Dataservices Demo Micro App
-keep class com.philips.platform.dscdemo.pojo.AppCharacteristics { *; }
-keep class com.philips.platform.dscdemo.pojo.AppUserCharacteristics { *; }

#Data-Services Settings
-keep class com.philips.platform.datasync.settings.UCoreSettings { *; }

#Data-Services Insight
-keep class com.philips.platform.datasync.insights.UCoreInsight { *; }
-keep class com.philips.platform.datasync.insights.UCoreInsightList { *; }

#Data-Services Push notification
-keep class com.philips.platform.datasync.PushNotification.UCorePushNotification { *; }

#Data-Services Device pairing
-keep class com.philips.platform.datasync.devicePairing.UCoreDevicePair { *; }

#Data-Services Subject Profile
-keep class com.philips.platform.datasync.subjectProfile.UCoreCreateSubjectProfileRequest { *; }
-keep class com.philips.platform.datasync.subjectProfile.UCoreCreateSubjectProfileResponse { *; }
-keep class com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile { *; }
-keep class com.philips.platform.datasync.subjectProfile.UCoreSubjectProfileList { *; }

# Sqlcipher
-keep class net.sqlcipher.** {*;}
-keep interface net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** {*;}
-keep interface net.sqlcipher.database.** { *; }
-keep enum net.sqlcipher.**
-keepclassmembers enum net.sqlcipher.** { *; }

#Ormlite
-dontwarn org.slf4j.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
-dontwarn javax.persistence.**
-dontwarn javax.lang.**
-dontwarn javax.annotation.**
-dontwarn javax.tools.**
-dontwarn com.j256.ormlite.**

-keep public class com.j256.ormlite.** {*;}
-keep class com.j256.ormlite.** { *; }
-keep interface com.j256.ormlite.** { *; }
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keep @com.j256.ormlite.table.DatabaseTable class * { *; }

# Retrofit
-dontwarn com.squareup.okhttp.**
-dontwarn retrofit.**
-dontwarn okio.**
-dontwarn rx.**
-dontwarn android.app.Notification

-keep class com.squareup.** { *; }
-keep interface com.squareup.** { *; }
-keep class retrofit.** { *; }
-keep interface retrofit.** { *;}

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

#Gson
-dontwarn com.google.gson.**
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** {*;}
-keep class com.google.gson.examples.android.model.** { *; }

#Green Robot Eventbus
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class de.greenrobot.event.** { *; }
-keep class de.greenrobot.** { *; }
-keepclassmembers class ** {
    public void onEvent(**);
}
-keepclassmembers,includedescriptorclasses class ** { public void onEvent*(**); }

# Jodatime
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

# from dagger.android.DispatchingAndroidInjector
-dontwarn com.google.errorprone.annotations.*




