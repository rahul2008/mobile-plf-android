package com.philips.amwelluapp.practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.UIBasePresenter;
import com.philips.amwelluapp.providerslist.PTHProvidersListFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBar;

public class PTHPracticeFragment extends PTHBaseFragment implements BackEventListener {

    private Consumer mConsumer;
    private UIBasePresenter mPresenter;
    private Label mTitle;
    private RecyclerView mPracticeRecyclerView;
    private PracticeRecyclerViewAdapter mPracticeRecyclerViewAdapter;
    private ActionBarListener actionBarListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.practice, container, false);
        mTitle = (Label) view.findViewById(R.id.pth_id_practice_label);
        mPracticeRecyclerView = (RecyclerView)view.findViewById(R.id.pth_recycler_view_practice);
        //mPTHBaseFragmentProgressBar = (ProgressBar)view.findViewById(R.id.pth_id_practice_progress_bar);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTitle.setText("To start a consult, pick a subject");
        mPracticeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(null!=((PTHPracticePresenter)mPresenter)) {
            showProgressBar();
            ((PTHPracticePresenter) mPresenter).fetchPractices();
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
        return false;
    }

    @Override
    public void finishActivityAffinity() {

    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }

    public void setConsumer(Consumer consumer){
        mConsumer=consumer;
        mPresenter = new PTHPracticePresenter(this,consumer);
    }

    public void showPracticeList(PTHPractice practices){
        hideProgressBar();
        mPracticeRecyclerViewAdapter = new PracticeRecyclerViewAdapter(getActivity(), practices);
        mPracticeRecyclerView.setAdapter(mPracticeRecyclerViewAdapter);
        mPracticeRecyclerViewAdapter.setmOnPracticeItemClickListener(new OnPracticeItemClickListener() {
            @Override
            public void onItemClick(Practice practice) {
                PTHProvidersListFragment providerListFragment = new PTHProvidersListFragment();
                providerListFragment.setPracticeAndConsumer(practice,mConsumer);
                providerListFragment.setActionBarListener(getActionBarListener());
                getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), providerListFragment,"ProviderListFragment").addToBackStack(null).commit();
            }
        });


    }
}
