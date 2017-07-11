package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.insurance.THSConfirmationFragment;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.R;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

public class THSFollowUpFragment extends THSBaseFragment implements  View.OnClickListener {
    public static final String TAG = THSFollowUpFragment.class.getSimpleName();
    private EditText mPhoneNumberEditText;
    private CheckBox mNoppAgreeCheckBox;
    private Button mFollowUpContiueButton;
    private THSBasePresenter mTHSFollowUpPresenter;
    private ActionBarListener actionBarListener;
    private String updatedPhone;
    private Label nopp_label;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.intake_follow_up, container, false);
        mTHSFollowUpPresenter = new THSFollowUpPresenter(this);
        mPhoneNumberEditText = (EditText)view.findViewById(R.id.pth_intake_follow_up_phone_number);
        mFollowUpContiueButton = (Button)view.findViewById(R.id.pth_intake_follow_up_continue_button);
        mFollowUpContiueButton.setOnClickListener(this);
        mNoppAgreeCheckBox = (CheckBox)view.findViewById(R.id.pth_intake_follow_up_nopp_agree_check_box);
        mNoppAgreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {
                    mFollowUpContiueButton.setEnabled(true);
                }else{
                    mFollowUpContiueButton.setEnabled(false);
                }
            }
        });
        nopp_label = (Label)view.findViewById(R.id.pth_intake_follow_up_i_agree_link_text);
        nopp_label.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
       THSConsumer THSConsumer = THSManager.getInstance().getPTHConsumer();
        if(null!= THSConsumer && null!= THSConsumer.getConsumer() &&  null!= THSConsumer.getConsumer().getPhone() &&  !THSConsumer.getConsumer().getPhone().isEmpty()){
            mPhoneNumberEditText.setText(THSConsumer.getConsumer().getPhone());
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
            addFragment(new THSConfirmationFragment(),THSConfirmationFragment.TAG,null);
           /* updatedPhone= mPhoneNumberEditText.getText().toString().trim();
            if(null!=updatedPhone && !updatedPhone.isEmpty()) {
                ((THSFollowUpPresenter) mTHSFollowUpPresenter).updateConsumer(updatedPhone);
            }*/
        }else if(v.getId() == R.id.pth_intake_follow_up_i_agree_link_text){
            ((THSFollowUpPresenter)mTHSFollowUpPresenter).onEvent(R.id.pth_intake_follow_up_i_agree_link_text);

        }

    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }
}
