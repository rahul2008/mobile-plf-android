
package com.philips.cdp.registration.coppa.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.utils.RegCoppaUtility;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaHelper;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;

public class ParentalApprovalFragment extends RegistrationCoppaBaseFragment implements OnClickListener {

    private LinearLayout mLlConfirmApprovalParent;
    private TextView tvConfirmApprovalDesc;
    private Button mBtnAgree;
    private Button mBtnDisAgree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onCreateView");

        View view = inflater.inflate(R.layout.fragment_reg_coppa_parental_approval, null);
        initUi(view);
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mLlConfirmApprovalParent, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUi(View view) {
        consumeTouch(view);
        mLlConfirmApprovalParent = (LinearLayout) view.findViewById(R.id.ll_reg_confirm_root_container);
        tvConfirmApprovalDesc = (TextView)view.findViewById(R.id.tv_reg_confirm_approval_details);
        mBtnAgree = (Button) view.findViewById(R.id.reg_btn_agree);
        mBtnAgree.setOnClickListener(this);
        mBtnDisAgree = (Button) view.findViewById(R.id.reg_btn_dis_agree);
        mBtnDisAgree.setOnClickListener(this);
        RegCoppaUtility.linkifyTermAndPolicy(tvConfirmApprovalDesc,getActivity(),privacyLinkClick);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.reg_btn_agree) {
            Toast.makeText(getRegistrationFragment().getParentActivity().getApplicationContext(), "Agree", Toast.LENGTH_SHORT).show();
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(getActivity());
            }
        } else if (id == R.id.reg_btn_dis_agree) {
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(getActivity());
            }
        }


    }

    @Override
    public int getTitleResourceId() {
        return R.string.Coppa_Consent_Approval_Screen_Title_txt;
    }

    private ClickableSpan privacyLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            RegistrationHelper.getInstance().getUserRegistrationListener().notifyOnPrivacyPolicyClickEventOccurred(getActivity());
        }
    };

}
