package com.philips.cdp.prodreg;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.tagging.Tagging;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

import java.util.Locale;

public class ProductRegistrationApplication extends Application {

    public static AIAppTaggingInterface mAIAppTaggingInterface;
    public static AppInfraInterface gAppInfra;


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
        mAIAppTaggingInterface = gAppInfra.getTagging().createInstanceForComponent("Product Registration", "Component ID");
    }

    private void initProductRegistration() {
        new ProdRegHelper().init(getApplicationContext());
    }

    private void initRegistration() {

        Tagging.enableAppTagging(true);
        Tagging.setTrackingIdentifier("integratingApplicationAppsId");
        Tagging.setLaunchingPageName("demo_app_home");
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationHelper.getInstance().initializeUserRegistration(this);
        Tagging.init(this, "Product Registration");
    }
}
