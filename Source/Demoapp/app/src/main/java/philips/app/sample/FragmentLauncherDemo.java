package philips.app.sample;

import android.support.v4.app.FragmentActivity;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * Created by 310213373 on 8/12/2016.
 */
public class FragmentLauncherDemo extends FragmentLauncher  {
    /**
     * FragmentLauncher method for launching the uApp.
     *
     * @param fragmentActivity
     * @param containerResId
     * @param actionBarListener
     */
    public FragmentLauncherDemo(FragmentActivity fragmentActivity, int containerResId, ActionBarListener actionBarListener) {
        super(fragmentActivity, containerResId, actionBarListener);
    }
}
