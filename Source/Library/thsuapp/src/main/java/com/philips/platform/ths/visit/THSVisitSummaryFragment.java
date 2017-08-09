package com.philips.platform.ths.visit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

/**
 * Created by philips on 8/4/17.
 */

public class THSVisitSummaryFragment extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSVisitSummaryFragment.class.getSimpleName();

    private ActionBarListener actionBarListener;
    THSVisitSummaryPresenter mTHSVisitSummaryPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_visit_summary, container, false);


        // hiding unused components
        view.findViewById(R.id.isAvailableLayout).setVisibility(View.GONE);
        view.findViewById(R.id.ps_edit_consumer_shipping_address).setVisibility(View.GONE);
        view.findViewById(R.id.ps_edit_pharmacy).setVisibility(View.GONE);

        Label prescriptionLabel = (Label) view.findViewById(R.id.ps_prescription_sent_label);
        prescriptionLabel.setText("Your prescription was sent to");

        mTHSVisitSummaryPresenter = new THSVisitSummaryPresenter(this);
        mTHSVisitSummaryPresenter.fetchVisitSummary();
        return  view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Thank you", true);
        }

    }


    @Override
    public int getContainerID() {
        return ((ViewGroup) getView().getParent()).getId();
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
