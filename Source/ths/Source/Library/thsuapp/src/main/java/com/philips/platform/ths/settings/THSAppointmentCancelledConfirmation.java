package com.philips.platform.ths.settings;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.welcome.THSWelcomeFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.THS_APPOINTMENT_CANCEL_CONFIRMATION;



/**
 * Created by philips on 23/03/18.
 */

public class THSAppointmentCancelledConfirmation extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSAppointmentCancelledConfirmation.class.getSimpleName();
    Label mPhoneNumber;
    Button goBackToStart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_appointment_cancelled, container, false);
        mPhoneNumber = (Label) view.findViewById(R.id.ths_appointment_cancelled_help_phone);
        mPhoneNumber.setOnClickListener(this);
        goBackToStart = (Button) view.findViewById(R.id.ths_appointment_cancelled_button);
        goBackToStart.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBarListener actionBarListener = getActionBarListener();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(getString(R.string.ths_appointments), true);
        }
        THSTagUtils.doTrackPageWithInfo(THS_APPOINTMENT_CANCEL_CONFIRMATION,null,null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ths_customer_support_phone_number_id) {
            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + mPhoneNumber.getText().toString()));
            startActivity(intent);
        } else if (view.getId() == R.id.ths_appointment_cancelled_button) {

            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(THSWelcomeFragment.TAG);
            if (fragment != null && fragment instanceof THSWelcomeFragment) {
                fragmentManager.popBackStack(THSWelcomeFragment.TAG, 0);
            }else {
                popSelfBeforeTransition();
                THSWelcomeFragment thsWelcomeFragment = new THSWelcomeFragment();
                addFragment(thsWelcomeFragment, THSWelcomeFragment.TAG, null, false);
            }
        }

    }

    @Override
    public boolean handleBackEvent() {
        return true;
    }
}
