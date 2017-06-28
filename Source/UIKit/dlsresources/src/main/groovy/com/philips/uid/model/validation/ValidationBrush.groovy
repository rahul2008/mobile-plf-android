package com.philips.uid.model.validation

import com.philips.uid.model.brush.Brush
import com.philips.uid.model.brush.BrushValue

class ValidationBrush {
    def brushName
    Map<String, BrushValue> validationBrushMap = new HashMap<>()
}
