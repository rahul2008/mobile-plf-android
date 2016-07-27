package com.philips.cdp.prodreg;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.Locale;

public class ProductRegistrationApplication extends Application {

    public static AppTaggingInterface mAIAppTaggingInterface;
    public static AppInfraInterface gAppInfra;
    public static LoggingInterface AILoggingInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        initProductRegistration();
        initAppInfra();
        initRegistration();
    }

    private void initAppInfra() {
        AppInfraSingleton.setInstance(gAppInfra = new AppInfra.Builder().build(getApplicationContext()));
        gAppInfra = AppInfraSingleton.getInstance();
        mAIAppTaggingInterface = gAppInfra.getTagging().createInstanceForComponent("Product Registration", com.philips.cdp.product_registration_lib.BuildConfig.VERSION_NAME);
        mAIAppTaggingInterface.setPreviousPage("DemoPage");
        AILoggingInterface = gAppInfra.getLogging().createInstanceForComponent("Product Registration", com.philips.cdp.product_registration_lib.BuildConfig.VERSION_NAME);
        if (BuildConfig.DEBUG) {
            AILoggingInterface.enableConsoleLog(true);
            AILoggingInterface.enableFileLog(true);
        } else {
            AILoggingInterface.enableConsoleLog(false);
            AILoggingInterface.enableFileLog(false);
        }
    }

    private void initProductRegistration() {
        new ProdRegHelper().init(getApplicationContext());
    }

    private void initRegistration() {
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();
        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);
        RegistrationHelper.getInstance().initializeUserRegistration(this);
    }
}
