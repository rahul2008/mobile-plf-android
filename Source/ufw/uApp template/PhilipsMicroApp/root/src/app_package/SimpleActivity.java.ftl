package ${packageName};


<#if applicationPackage??>
import ${applicationPackage}.R;
</#if>


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.uappframework.launcher.ActivityLauncher;

public class ${activityClass} extends ${superClass} {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.${layoutName});

        ${escapeXmlString(appTitle)}uAppInterface uAppInterface = new ${escapeXmlString(appTitle)}uAppInterface();
        uAppInterface.init(new ${escapeXmlString(appTitle)}uAppDependencies(null), new ${escapeXmlString(appTitle)}uAppSettings(this));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);// pass launch input if required
    }

}
