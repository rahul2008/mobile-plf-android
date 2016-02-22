package com.philips.cdp.registration;

/**
 * Created by 310190722 on 19-Feb-16.
 */
 class ABCD {
    private static ABCD mABCD;

    public String getmP() {
        return mP;
    }

    public void setmP(String mP) {
        this.mP = mP;
    }

    private String mP;


    public static synchronized ABCD getInstance(){
        if(mABCD == null){
            mABCD = new ABCD();
        }

        return mABCD;
    }



    private ABCD(){

    }
}
