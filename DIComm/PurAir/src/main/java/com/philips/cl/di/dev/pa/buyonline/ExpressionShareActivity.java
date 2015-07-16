package com.philips.cl.di.dev.pa.buyonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.WBShareActivity;
import com.philips.cl.di.dev.pa.activity.BaseActivity;

public class ExpressionShareActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_expression_activity);
		setTitleText("表情分享");
		initPage();
	}
	
	private void setTitleText(String title){
		View view = findViewById(R.id.title_text_tv);
		if (null != view && view instanceof TextView) {
			((TextView)view).setText(title);
		}
	}

	private void initPage() {
		LinearLayout layout =(LinearLayout)findViewById(R.id.expression_share_ll);
		int count = layout.getChildCount();
		int k = 1;
		for (int i = 0; i < count; i++) {
			View view = layout.getChildAt(i);
			if (view instanceof LinearLayout) {
				LinearLayout layout2 = (LinearLayout)view;
				layout2.getChildAt(0).setTag(k);
				layout2.getChildAt(0).setOnClickListener(exPressionClickListener);
				k++;
				layout2.getChildAt(1).setTag(k);
				layout2.getChildAt(1).setOnClickListener(exPressionClickListener);
				k++;
			}
		}
		findViewById(R.id.title_left_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private View.OnClickListener exPressionClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int index = Integer.parseInt(String.valueOf(v.getTag()));
			alertShare(index);

		}
	};

	private void alertShare(final int index) {
//		int imgResId = getResources().getIdentifier("expression_"+index,  
//				"drawable", getPackageName()); 
		Intent intent = new Intent(this, WBShareActivity.class);
		intent.putExtra("shareImgName", "expression_"+index+".png");
		if (index <= 6) {
			intent.putExtra("share_title", "飞利浦净化丸");
		}else if(index <= 12){
			intent.putExtra("share_title", "飞利浦空净神器系列");
		}else{
			intent.putExtra("share_title", "飞利浦净化士");
		}
		intent.putExtra("type", 2);
		startActivity(intent);
		finish();
	}
}
