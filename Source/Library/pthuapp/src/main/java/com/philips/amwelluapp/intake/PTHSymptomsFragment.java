package com.philips.amwelluapp.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.visit.Topic;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.practice.OnPracticeItemClickListener;
import com.philips.amwelluapp.practice.PTHPractice;
import com.philips.amwelluapp.practice.PTHPracticePresenter;
import com.philips.amwelluapp.practice.PracticeRecyclerViewAdapter;
import com.philips.amwelluapp.providerslist.PTHProviderInfo;
import com.philips.amwelluapp.providerslist.PTHProvidersListFragment;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.CheckBox;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class PTHSymptomsFragment extends PTHBaseFragment implements BackEventListener {
    public static final String TAG = PTHSymptomsFragment.class.getSimpleName();
    PTHSymptomsPresenter mPTHSymptomsPresenter;
    PTHConsumer consumer;
    PTHProviderInfo providerInfo;
    LinearLayout topicLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.pth_symptoms, container, false);
        Bundle bundle = getArguments();
        consumer = (PTHConsumer) bundle.getSerializable("Consumer");
        providerInfo = (PTHProviderInfo) bundle.getSerializable("providerInfo");
        topicLayout = (LinearLayout) view.findViewById(R.id.checkbox_container);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPTHSymptomsPresenter = new PTHSymptomsPresenter(this,consumer,providerInfo);
        if(null != getActionBarListener()){
            getActionBarListener().updateActionBar(getString(R.string.pth_prepare_your_visit),true);
        }
        getVisistContext();
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getVisistContext() {
        mPTHSymptomsPresenter.getVisitContext();
    }

    public void addTopicsToView(List<Topic> topics){
        for (Topic topic:topics
             ) {
            CheckBox checkBox = new CheckBox(getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(layoutParams);
            checkBox.setEnabled(true);
            checkBox.setText(topic.getTitle());
            topicLayout.addView(checkBox);
        }
    }
}
