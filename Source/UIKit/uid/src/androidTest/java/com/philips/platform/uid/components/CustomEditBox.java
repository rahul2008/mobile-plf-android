package com.philips.platform.uid.components;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.philips.platform.uid.view.widget.EditText;

import java.lang.reflect.Field;

public class CustomEditBox extends EditText {
    public CustomEditBox(final Context context) {
        super(context);
    }

    public CustomEditBox(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditBox(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Editable getText() {
        if (super.getEditableText() != null) {
            return super.getText();
        } else {
            try {
                TextView textView = this;
                Field f = textView.getClass().getDeclaredField("mText");
                f.setAccessible(true);
                String text = (String) f.get(textView);
                return Editable.Factory.getInstance().newEditable(text);
            } catch (Exception e) {
            }
        }
        return null;
    }
}
