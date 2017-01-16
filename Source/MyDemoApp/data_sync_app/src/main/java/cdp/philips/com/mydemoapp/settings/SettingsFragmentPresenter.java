package cdp.philips.com.mydemoapp.settings;

import android.content.Context;

import com.philips.platform.core.listeners.DBRequestListener;

/**
 * Created by sangamesh on 09/01/17.
 */

public class SettingsFragmentPresenter {

    private final Context mContext;
    private final DBRequestListener dbRequestListener;
    public SettingsFragmentPresenter(Context mContext, DBRequestListener dbRequestListener) {
        this.mContext = mContext;
        this.dbRequestListener = dbRequestListener;
    }
}
