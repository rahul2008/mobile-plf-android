package com.philips.cl.di.digitalcare.social;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.philips.cl.di.digitalcare.util.DLog;

/**
 * 
 * @author naveen@philips.com
 * @Since Mar 18, 2015 This is a common class responsible to support
 *        "Product Information Checkbox" feature as well as
 *        "Product EditText Content"feature to TWITTER Support & FACEBOOK
 *        Support functionalities.
 */
public class SocialProductDescriptions implements OnCheckedChangeListener {

	private String TAG = SocialProductDescriptions.class.getSimpleName();

	private CheckBox mCheckBox = null;
	private EditText mEditText = null;
	private String mProductInformation = "The Prduct Description from CDLS";

	public SocialProductDescriptions(EditText edittext, CheckBox checkBox) {
		DLog.d(TAG, "Constructor++");
		this.mCheckBox = checkBox;
		this.mEditText = edittext;
		intialize();
	}

	private void intialize() {
		mCheckBox.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			DLog.d(TAG, "Checked True");
			DLog.d(TAG, getEditorText().toString());
			mEditText.setText(getEditorText());

		} else {
			DLog.d(TAG, "Checked False");
			DLog.d(TAG, "" + getEditorText().toString());
			mEditText.setText(getEditorText());
		}
	}

	public String getEditorText() {
		String mContent = null, mEditorContent = mEditText.getText().toString();

		if (mCheckBox.isChecked()) {
			mContent = mProductInformation + " " + mEditorContent;
		} else {
			if (mEditorContent.contains(mProductInformation))
				mContent = mEditorContent.replace(mProductInformation, "")
						.trim();
			else
				mContent = mEditorContent;

		}
		if (mContent != null)
			mContent.trim();
		DLog.d(TAG, "Text in the Content Description" + mContent);
		return mContent;
	}

}
