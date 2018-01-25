#Registration API

-keep class com.philips.cdp.registration.BuildConfig {*;}

-dontwarn com.philips.cdp.registration.**
-dontwarn com.fasterxml.jackson.databind.ext.Java7SupportImpl
-keep class com.philips.cdp.registration.User {*;}
-keep class com.philips.cdp.registration.AppIdentityInfo {*;}
-keep class com.philips.cdp.registration.hsdp.HsdpUserRecord {*;}
-keep class com.philips.cdp.registration.errormapping.CheckLocale {*;}
-keep class com.philips.cdp.registration.dao.** {*;}
-keep class com.philips.cdp.registration.ui.utils.** {*;}
-keep class com.philips.cdp.registration.hsdp.HsdpUser {*;}
-keep class com.philips.cdp.registration.app.tagging.** {*;}
-keep class com.philips.cdp.registration.listener.** {*;}
-keep interface com.philips.cdp.registration.handlers.UpdateUserDetailsHandler {*;}
-keep interface com.philips.cdp.registration.handlers.RefreshLoginSessionHandler {*; }
-keep interface com.philips.cdp.registration.handlers.LogoutHandler {*;}
-keep enum com.philips.cdp.registration.configuration.RegistrationLaunchMode { *; }
-keep enum com.philips.cdp.registration.settings.RegistrationFunction { *; }
-keep enum com.philips.cdp.registration.configuration.Configuration { *; }
-keep class com.philips.cdp.registration.settings.RegistrationHelper { *; }
-keep class com.philips.cdp.registration.configuration.RegistrationConfiguration { *; }
-keep class com.philips.cdp.registration.settings.UserRegistrationInitializer { *; }
-keep class com.philips.cdp.registration.configuration.URConfigurationConstants { *; }