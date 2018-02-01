#Registration API
-dontwarn com.philips.cdp.registration.**
-dontwarn com.fasterxml.jackson.databind.ext.Java7SupportImpl
-keep class com.philips.cdp.registration.BuildConfig {*;}
-keep class com.philips.cdp.registration.User {*;}
-keep class com.philips.cdp.registration.hsdp.HsdpUserRecord {*;}
-keep class com.philips.cdp.registration.errormapping.CheckLocale {*;}
-keep class com.philips.cdp.registration.dao.** {*;}
-keep class com.philips.cdp.registration.ui.utils.** {*;}
-keep class com.philips.cdp.registration.hsdp.HsdpUser {*;}
-keep class com.philips.cdp.registration.listener.** {*;}
-keep class com.philips.cdp.registration.settings.RegistrationHelper { *; }
-keep class com.philips.cdp.registration.configuration.RegistrationConfiguration { *; }
-keep class com.philips.cdp.registration.settings.UserRegistrationInitializer { *; }
-keep class com.philips.cdp.registration.configuration.URConfigurationConstants { *; }
-keep class com.philips.cdp.registration.app.tagging.AppTagging { *; }

#Interfaces
-keep interface com.philips.cdp.registration.handlers.UpdateUserDetailsHandler {*;}
-keep interface com.philips.cdp.registration.handlers.RefreshLoginSessionHandler {*; }
-keep interface com.philips.cdp.registration.handlers.LogoutHandler {*;}

#Public Enums
-keep enum com.philips.cdp.registration.configuration.RegistrationLaunchMode { *; }
-keep enum com.philips.cdp.registration.settings.RegistrationFunction { *; }
-keep enum com.philips.cdp.registration.configuration.Configuration { *; }

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}