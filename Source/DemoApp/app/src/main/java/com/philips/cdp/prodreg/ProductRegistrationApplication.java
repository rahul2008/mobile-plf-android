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
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

import java.util.Locale;

public class ProductRegistrationApplication extends Application {

    public static AIAppTaggingInterface mAIAppTaggingInterface;
    public static AppInfra gAppInfra;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        mAIAppTaggingInterface = gAppInfra.getTagging().createInstanceForComponent("Component name", "Component ID");
        new ProdRegHelper().init(getApplicationContext());
        initRegistration();
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
