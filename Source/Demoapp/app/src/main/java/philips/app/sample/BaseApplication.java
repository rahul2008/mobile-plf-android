package philips.app.sample;

import android.app.Application;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * Created by 310213373 on 8/17/2016.
 */
public class BaseApplication extends Application {

    AppInfra appInfra;
    @Override
    public void onCreate() {
        super.onCreate();
        UappDependencies uapp= new UappDependencies(appInfra);
        uapp.getAppInfra();

    }
}
