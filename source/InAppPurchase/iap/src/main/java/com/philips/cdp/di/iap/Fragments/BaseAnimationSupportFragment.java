/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : InAppPurchase
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.Fragments;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPLog;

public abstract class BaseAnimationSupportFragment extends BaseNoAnimationFragment {

    private String mUnderlyingFragmentTag = null;

    public enum AnimationType {
        /**
         * No animation for Fragment
         */
        NONE,
        /**
         * Right to Left translate animation
         */
        RIGHT_TO_LEFT_67PERCENT,
        /**
         * Right to Left translate left
         */
        RIGHT_TO_LEFT,
        /**
         * Top to Bottom translate animation
         */
        TOP_TO_BOTTOM,
        /**
         * Bottom to Top translate animation
         */
        BOTTOM_TO_TOP
    }

    public enum TransitionAnimation {
        ENTER, EXIT, ENTEREXIT, NONE;
    }

    private AnimationType getAnimationType() {
        if (getArguments() == null || !getArguments().containsKey(NetworkConstants.EXTRA_ANIMATIONTYPE)) {
            return getDefaultAnimationType();
        }
        return AnimationType.values()[getArguments().getInt(NetworkConstants.EXTRA_ANIMATIONTYPE)];
    }

    protected abstract AnimationType getDefaultAnimationType();

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!enter) return null;

        Animation animation = getEnterAnimation();
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "Animation started for fragment - "
                        + BaseAnimationSupportFragment.this.getClass().getSimpleName());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // NOP
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "Animation ended for fragment - "
                        + BaseAnimationSupportFragment.this.getClass().getSimpleName());
                animation.setAnimationListener(null);
                //removeUnderlyingFragment(getActivity(), mUnderlyingFragmentTag);
            }
        });
        return animation;
    }

    private Animation getEnterAnimation() {
        switch (getAnimationType()) {
            case NONE:
                IAPLog.d(IAPLog.ANIMATION, "Fragment animation: NONE");
                return AnimationUtils.loadAnimation(getActivity(), R.anim.none);
            case RIGHT_TO_LEFT_67PERCENT:
                IAPLog.d(IAPLog.ANIMATION, "Fragment animation: RIGHT TO LEFT 807PERCENT");
                return AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_from_right_80percent);

            case RIGHT_TO_LEFT:
                IAPLog.d(IAPLog.ANIMATION, "Fragment animation: RIGHT TO LEFT");
                return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_right);
            case BOTTOM_TO_TOP:
                IAPLog.d(IAPLog.ANIMATION, "Fragment animation: BOTTOM TO TOP");
                return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_bottom);
            default:
                IAPLog.d(IAPLog.ANIMATION, "Fragment animation defaulted to: NONE");
                return AnimationUtils.loadAnimation(getActivity(), R.anim.none);
        }
    }

//    public boolean removeUnderlyingFragment(Activity activity, String tag) {
//        if (activity == null || !(activity )) return false;
//        if (tag == null || tag.isEmpty()) return false;
//
//        (activity).removeFragment(tag);
//        return true;
//    }

    public void setUnderlyingFragmentTag(String tag) {
        mUnderlyingFragmentTag = tag;
    }
}
