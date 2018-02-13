/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.thsdemolaunch;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;

import com.philips.cdp.registration.User;
import com.philips.platform.myapplicationlibrary.R;
import com.philips.platform.ths.appointment.THSDatePickerFragmentUtility;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSDateEnum;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.ProgressBarButton;
import com.philips.platform.uid.view.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static utility.THSDemoAppConstants.DEPENDENT;

public class THSDemoAddDependentActivity extends UIDActivity implements View.OnClickListener{

    private ThemeConfiguration themeConfiguration;
    private Toolbar toolbar;
    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mDateOfBirth;

    private RadioGroup radio_group_single_line;
    private Date mDate;
    protected EditText mSystolic;
    protected EditText mDiastolic;
    protected EditText mTemperature;
    ProgressBarButton mContinueButton;
    protected EditText mWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeConfiguration = new ThemeConfiguration(this, ColorRange.PURPLE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE);
        UIDHelper.init(themeConfiguration);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dependent_enrollment_form);
        setViews();
        setSupportActionBar(toolbar);
        UIDHelper.setTitle(this, "Add dependent");
    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon, getTheme()));
        mEditTextFirstName = findViewById(R.id.ths_demo_edit_first_name);
        mEditTextLastName = findViewById(R.id.ths_demo_edit_last_name);
        mDateOfBirth = findViewById(R.id.ths_demo_edit_dob);
        mDateOfBirth.setFocusable(false);
        mDateOfBirth.setClickable(true);
        mDateOfBirth.setOnClickListener(this);
        radio_group_single_line = findViewById(R.id.radio_group_single_line);

        mSystolic = findViewById(R.id.ths_demo_vital_info_name);
        mDiastolic = findViewById(R.id.ths_demo_diastolic_name_demo);
        mTemperature = findViewById(R.id.ths_demo_temperature_demo);
        mWeight = findViewById(R.id.ths_demo_weight_name);
        mContinueButton = findViewById(R.id.ths_demo_continue);
        mContinueButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ths_demo_edit_dob) {
            updateDate();
        }else if(id == R.id.ths_demo_continue){
            mContinueButton.showProgressIndicator();
            launchAmwellAfterAddingDependent();
        }
    }

    private void launchAmwellAfterAddingDependent() {
        User user = new User(this);

        THSConsumer dependent = new THSConsumer();
        dependent.setDob(mDate);
        dependent.setFirstName(mEditTextFirstName.getText().toString());
        dependent.setLastName(mEditTextLastName.getText().toString());
        final int checkedRadioButtonId = radio_group_single_line.getCheckedRadioButtonId();
        if(checkedRadioButtonId == R.id.ths_demo_checkbox_female) {
            dependent.setGender(com.philips.cdp.registration.ui.utils.Gender.FEMALE);
        }else {
            dependent.setGender(com.philips.cdp.registration.ui.utils.Gender.MALE);
        }
        dependent.setBloodPressureSystolic(mSystolic.getText().toString());
        dependent.setBloodPressureDiastolic(mDiastolic.getText().toString());
        dependent.setTemperature(Double.parseDouble(mTemperature.getText().toString()));
        dependent.setWeight(Integer.parseInt(mWeight.getText().toString()));
        dependent.setDependent(true);
        dependent.setHsdpUUID(user.getJanrainUUID());

        List<THSConsumer> dependants = new ArrayList<>();
        dependants.add(dependent);


        THSConsumer thsConsumer = new THSConsumer();
        thsConsumer.setDob(user.getDateOfBirth());
        thsConsumer.setEmail(user.getEmail());
        thsConsumer.setFirstName(user.getGivenName());
        thsConsumer.setGender(com.philips.cdp.registration.ui.utils.Gender.MALE);
        thsConsumer.setLastName(user.getFamilyName());
        thsConsumer.setDisplayName("Spoorti hihi");
        thsConsumer.setDependents(dependants);
        thsConsumer.setHsdpUUID(user.getHsdpUUID());
        thsConsumer.setBloodPressureSystolic("120");
        thsConsumer.setBloodPressureDiastolic("80");
        thsConsumer.setTemperature(90.0);
        thsConsumer.setWeight(56);
        thsConsumer.setDependent(false);


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(DEPENDENT,thsConsumer);
        startActivity(intent);
        mContinueButton.hideProgressIndicator();

    }

    private void updateDate() {
        final THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(this, THSDateEnum.HIDEFUTUREDATE);

        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                thsDatePickerFragmentUtility.setCalendar(year, month, day);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day);
                final Date date = new Date();
                date.setTime(calendar.getTimeInMillis());

                mDateOfBirth.setText(new SimpleDateFormat(THSConstants.DATE_FORMATTER, Locale.getDefault()).
                        format(date));
                mDate = date;

            }
        };
        thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
    }
}
