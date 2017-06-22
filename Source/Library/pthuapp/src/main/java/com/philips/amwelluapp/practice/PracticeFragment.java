package com.philips.amwelluapp.practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.UIBasePresenter;
import com.philips.amwelluapp.providerslist.PTHProvidersListFragment;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Label;

public class PracticeFragment extends PTHBaseFragment implements BackEventListener {

    private Consumer mConsumer;
    private UIBasePresenter mPresenter;
    private Label mTitle;
    private RecyclerView mPracticeRecyclerView;
    private PracticeRecyclerViewAdapter mPracticeRecyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.practice, container, false);
        mTitle = (Label) view.findViewById(R.id.pth_id_practice_label);
        mPracticeRecyclerView = (RecyclerView)view.findViewById(R.id.pth_recycler_view_practice);
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
        ((PTHPracticePresenter)mPresenter).fetchPractices();

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

        mPracticeRecyclerViewAdapter = new PracticeRecyclerViewAdapter(getActivity(), practices);
        mPracticeRecyclerView.setAdapter(mPracticeRecyclerViewAdapter);
        mPracticeRecyclerViewAdapter.setmOnPracticeItemClickListener(new OnPracticeItemClickListener() {
            @Override
            public void onItemClick(Practice practice) {
                PTHProvidersListFragment providerListFragment = new PTHProvidersListFragment();
                providerListFragment.setPracticeAndConsumer(practice,mConsumer);
                getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), providerListFragment,"ProviderListFragment").addToBackStack(null).commit();
                Toast.makeText(getFragmentActivity(),practice.getName()+ "Practice clicked",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
