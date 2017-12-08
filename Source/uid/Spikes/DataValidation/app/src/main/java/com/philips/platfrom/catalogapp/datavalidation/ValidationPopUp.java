package com.philips.platfrom.catalogapp.datavalidation;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

public class ValidationPopUp extends PopupWindow {
    public ValidationPopUp(final Context context) {
        super(context);
        View view = View.inflate(context, R.layout.validation_pop_up_layout, null);
        view.findViewById(R.id.popup_error_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dismiss();
            }
        });
        setContentView(view);
        setHeight(100);
        setWidth(300);
    }

    public void showError(View anchorView) {
        showAtLocation(anchorView, Gravity.NO_GRAVITY, anchorView.getLeft(), anchorView.getTop() +100);
    }
}