
/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.ths.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public interface THSBaseView {
    void finishActivityAffinity();
    FragmentActivity getFragmentActivity();
    int getContainerID();
    void addFragment(THSBaseFragment fragment, String fragmentTag, Bundle bundle, boolean isReplace);
    void popFragmentByTag(String fragmentTag,int flag);
}
