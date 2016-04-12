package com.philips.cdp.digitalcare.faq;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.digitalcare.R;

/**
 * Created by naveen@philips.com on 05-Apr-16.
 */
public class FAQCustomView {

    private Context mContext = null;

    public FAQCustomView(Context context) {
        this.mContext = context;
    }

    protected View init() {
        ScrollView scrollView = new ScrollView(mContext);
        RelativeLayout.LayoutParams scrollViewLayoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        scrollView.setLayoutParams(scrollViewLayoutParams);

        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams containerparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(containerparams);

        View parent = getQuestionTypeView("This is the FQ category");
        View child1 = getQuestionView("First Question");
        View child2 = getQuestionView("Second Question");

        container.addView(parent);
        container.addView(child1);
        container.addView(child2);


        scrollView.addView(container);

        return scrollView;

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

    private View getQuestionTypeView(String questionType) {
        RelativeLayout questionTypeView = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams questionTypeParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView headerText = new TextView(mContext);
        headerText.setText(questionType);
        RelativeLayout.LayoutParams headerTextParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerTextParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        headerTextParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView arrowImage = new ImageView(mContext);
        RelativeLayout.LayoutParams arrowImageParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        arrowImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        arrowImageParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        arrowImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.uikit_up_arrow, null));


        questionTypeView.setLayoutParams(questionTypeParams);
        headerText.setLayoutParams(headerTextParams);
        arrowImage.setLayoutParams(arrowImageParams);

        questionTypeView.addView(headerText);
        questionTypeView.addView(arrowImage);

        return questionTypeView;
    }
}
