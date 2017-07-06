
/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.amwelluapp.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public interface PTHBaseView {
    void finishActivityAffinity();
    FragmentActivity getFragmentActivity();
    int getContainerID();
    void addFragment(PTHBaseFragment fragment, String fragmentTag, Bundle bundle);
}
