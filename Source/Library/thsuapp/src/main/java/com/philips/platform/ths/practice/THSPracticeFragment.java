package com.philips.platform.ths.practice;

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
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.providerslist.THSProvidersListFragment;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Label;

public class THSPracticeFragment extends THSBaseFragment implements BackEventListener {

    public static final String TAG = THSPracticeFragment.class.getSimpleName();
    //TODO: Review Comment - Spoorti - Can we make mConsumer as local variable instead of global?
    private Consumer mConsumer;
    private THSBasePresenter mPresenter;
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
        if(null!=((THSPracticePresenter)mPresenter)) {
            showProgressBar();
            ((THSPracticePresenter) mPresenter).fetchPractices();
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
        mPresenter = new THSPracticePresenter(this,consumer);
    }

    public void showPracticeList(THSPracticeList practices){
        hideProgressBar();
        mPracticeRecyclerViewAdapter = new PracticeRecyclerViewAdapter(getActivity(), practices);
        mPracticeRecyclerView.setAdapter(mPracticeRecyclerViewAdapter);
        mPracticeRecyclerViewAdapter.setmOnPracticeItemClickListener(new OnPracticeItemClickListener() {
            @Override
            public void onItemClick(Practice practice) {
                THSProvidersListFragment providerListFragment = new THSProvidersListFragment();
                providerListFragment.setPracticeAndConsumer(practice,mConsumer);
                providerListFragment.setActionBarListener(getActionBarListener());
                //TODO: Review Comment - Spoorti - Use THSBaseView.addFragment for doing the below operation
                //TODO: Review comment - Rakesh - Please move the action to presenter and the addition of fragment will be called from presenter with a hook(base view)
                getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), providerListFragment,"ProviderListFragment").addToBackStack(null).commit();
            }
        });


    }
}
