package com.philips.platform.ccdemouapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.uikit.customviews.PuiEditText;
import com.philips.platform.ccdemouapp.adapter.Listener;
import com.philips.platform.ccdemouapplibrary.R;

import java.util.ArrayList;

/**
 * Created by 310190678 on 20-Jan-16.
 */
public class CustomDialog extends Dialog {

    private Context mContext = null;
    private PuiEditText mCtn = null;
    private PuiEditText mCategoty = null;
    private PuiEditText mCatalog = null;
    private Button mButton = null;
    private ArrayList<String> mList = null;
    private Listener mListner = null;

    public CustomDialog(Context context, ArrayList<String> list, Listener listener) {
        super(context);
        this.mContext = context;
        mList = list;
        mListner = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogview);
        initUI();
        initListeners();


    }

    private void initListeners() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product = null;
                if (mCtn.getEditText().getText() != null)
                    product = mCtn.getEditText().getText().toString().trim();
                mList.add(product);
                mListner.updateList(mList);

                dismiss();
            }
        });
    }

    private void initUI() {
        mCtn = (PuiEditText) findViewById(R.id.ctn_editText);
        mCategoty = (PuiEditText) findViewById(R.id.category_editText);
        mCatalog = (PuiEditText) findViewById(R.id.catalog_editText);
        mButton = (Button) findViewById(R.id.dialog_submit);
    }
}
