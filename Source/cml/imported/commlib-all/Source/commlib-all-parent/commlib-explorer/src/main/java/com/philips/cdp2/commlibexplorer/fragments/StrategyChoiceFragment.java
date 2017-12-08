/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.fragments;

import android.util.Log;

import com.philips.cdp2.commlibexplorer.CommlibExplorerApp;
import com.philips.cdp2.commlibexplorer.R;
import com.philips.cdp2.commlibexplorer.presenters.CommStrategyPresenter;
import com.philips.cdp2.commlibexplorer.strategy.CommStrategy;

import java.util.List;

import nl.rwslinkman.presentable.Presenter;
import nl.rwslinkman.presentable.interaction.PresentableItemClickListener;

public class StrategyChoiceFragment extends CommLibExplorerAppFragment<CommStrategy> implements PresentableItemClickListener<CommStrategy> {
    private static final String TAG = "StrategyChoiceFragment";

    public StrategyChoiceFragment() {
        // NOP
    }

    public static StrategyChoiceFragment newInstance() {
        return new StrategyChoiceFragment();
    }

    @Override
    public void onResume() {
        super.onResume();

        setListItemClickListener(this);

        titleView.setText(R.string.strategy_choice_title_message);
        subtitleView.setText(R.string.strategy_choice_subtitle);

        List<CommStrategy> strategyList = ((CommlibExplorerApp) getActivity().getApplication()).getAvailableContexts();
        updateList(strategyList);
    }

    @Override
    Presenter getListPresenter() {
        return new CommStrategyPresenter();
    }

    @Override
    public void onItemClicked(CommStrategy item) {
        Log.d(TAG, "onItemClicked: Clicked on " + item.getType().value());
        navigateToDeviceDiscovery(item);
    }

    private void navigateToDeviceDiscovery(CommStrategy strategy) {
        getConnectionService().createCommCentral(strategy);

        DeviceDiscoveryFragment fragment = DeviceDiscoveryFragment.newInstance();
        getMainActivity().navigateTo(fragment);
    }
}
