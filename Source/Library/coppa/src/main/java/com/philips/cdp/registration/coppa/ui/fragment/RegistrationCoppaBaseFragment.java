
package com.philips.cdp.registration.coppa.ui.fragment;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.apptagging.AppTaggingErrors;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RLog;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class RegistrationCoppaBaseFragment extends Fragment {

    protected int mLeftRightMarginPort;

    protected int mLeftRightMarginLand;

    protected abstract void setViewParams(Configuration config, int width);

    protected abstract void handleOrientation(final View view);

    public abstract int getTitleResourceId();

    public int getPrevTitleResourceId() {
        return mPrevTitleResourceId;
    }

    public void setPrevTitleResourceId(int mPrevTitleResourceId) {
        this.mPrevTitleResourceId = mPrevTitleResourceId;
    }

    private int mPrevTitleResourceId = -99;

    protected static int mWidth = 0;
    protected static int mHeight = 0;

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Thread mUiThread = Looper.getMainLooper().getThread();
    protected final void handleOnUIThread(Runnable runnable) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    private final int JELLY_BEAN = 16;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomLocale();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaBaseFragment : onCreate");
        mLeftRightMarginPort = (int) getResources().getDimension(R.dimen.reg_layout_margin_port);
        mLeftRightMarginLand = (int) getResources().getDimension(R.dimen.reg_layout_margin_land);
    }

    private void setCustomLocale() {
        Locale.setDefault(RegistrationHelper.getInstance().getLocale(getContext()));
        Configuration config = new Configuration();
        config.locale = RegistrationHelper.getInstance().getLocale(getContext());
        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaBaseFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaBaseFragment : onStart");
    }

    @Override
    public void onResume() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaBaseFragment : onResume");

        super.onResume();


        setCurrentTitle();


    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaBaseFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaBaseFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaBaseFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaBaseFragment : onDestroy");
        setPrevTiltle();
        super.onDestroy();
    }

    private void setPrevTiltle() {
        RegistrationCoppaFragment fragment = (RegistrationCoppaFragment) getParentFragment();

        if (null != fragment && null != fragment.getUpdateTitleListener()
                && mPrevTitleResourceId != -99) {
            if( this instanceof ParentalApprovalFragment){
                int count = fragment.getChildFragmentManager().getBackStackEntryCount();
                Fragment regFragment = fragment.getChildFragmentManager().getFragments().get(count);
                if (regFragment != null && regFragment instanceof RegistrationFragment) {
                    fragment.getUpdateTitleListener().updateRegistrationTitleWithBack(((RegistrationFragment)regFragment).getCurrentTitleResource());
                }

            }else {
                if (fragment.getFragmentBackStackCount() > 2) {
                    fragment.getUpdateTitleListener().updateRegistrationTitleWithBack(
                            mPrevTitleResourceId);
                } else {
                    fragment.getUpdateTitleListener().updateRegistrationTitle(mPrevTitleResourceId);
                }

                trackBackActionPage();
                fragment.setResourceID(mPrevTitleResourceId);
            }
        }else{
            if( this instanceof ParentalApprovalFragment){

                int count = fragment.getChildFragmentManager().getBackStackEntryCount();

                Fragment regFragment = fragment.getChildFragmentManager().getFragments().get(count);
                if (regFragment != null && regFragment instanceof RegistrationFragment) {
                    fragment.getUpdateTitleListener().updateRegistrationTitle(((RegistrationFragment)regFragment).getCurrentTitleResource());
                }

            }
        }


    }

    private void trackBackActionPage() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaBaseFragment : onDetach");
    }

    private void setCurrentTitle() {

        RegistrationCoppaFragment fragment = (RegistrationCoppaFragment) getParentFragment();
        if (null != fragment && null != fragment.getUpdateTitleListener()
                && -99 != fragment.getResourceID()) {
            mPrevTitleResourceId =  fragment.getResourceID();
        }
        if (fragment.getFragmentBackStackCount() > 1) {
                fragment.getUpdateTitleListener().updateRegistrationTitleWithBack(getTitleResourceId());
        } else {
            fragment.getUpdateTitleListener().updateRegistrationTitle(getTitleResourceId());
        }
        fragment.setResourceID(getTitleResourceId());
    }

    public RegistrationCoppaFragment getRegistrationFragment() {
        Fragment fragment = getParentFragment();
        if (fragment != null && (fragment instanceof RegistrationCoppaFragment)) {
            return (RegistrationCoppaFragment) fragment;
        }
        return null;
    }

    protected void consumeTouch(View view) {
        if (view == null)
            return;
        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }


    protected void applyParams(Configuration config, View view, int width) {

        LayoutParams mParams = (LayoutParams) view.getLayoutParams();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (getResources().getBoolean(R.bool.isTablet)) {
                mParams.leftMargin = mParams.rightMargin = width / 5;
            } else {
                mParams.leftMargin = mParams.rightMargin = 0;
            }
        } else {
            if (getResources().getBoolean(R.bool.isTablet)) {
                mParams.leftMargin = mParams.rightMargin = (int) (((width / 6) * (1.75)));
            } else {
                mParams.leftMargin = mParams.rightMargin = (int) ((width) / 6);
            }
        }
        view.setLayoutParams(mParams);
    }

    protected void trackPage(String currPage) {
        AppTagging.trackPage(currPage);
    }

    protected void trackActionStatus(String state, String key, String value) {
        AppTagging.trackAction(state, key, value);
    }

    protected void trackActionForRemarkettingOption(String state) {
        AppTagging.trackAction(state, null, null);
    }

    protected void trackActionForAcceptTermsOption(String state) {
        AppTagging.trackAction(state, null, null);
    }

    protected void trackActionRegisterError(int errorCode) {
        AppTaggingErrors.trackActionRegisterError(errorCode);
    }

    protected void trackActionLoginError(int errorCode) {
        AppTaggingErrors.trackActionLoginError(errorCode);
    }

    protected void trackActionForgotPasswordFailure(int errorCode) {
        AppTaggingErrors.trackActionForgotPasswordFailure(errorCode);
    }

    protected void trackActionResendVerificationFailure(int errorCode) {
        AppTaggingErrors.trackActionResendNetworkFailure(errorCode);
    }

    protected void trackMultipleActionsRegistration() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(AppTagingConstants.REGISTRATION_CHANNEL, AppTagingConstants.MY_PHILIPS);
        map.put(AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.START_USER_REGISTRATION);
        AppTagging.trackMultipleActions(AppTagingConstants.SEND_DATA, map);
    }

    protected void trackMultipleActionsLogin(String providerName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(AppTagingConstants.LOGIN_CHANNEL, providerName);
        map.put(AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.LOGIN_START);
        AppTagging.trackMultipleActions(AppTagingConstants.SEND_DATA, map);
    }

    protected void trackMultipleActionsMap(String state, HashMap map) {
        AppTagging.trackMultipleActions(state, map);
    }

    protected void handleOrientationOnView(final View view) {
        if (null == view) {
            return;
        }
        if (mWidth == 0 && mHeight == 0) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Configuration config = getResources().getConfiguration();
                    if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        mWidth = view.getWidth();
                        mHeight = view.getHeight();
                    } else {
                        mWidth = view.getHeight();
                        mHeight = view.getWidth();
                    }

                    if (Build.VERSION.SDK_INT < JELLY_BEAN) {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    setViewParams(getResources().getConfiguration(), view.getWidth());
                }
            });
        } else {
            Configuration config = getResources().getConfiguration();
            if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setViewParams(getResources().getConfiguration(), mWidth);
            } else {
                setViewParams(getResources().getConfiguration(), mHeight);
            }

        }
    }


    public void setCustomParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setViewParams(config, mWidth);
        } else {
            setViewParams(config, mHeight);
        }

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        setCustomLocale();


    }

    protected void scrollViewAutomatically(final View view, final ScrollView scrollView) {
        view.requestFocus();
      /* scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, view.getTop());
            }
        });*/

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, view.getTop());
                    }
                });
                if (Build.VERSION.SDK_INT < JELLY_BEAN) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

}
