package com.philips.platform;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.philips.platform.uappdemo.R;
import com.philips.platform.uid.utils.UIDActivity;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends UIDActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
//            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
//            findViewById(R.id.main_container).setSystemUiVisibility(LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private String TAG = getClass().getSimpleName();
    private View flagsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);

        mContentView = findViewById(R.id.fullscreen_content);

        flagsView = mContentView;


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            AdvancedImmersiveModeFragment fragment = new AdvancedImmersiveModeFragment();
            transaction.replace(R.id.main_container, fragment);
            transaction.commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first

       /* mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);*/

       demo1();
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void demo1() {
        int uiOptions = flagsView.getSystemUiVisibility();
        uiOptions &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
        uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE;
        uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        flagsView.setSystemUiVisibility(uiOptions);
        // END_INCLUDE(leanback_presets)

        dumpFlagStateToLog(uiOptions);
    }



    /**
     * Helper method to dump flag state to the log.
     * @param uiFlags Set of UI flags to inspect
     */
    public void dumpFlagStateToLog(int uiFlags) {
        if ((uiFlags & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
            Log.i(TAG, "SYSTEM_UI_FLAG_LOW_PROFILE is set");
        } else {
            Log.i(TAG, "SYSTEM_UI_FLAG_LOW_PROFILE is unset");
        }

        if ((uiFlags & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0) {
            Log.i(TAG, "SYSTEM_UI_FLAG_FULLSCREEN is set");
        } else {
            Log.i(TAG, "SYSTEM_UI_FLAG_FULLSCREEN is unset");
        }

        if ((uiFlags & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0) {
            Log.i(TAG, "SYSTEM_UI_FLAG_HIDE_NAVIGATION is set");
        } else {
            Log.i(TAG, "SYSTEM_UI_FLAG_HIDE_NAVIGATION is unset");
        }

        if ((uiFlags & View.SYSTEM_UI_FLAG_IMMERSIVE) != 0) {
            Log.i(TAG, "SYSTEM_UI_FLAG_IMMERSIVE is set");
        } else {
            Log.i(TAG, "SYSTEM_UI_FLAG_IMMERSIVE is unset");
        }

        if ((uiFlags & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) != 0) {
            Log.i(TAG, "SYSTEM_UI_FLAG_IMMERSIVE_STICKY is set");
        } else {
            Log.i(TAG, "SYSTEM_UI_FLAG_IMMERSIVE_STICKY is unset");
        }
    }
}
