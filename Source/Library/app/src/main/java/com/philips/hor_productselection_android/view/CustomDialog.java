package com.philips.hor_productselection_android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.philips.hor_productselection_android.R;
import com.philips.hor_productselection_android.adapter.CtnListViewListener;

import java.util.ArrayList;

/**
 * Created by 310190678 on 20-Jan-16.
 */
public class CustomDialog extends Dialog {

    private Context mContext = null;
    private EditText mCtn = null;
    private EditText mCategoty = null;
    private EditText mCatalog = null;
    private Button mButton = null;
    private ArrayList<String> mList = null;
    private CtnListViewListener mListner = null;

    public CustomDialog(Context context, ArrayList<String> list, CtnListViewListener ctnListViewListener) {
        super(context);
        this.mContext = context;
        mList = list;
        mListner = ctnListViewListener;
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
                if (mCtn.getText() != null)
                    product = mCtn.getText().toString().trim();


                mList.add(product);
                mListner.updateList(mList);

                dismiss();
            }
        });
    }

    private void initUI() {
        mCtn = (EditText) findViewById(R.id.ctn_editText);
        mCategoty = (EditText) findViewById(R.id.category_editText);
        mCatalog = (EditText) findViewById(R.id.catalog_editText);
        mButton = (Button) findViewById(R.id.dialog_submit);
    }
}
