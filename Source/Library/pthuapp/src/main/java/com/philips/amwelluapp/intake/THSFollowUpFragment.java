package com.philips.amwelluapp.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.utility.PTHManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

public class THSFollowUpFragment extends PTHBaseFragment implements  View.OnClickListener {
    public static final String TAG = THSFollowUpFragment.class.getSimpleName();
    private EditText mPhoneNumberEditText;
    private CheckBox mNoppAgreeCheckBox;
    private Button mFollowUpContiueButton;
    private PTHBasePresenter mTHSFollowUpPresenter;
    private ActionBarListener actionBarListener;
    private String updatedPhone;
    private Label nopp_label;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.intake_follow_up, container, false);
        mTHSFollowUpPresenter = new THSFollowUpPresenter(this);
        mPhoneNumberEditText = (EditText)view.findViewById(R.id.pth_intake_follow_up_phone_number);

        mNoppAgreeCheckBox = (CheckBox)view.findViewById(R.id.pth_intake_follow_up_nopp_agree_check_box);
        nopp_label = (Label)view.findViewById(R.id.pth_intake_follow_up_i_agree_link_text);
        nopp_label.setOnClickListener(this);
        mFollowUpContiueButton = (Button)view.findViewById(R.id.pth_intake_follow_up_continue_button);
        mFollowUpContiueButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
       PTHConsumer pthConsumer = PTHManager.getInstance().getPTHConsumer();
        if(null!=pthConsumer && null!=pthConsumer.getConsumer() &&  null!=pthConsumer.getConsumer().getPhone() &&  !pthConsumer.getConsumer().getPhone().isEmpty()){
            mPhoneNumberEditText.setText(pthConsumer.getConsumer().getPhone());
        }
    }




    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.pth_intake_follow_up_continue_button){
            updatedPhone= mPhoneNumberEditText.getText().toString().trim();
            if(null!=updatedPhone && !updatedPhone.isEmpty()) {
                ((THSFollowUpPresenter) mTHSFollowUpPresenter).updateConsumer(updatedPhone);
            }
        }else if(v.getId() == R.id.pth_intake_follow_up_i_agree_link_text){
            ((THSFollowUpPresenter)mTHSFollowUpPresenter).onEvent(R.id.pth_intake_follow_up_i_agree_link_text);

        }

    }
}
