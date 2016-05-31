/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.servertime;

import android.content.Intent;
import android.test.mock.MockContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310202337 on 1/29/2016.
 */

public class TestContext extends MockContext{

    private List<Intent> mReceivedIntents = new ArrayList<Intent>();

    @Override
    public String getPackageName()
    {
        return "com.mypackage.test";
    }

    @Override
    public void startActivity(Intent xiIntent)
    {
        mReceivedIntents.add(xiIntent);
    }

    public List<Intent> getReceivedIntents()
    {
        return mReceivedIntents;
    }

}