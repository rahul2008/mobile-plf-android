/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.details;


import com.philips.platform.mya.base.mvp.MyaBaseView;
import com.philips.platform.mya.base.mvp.MyaPresenterInterface;
import com.philips.platform.myaplugin.user.UserDataModelProvider;

import java.util.Date;

class MyaDetailContract {

    interface View extends MyaBaseView {

        void setUserName(String name);

        void setCircleText(String data);

        void setEmail(String email);

        void setGender(String gender);

        void setDateOfBirth(Date dateOfBirth);

        void setMobileNumber(String number);

        void handleArrowVisibility(String email, String mobileNumber);

    }

    interface Presenter extends MyaPresenterInterface<View> {
        void setUserDetails(UserDataModelProvider userDataModelProvider);
    }
}
