package com.philips.platform.appframework.connectivity;

import com.android.volley.VolleyError;
import com.philips.cdp.registration.User;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConnectivityContract {

    interface View {
        void updateUIOnPostMomentSuccess(String momentId);

        void updateUIOnPostMomentError(VolleyError volleyError);

        void updateUIOnGetMomentSuccess(String momentValue);

        void updateUIOnGetMomentError(VolleyError volleyError);
    }

    interface UserActionsListener {
        void postMoment(User user, String momentValue);

        void getMoment(User user, String momentId);
    }
}
