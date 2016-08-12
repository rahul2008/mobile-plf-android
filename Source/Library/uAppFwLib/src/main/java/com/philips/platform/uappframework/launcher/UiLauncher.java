/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.launcher;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.animation.Animation;

import java.io.Serializable;

public abstract class UiLauncher implements Serializable {

    private static final long serialVersionUID = -8906511992872569045L;
    /**
     * Enter {@Link android.view.animation}
     */
    protected int mEnterAnimResId;

    /**
     * Exit {@Link android.view.animation}
     */
    protected int mExitAnimResId;

    /**
     * returns the enter animation ID  {@Link android.view.animation}
     */
    public int getEnterAnimation() {
        return mEnterAnimResId;
    }
    /**
     * returns the exit animation ID  {@Link android.view.animation}
     */
    public int getExitAnimation() {
        return mExitAnimResId;
    }

    /**
     * for setting custom animations  {@Link android.view.animation}
     */
    public void setCustomAnimation(int enterAnim, int exitAnim) {
        this.mEnterAnimResId = enterAnim;
        this.mExitAnimResId = exitAnim;
    }

}
