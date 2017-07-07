package com.philips.amwelluapp.intake;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.americanwell.sdk.entity.visit.Topic;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.providerslist.PTHProviderInfo;
import com.philips.amwelluapp.utility.PTHConstants;
import com.philips.amwelluapp.utility.PTHManager;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;

import java.util.List;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class PTHSymptomsFragment extends PTHBaseFragment implements BackEventListener, View.OnClickListener {
    public static final String TAG = PTHSymptomsFragment.class.getSimpleName();
    PTHSymptomsPresenter mPTHSymptomsPresenter;
    PTHProviderInfo providerInfo;
    LinearLayout topicLayout;
    FloatingActionButton floatingActionButton;
    Button mContinue;
    Typeface face;

    //TODO: Spoorti - check null condition for the bundle Arguments
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.pth_symptoms, container, false);
        Bundle bundle = getArguments();
        providerInfo = bundle.getParcelable(PTHConstants.THS_PROVIDER_INFO);
        topicLayout = (LinearLayout) view.findViewById(R.id.checkbox_container);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_button);
        mContinue = (Button) view.findViewById(R.id.continue_btn);
        mContinue.setOnClickListener(this);

        face = TypefaceUtils.load(getActivity().getAssets(), "fonts/pui_icon.ttf");
        FontIconDrawable fontIconDrawable = new FontIconDrawable(getContext(), "\uE612", face).actionBarSize().color(Color.BLACK);

        floatingActionButton.setImageDrawable(fontIconDrawable);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPTHSymptomsPresenter = new PTHSymptomsPresenter(this, providerInfo);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.pth_prepare_your_visit), true);
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
        if (PTHManager.getInstance().getPthVisitContext() == null) {
            mContinue.setEnabled(false);
            mPTHSymptomsPresenter.getVisitContext();
        }else {
            mContinue.setEnabled(true);
            addTopicsToView(PTHManager.getInstance().getPthVisitContext());
        }
    }

    //TODO: SPOORTI - crashing when back is pressed
    public void addTopicsToView(PTHVisitContext visitContext) {
        if(getContext()!=null) {
            List<Topic> topics = visitContext.getTopics();
            for (final Topic topic : topics
                    ) {
                CheckBox checkBox = new CheckBox(getContext());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                checkBox.setLayoutParams(layoutParams);
                checkBox.setEnabled(true);
                checkBox.setText(topic.getTitle());
                if(topic.isSelected()){
                    checkBox.setChecked(true);
                }
                topicLayout.addView(checkBox);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        topic.setSelected(true);
                    }
                });
            }
        }
        mContinue.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.continue_btn) {
            mPTHSymptomsPresenter.onEvent(R.id.continue_btn);
        }
    }
}
