package com.philips.platform.ths.cost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;

/**
 * Created by philips on 7/19/17.
 */

public class THSCostSummaryFragment extends THSBaseFragment {
    public static final String TAG = THSCostSummaryFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    private THSCostSummaryPresenter mPresenter;
    private RelativeLayout mProgressbarContainer;
    Label costBigLabel;
    Label costSmallLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_cost_summary, container, false);
        mPresenter = new THSCostSummaryPresenter(this);
        costBigLabel = (Label) view.findViewById(R.id.ths_cost_summary_cost_big_label);
        costSmallLabel = (Label) view.findViewById(R.id.ths_cost_summary_cost_small_label);
        mProgressbarContainer=(RelativeLayout) view.findViewById(R.id.ths_cost_summary_relativelayout);
        mPresenter.createVisit();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createCustomProgressBar(mProgressbarContainer, MEDIUM);
        actionBarListener = getActionBarListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Cost", true);
        }
    }
}
