package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.SDKError;

/**
 * Created by philips on 7/7/17.
 */

public interface THSNoppCallBack {

    void onNoppReceivedSuccess(String string, SDKError sDKError );
    void onNoppReceivedFailure(Throwable throwable );


}
