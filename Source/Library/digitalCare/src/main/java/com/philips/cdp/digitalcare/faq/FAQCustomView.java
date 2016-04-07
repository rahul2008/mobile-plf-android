package com.philips.cdp.digitalcare.faq;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by naveen@philips.com on 05-Apr-16.
 */
public class FAQCustomView extends View {

    private Context mContext = null;

    public FAQCustomView(Context context) {
        super(context);
        this.mContext = context;
    }

    private View getQuestionView(String question) {
        RelativeLayout questionView = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams questionLayoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView questionTextView = new TextView(mContext);
        RelativeLayout.LayoutParams questionTextparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        questionTextView.setText(question);

        questionTextView.setLayoutParams(questionTextparams);
        questionView.setLayoutParams(questionLayoutParams);

        questionView.addView(questionTextView);

        return questionView;
    }

    private View getQuestionTypeView(String questionType)
    {
        RelativeLayout questionTypeView = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams questionTypeParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView headerText = new TextView(mContext);
        RelativeLayout.LayoutParams headerTextParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerTextParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        headerTextParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView arrowImage = new ImageView(mContext);
        RelativeLayout.LayoutParams arrowImageParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        arrowImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        arrowImageParams.addRule(RelativeLayout.CENTER_IN_PARENT);


        questionTypeView.setLayoutParams(questionTypeParams);
        headerText.setLayoutParams(headerTextParams);
        arrowImage.setLayoutParams(arrowImageParams);

        questionTypeView.addView(headerText);
        questionTypeView.addView(arrowImage);

        return questionTypeView;
    }
}
