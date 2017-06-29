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
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.providerslist.PTHProvidersListFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBar;

public class PTHPracticeFragment extends PTHBaseFragment implements BackEventListener {

    //TODO: Review Comment - Spoorti - Can we make mConsumer as local variable instead of global?
    private Consumer mConsumer;
    private PTHBasePresenter mPresenter;
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
        //TODO: Review Comment - Spoorti - Do not hard code the String. Take it from Strings.xml
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

    //TODO: Review Comment - Spoorti - finish the activity here
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

    //TODO: Review Comment - Spoorti - Not sure why Presenter is created inside setConsumer
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
                //TODO: Review Comment - Spoorti - Use PTHBaseView.addFragment for doing the below operation
                //TODO: Review comment - Rakesh - Please move the action to presenter and the addition of fragment will be called from presenter with a hook(base view)
                getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), providerListFragment,"ProviderListFragment").addToBackStack(null).commit();
            }
        });


    }
}
