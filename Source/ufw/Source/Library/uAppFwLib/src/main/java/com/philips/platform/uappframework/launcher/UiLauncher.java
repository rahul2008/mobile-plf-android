/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.launcher;


import androidx.annotation.AnimRes;

import java.io.Serializable;

/**
 * Contains methods for setting animations for the activity
 *
 * @since 1.0.0
 */


public abstract class UiLauncher implements Serializable {


    private static final long serialVersionUID = -8906511992872569045L;
    /**
     * Enter {@Link android.view.animation}
     */

    protected int mEnterAnimResId;
    protected int mExitAnimResId;

    /**
     * Animation for enter.
     * @returns the enter animation ID  {@Link android.view.animation}
     * @since 1.0.0
     */
    public int getEnterAnimation() {
        return mEnterAnimResId;
    }
    /**
     * Animation for exit.
     * @returns the exit animation ID  {@Link android.view.animation}
     * @since 1.0.0
     */
    public int getExitAnimation() {
        return mExitAnimResId;
    }

    /**
     * Setting custom animations.  {@Link android.view.animation}
     * @param enterAnim the enter animation
     * @param exitAnim the exit animation
     * @since 1.0.0
     */
    public void setCustomAnimation(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        this.mEnterAnimResId = enterAnim;
        this.mExitAnimResId = exitAnim;
    }

}
