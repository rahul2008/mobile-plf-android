package com.philips.platform.ths.intake;

import com.philips.platform.uid.view.widget.InputValidationLayout;

public class THSVitalsWeightValidator implements InputValidationLayout.Validator{
    @Override
    public boolean validate(CharSequence charSequence) {
        if(charSequence == null || charSequence.toString().isEmpty()){
            return false;
        }
        int weight = Integer.parseInt(charSequence.toString());
        return weight > 0 && weight < 500;
    }
}
