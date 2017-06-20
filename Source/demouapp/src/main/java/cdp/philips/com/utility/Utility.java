package cdp.philips.com.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 310218660 on 12/8/2016.
 */

public class Utility {

    public boolean isOnline(Context context) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
        } catch (Exception ignored) {
            return false;
        }
    }
}
