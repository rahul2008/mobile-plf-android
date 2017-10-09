/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.practice.THSPractice;
import com.philips.platform.ths.practice.THSPracticeFragment;

public class THSDependentPresenter implements THSBasePresenter{
    THSDependantHistoryFragment mThsDependantHistoryFragment;

    public THSDependentPresenter(THSDependantHistoryFragment thsDependantHistoryFragment) {
        mThsDependantHistoryFragment = thsDependantHistoryFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }

    public void showDependentsPractices(THSConsumer thsConsumer) {
        mThsDependantHistoryFragment.addFragment(new THSPracticeFragment(), THSPracticeFragment.TAG,null,false);
    }
}
