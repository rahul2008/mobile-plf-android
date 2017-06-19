
/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.amwelluapp.base;

import android.support.v4.app.FragmentActivity;

public interface UIBaseView {
    void finishActivityAffinity();
    FragmentActivity getFragmentActivity();
    int getContainerID();
}
