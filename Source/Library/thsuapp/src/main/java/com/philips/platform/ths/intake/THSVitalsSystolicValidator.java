package com.philips.platform.ths.intake;


import com.philips.platform.uid.view.widget.InputValidationLayout;

public class THSVitalsSystolicValidator implements InputValidationLayout.Validator {

    @Override
    public boolean validate(CharSequence charSequence) {
        if(charSequence == null || charSequence.toString().isEmpty()){
            return false;
        }

        int systolicValue = Integer.parseInt(charSequence.toString());
        return systolicValue > 0 && systolicValue < 250;
    }
}
