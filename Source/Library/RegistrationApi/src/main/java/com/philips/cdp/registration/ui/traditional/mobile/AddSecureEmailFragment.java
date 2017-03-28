package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.registration.B;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.customviews.XEmail;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.RLog;

import butterfork.Bind;
import butterfork.ButterFork;
import butterfork.OnClick;

import static com.philips.cdp.registration.app.tagging.AppTagingConstants.REGISTRATION_ACTIVATION_SMS;


public class AddSecureEmailFragment extends RegistrationBaseFragment implements OnUpdateListener {

    @Bind(B.id.btn_reg_secure_data_email)
    Button  btnRegSecureDataEmail;

    @Bind(B.id.btn_reg_secure_data_email_later)
    Button btn_regSecureDataEmailLater;

    @Bind(B.id.rl_reg_securedata_email_field)
    XEmail rlRegSecuredataEmailfield;

    Context context;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onCreateView");
        trackActionStatus(REGISTRATION_ACTIVATION_SMS,"","");
        context = getRegistrationFragment().getActivity().getApplicationContext();
        user = new User(context);
        View view = inflater.inflate(R.layout.reg_fragment_secure_email, container, false);
        ButterFork.bind(this, view);
        rlRegSecuredataEmailfield.setOnUpdateListener(this);


        return view;
    }

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_RegCreateAccount_NavTitle;
    }


    @OnClick(B.id.btn_reg_secure_data_email)
    public void addEmailButton(){

    }

    @OnClick(B.id.btn_reg_secure_data_email_later)
    public void addLaterEmailButton(){

    }

    @Override
    public void onUpdate() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                updateUiStatus();
            }
        });
    }

    private void updateUiStatus() {
        if (rlRegSecuredataEmailfield.isValidEmail()) {
            btnRegSecureDataEmail.setEnabled(true);
        } else {
            btn_regSecureDataEmailLater.setEnabled(false);
        }
    }
}
