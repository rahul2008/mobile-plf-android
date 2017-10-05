package com.philips.cdp2.ews.view;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.view.dialog.CancelDialogFragment;

public class BaseFragment extends Fragment {

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.ews_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.skip_setup) {
            handleCancelButtonClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void handleCancelButtonClicked() {
        new CancelDialogFragment().show(getChildFragmentManager(), "cancelDialog");
    }

}
