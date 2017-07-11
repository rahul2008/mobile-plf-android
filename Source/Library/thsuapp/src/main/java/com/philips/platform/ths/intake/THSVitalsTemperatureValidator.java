package com.philips.platform.ths.intake;

import com.philips.platform.uid.view.widget.InputValidationLayout;

public class THSVitalsTemperatureValidator implements InputValidationLayout.Validator{
    @Override
    public boolean validate(CharSequence charSequence) {
        if(charSequence == null || charSequence.toString().isEmpty()){
            return false;
        }
        Double temperature = Double.parseDouble(charSequence.toString());
        return temperature > 0 && temperature < 120;
    }
}
