package com.philips.platform.ths.intake;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.visit.Topic;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;

import java.util.List;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class THSSymptomsFragment extends THSBaseFragment implements BackEventListener, View.OnClickListener {
    public static final String TAG = THSSymptomsFragment.class.getSimpleName();
    THSSymptomsPresenter mTHSSymptomsPresenter;
    THSProviderInfo providerInfo;
    LinearLayout topicLayout;
    FloatingActionButton floatingActionButton;
    Button mContinue;
    Typeface face;
    RelativeLayout mRelativeLayout;

    //TODO: Spoorti - check null condition for the bundle Arguments
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_symptoms, container, false);
        Bundle bundle = getArguments();
        providerInfo = bundle.getParcelable(THSConstants.THS_PROVIDER_INFO);
        topicLayout = (LinearLayout) view.findViewById(R.id.checkbox_container);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_button);
        mContinue = (Button) view.findViewById(R.id.continue_btn);
        mContinue.setOnClickListener(this);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.symptoms_container);

        face = TypefaceUtils.load(getActivity().getAssets(), "fonts/pui_icon.ttf");
        FontIconDrawable fontIconDrawable = new FontIconDrawable(getContext(), "\uE612", face).actionBarSize().color(Color.BLACK);

        floatingActionButton.setImageDrawable(fontIconDrawable);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTHSSymptomsPresenter = new THSSymptomsPresenter(this, providerInfo);
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
        if (THSManager.getInstance().getPthVisitContext() == null) {
            createCustomProgressBar(mRelativeLayout, MEDIUM);
            mContinue.setEnabled(false);
            mTHSSymptomsPresenter.getVisitContext();
        }else {
            mContinue.setEnabled(true);
            addTopicsToView(THSManager.getInstance().getPthVisitContext());
        }
    }

    //TODO: SPOORTI - crashing when back is pressed
    public void addTopicsToView(THSVisitContext visitContext) {
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
            mTHSSymptomsPresenter.onEvent(R.id.continue_btn);
        }
    }
}
