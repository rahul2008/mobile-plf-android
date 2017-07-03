package com.philips.amwelluapp.intake;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.visit.Topic;
import com.americanwell.sdk.entity.visit.VisitContext;
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
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class PTHSymptomsFragment extends PTHBaseFragment implements BackEventListener, View.OnClickListener {
    public static final String TAG = PTHSymptomsFragment.class.getSimpleName();
    PTHSymptomsPresenter mPTHSymptomsPresenter;
    PTHConsumer consumer;
    PTHProviderInfo providerInfo;
    LinearLayout topicLayout;
    FloatingActionButton floatingActionButton;
    Button mContinue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.pth_symptoms, container, false);
        Bundle bundle = getArguments();
        consumer = (PTHConsumer) bundle.getParcelable("Consumer");
        providerInfo = (PTHProviderInfo) bundle.getParcelable("providerInfo");
        topicLayout = (LinearLayout) view.findViewById(R.id.checkbox_container);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_button);
        mContinue = (Button) view.findViewById(R.id.continue_btn);
        mContinue.setOnClickListener(this);

        Typeface face= TypefaceUtils.load(getActivity().getAssets(),"fonts/pui_icon.ttf");
        FontIconDrawable fontIconDrawable = new FontIconDrawable(getContext(),"\uE612",face).actionBarSize().color(Color.BLACK);

        floatingActionButton.setImageDrawable(fontIconDrawable);

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
    public void onDestroyView() {
        super.onDestroyView();
        consumer = null;
        providerInfo = null;
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

    public void addTopicsToView(PTHVisitContext visitContext){
        List<Topic> topics = visitContext.getTopics();
        for (final Topic topic:topics
             ) {
            CheckBox checkBox = new CheckBox(getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(layoutParams);
            checkBox.setEnabled(true);
            checkBox.setText(topic.getTitle());
            topicLayout.addView(checkBox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    topic.setSelected(true);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.continue_btn) {
            mPTHSymptomsPresenter.onEvent(R.id.continue_btn);
        }
    }
}
