package cdp.philips.com.mydemoapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.dscdemo.DSDemoAppuAppDependencies;
import com.philips.platform.dscdemo.DSDemoAppuAppInterface;
import com.philips.platform.mya.catk.ConsentInteractor;
import com.philips.platform.mya.catk.ConsentsClient;
import com.philips.platform.mya.csw.justintime.JustInTimeTextResources;
import com.philips.platform.pif.chi.ConsentDefinitionRegistry;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class LauncherActivity extends Activity {
    private DSDemoAppuAppInterface dsDemoAppuAppInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
        dsDemoAppuAppInterface = new DSDemoAppuAppInterface();
    }

    public void launch(View v) {

        UappDependencies dsDemoDeps = createDependencies();
        UappSettings dsAppSettings = createSettings();
        dsDemoAppuAppInterface.init(dsDemoDeps, dsAppSettings);
        dsDemoAppuAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);
    }

    private UappDependencies createDependencies() {
        AppInfraInterface ail = DemoApplication.getInstance().getAppInfra();
        JustInTimeTextResources jitTextRes = new JustInTimeTextResources();
        ConsentInteractor momentConsentHandler = new ConsentInteractor(ConsentsClient.getInstance());
        ConsentDefinition consentDefinition = ConsentDefinitionRegistry.getDefinitionByConsentType("moment");
        return new DSDemoAppuAppDependencies(ail, momentConsentHandler, consentDefinition, jitTextRes);
    }

    private UappSettings createSettings() {
        return new UappSettings(getApplicationContext());
    }
}
