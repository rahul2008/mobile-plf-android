#InAppPurchase
-keep class com.philips.cdp.di.iap.store.AbstractStore {*;}
-keep class com.philips.cdp.di.iap.store.HybrisStore {*;}
-keep class com.philips.cdp.di.iap.store.IAPUser {*;}
-keep class com.philips.cdp.di.iap.store.LocalStore {*;}
-keep class com.philips.cdp.di.iap.store.StoreConfiguration {*;}
-keep class com.philips.cdp.di.iap.store.StoreController {*;}
-keep interface com.philips.cdp.di.iap.store.StoreListener{*;}

-keep class com.philips.cdp.di.iap.model** {*;}
-keep interface com.philips.cdp.di.iap.model** {*;}

-keep class com.philips.cdp.di.iap.response** {*;}
-keep interface com.philips.cdp.di.iap.response** {*;}

-keep class com.philips.cdp.di.iap.integration** {*;}
-keep interface com.philips.cdp.di.iap.integration** {*;}

-keep class com.philips.cdp.di.iap.utils** {*;}
-keepattributes InnerClasses
#-keepattributes Signature
#-keep interface com.philips.cdp.di.iap.integration.IAPLaunchInput$IAPFlows** {*;}