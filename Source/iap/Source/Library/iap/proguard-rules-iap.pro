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