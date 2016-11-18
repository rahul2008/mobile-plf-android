/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.uit.components.navigation;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

public class NavigationbarFragment extends Fragment {

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(com.philips.platform.uit.test.R.menu.notificationbar, menu);
    }
}
