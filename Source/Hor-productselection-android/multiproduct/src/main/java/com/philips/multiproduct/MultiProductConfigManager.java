package com.philips.multiproduct;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;


import com.philips.multiproduct.activity.MultiProductActivity;
import com.philips.multiproduct.listeners.ActionbarUpdateListener;
import com.philips.multiproduct.utils.MLogger;
import com.philips.multiproduct.utils.Constants;

import java.util.Locale;


public class MultiProductConfigManager {

    private static final String TAG = MultiProductConfigManager.class.getSimpleName();
    private static MultiProductConfigManager mDigitalCareInstance = null;
    private static Context mContext = null;
    private static Locale mLocale = null;

    /*
     * Initialize everything(resources, variables etc) required for DigitalCare.
     * Hosting app, which will integrate this DigitalCare, has to pass app
     * context.
     */
    private MultiProductConfigManager() {
    }

    /*
     * Singleton pattern.
     */
    public static MultiProductConfigManager getInstance() {
        if (mDigitalCareInstance == null) {
            mDigitalCareInstance = new MultiProductConfigManager();
        }
        return mDigitalCareInstance;
    }

    public Locale getLocale()
    {
        return mLocale;
    }


    /**
     * Returs the Context used in the DigitalCare Component
     *
     * @return Returns the Context using by the Component.
     */
    public Context getContext() {
        return mContext;
    }


    /**
     * <p>This is the DigitalCare initialization method. Please make sure to call this method before invoking the DigitalComponent.
     * For more help/details please make sure to have a glance at the Demo sample </p>
     *
     * @param applicationContext Please pass the valid  Context
     */
    public void initializeDigitalCareLibrary(Context applicationContext) {
        if (mContext == null) {
            MultiProductConfigManager.mContext = applicationContext;

        }
    }


    public void invokeDigitalCareAsFragment(FragmentActivity context,
                                            int parentContainerResId,
                                            ActionbarUpdateListener actionbarUpdateListener, int enterAnim,
                                            int exitAnim) {
        if (mContext == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale and consumerproductInfo before Support page is invoked");
        }
    }


    public void invokeDigitalCareAsActivity(int startAnimation, int endAnimation, ActivityOrientation orientation) {
        if (mContext == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale and consumerproductInfo before Support page is invoked");
        }

        Intent intent = new Intent(this.getContext(), MultiProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(Constants.STOP_ANIMATION_ID, endAnimation);
        intent.putExtra(Constants.SCREEN_ORIENTATION, orientation.getOrientationValue());
        getContext().startActivity(intent);
    }


    public void setLocale(String langCode, String countryCode) {

        if (langCode != null && countryCode != null) {
            mLocale = new Locale(langCode, countryCode);
            MLogger.d(TAG, "Setting Locale :  : " + mLocale.toString());
        }
    }


    public String getDigitalCareLibVersion() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * These are Flags used for setting/controlling screen orientation.
     * <p>This method helps only you are using the DigitalCare component from the Intent</p>
     * <p/>
     * <p> <b>Note : </b> The flags are similar to deafult android screen orientation flags</p>
     */
    public enum ActivityOrientation {


        SCREEN_ORIENTATION_UNSPECIFIED(-1), SCREEN_ORIENTATION_LANDSCAPE(0),
        SCREEN_ORIENTATION_PORTRAIT(1), SCREEN_ORIENTATION_USER(2), SCREEN_ORIENTATION_BEHIND(3),
        SCREEN_ORIENTATION_SENSOR(4),
        SCREEN_ORIENTATION_NOSENSOR(5),
        SCREEN_ORIENTATION_SENSOR_LANDSCAPE(6),
        SCREEN_ORIENTATION_SENSOR_PORTRAIT(7),
        SCREEN_ORIENTATION_REVERSE_LANDSCAPE(8),
        SCREEN_ORIENTATION_REVERSE_PORTRAIT(9),
        SCREEN_ORIENTATION_FULL_SENSOR(10),
        SCREEN_ORIENTATION_USER_LANDSCAPE(11),
        SCREEN_ORIENTATION_USER_PORTRAIT(12),
        SCREEN_ORIENTATION_FULL_USER(13),
        SCREEN_ORIENTATION_LOCKED(14);
        private int value;

        ActivityOrientation(int value) {
            this.value = value;
        }

        private int getOrientationValue() {
            return value;
        }
    }

}
