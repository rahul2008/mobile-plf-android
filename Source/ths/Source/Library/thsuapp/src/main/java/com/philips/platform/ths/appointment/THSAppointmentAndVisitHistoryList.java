package com.philips.platform.ths.appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.dependantregistration.THSDependantHistoryFragment;
import com.philips.platform.ths.settings.THSScheduledVisitsFragment;
import com.philips.platform.ths.settings.THSVisitHistoryFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Label;

public class THSAppointmentAndVisitHistoryList extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSAppointmentAndVisitHistoryList.class.getSimpleName();

    private RelativeLayout mRelativeLayoutAppointments;
    private RelativeLayout mRelativeLayoutVisitHostory;
    private Label mAppointmentLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_appointment_and_visit_history_list, container, false);
        mRelativeLayoutAppointments = (RelativeLayout) view.findViewById(R.id.appointments);
        mRelativeLayoutVisitHostory = (RelativeLayout) view.findViewById(R.id.visit_history);
        mAppointmentLabel = (Label) view.findViewById(R.id.pth_practice_name);
        mAppointmentLabel.setText(getString(R.string.ths_upcoming_appointments));
        mRelativeLayoutVisitHostory.setOnClickListener(this);
        mRelativeLayoutAppointments.setOnClickListener(this);

        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_appointments), true);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        final Context context = THSAppointmentAndVisitHistoryList.this.getContext();
        Bundle bundle = new Bundle();
        if (i == R.id.appointments) {
            bundle.putInt(THSConstants.THS_LAUNCH_INPUT, THSConstants.THS_SCHEDULED_VISITS);
            addFragment(new THSScheduledVisitsFragment(), THSScheduledVisitsFragment.TAG, null, false);
        } else if (i == R.id.visit_history) {
            bundle.putInt(THSConstants.THS_LAUNCH_INPUT, THSConstants.THS_VISITS_HISTORY);
            if (THSManager.getInstance().getThsParentConsumer(context).getDependents() != null && THSManager.getInstance().getThsParentConsumer(context).getDependents().size() > 0) {
                addFragment(new THSDependantHistoryFragment(), THSDependantHistoryFragment.TAG, bundle, false);
            } else {
                addFragment(new THSVisitHistoryFragment(), THSScheduledVisitsFragment.TAG, null, false);
            }
        }
    }
}
