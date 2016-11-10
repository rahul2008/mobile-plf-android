/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewPropertyAnimator;

public class ModalAlertUtil {

    public static void animateAlpha(final View view, float fromAlpha, float toAlpha, int duration, final Runnable endAction) {
        ViewPropertyAnimator animator = view.animate().alpha(toAlpha).setDuration(duration);
        if (endAction != null) {
            animator.setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    endAction.run();
                }
            });
        }
        animator.start();
    }
}