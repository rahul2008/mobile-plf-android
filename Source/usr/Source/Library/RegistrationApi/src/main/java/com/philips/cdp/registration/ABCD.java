/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration;

class ABCD {
    private static volatile ABCD mABCD;

    public String getmP() {
        return mP;
    }

    public void setmP(String mP) {
        this.mP = mP;
    }

    private String mP;


    public static synchronized ABCD getInstance() {
        if (mABCD == null) {
            synchronized (ABCD.class) {
                if (mABCD == null) {
                    mABCD = new ABCD();
                }
            }

        }
        return mABCD;
    }



    private ABCD(){

    }
}
