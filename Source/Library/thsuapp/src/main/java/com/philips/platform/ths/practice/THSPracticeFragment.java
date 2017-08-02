package com.philips.platform.ths.practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.providerslist.THSProvidersListFragment;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Label;

public class THSPracticeFragment extends THSBaseFragment implements BackEventListener {

    public static final String TAG = THSPracticeFragment.class.getSimpleName();

    private THSPracticePresenter mPresenter;
    private Label mTitle;
    private RecyclerView mPracticeRecyclerView;
    private PracticeRecyclerViewAdapter mPracticeRecyclerViewAdapter;
    private ActionBarListener actionBarListener;
    private RelativeLayout mRealtiveLayoutPracticeContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ths_practice, container, false);
        mTitle = (Label) view.findViewById(R.id.pth_id_practice_label);
        mPracticeRecyclerView = (RecyclerView)view.findViewById(R.id.pth_recycler_view_practice);
        mRealtiveLayoutPracticeContainer = (RelativeLayout)view.findViewById(R.id.activity_main);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new THSPracticePresenter(this);
        mTitle.setText(getFragmentActivity().getResources().getString(R.string.ths_practice_pick_subject));
        mPracticeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(null!=(mPresenter)) {
            createCustomProgressBar(mRealtiveLayoutPracticeContainer,BIG);
            ( mPresenter).fetchPractices();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar("Practice screen",true);
        }
    }

    @Override
    public boolean handleBackEvent() {
        return true;
    }




    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    public void showPracticeList(THSPracticeList practices){
        hideProgressBar();
        mPracticeRecyclerViewAdapter = new PracticeRecyclerViewAdapter(getActivity(), practices);
        mPracticeRecyclerView.setAdapter(mPracticeRecyclerViewAdapter);
        mPracticeRecyclerViewAdapter.setmOnPracticeItemClickListener(new OnPracticeItemClickListener() {
            @Override
            public void onItemClick(Practice practice) {
               mPresenter.showProviderList(practice);
               }
        });


    }
}
