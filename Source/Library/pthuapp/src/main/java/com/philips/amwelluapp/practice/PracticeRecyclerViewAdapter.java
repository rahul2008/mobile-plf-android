package com.philips.amwelluapp.practice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.americanwell.sdk.entity.practice.Practice;
import com.philips.amwelluapp.R;


public class PracticeRecyclerViewAdapter extends RecyclerView.Adapter<PracticeRecyclerViewAdapter.CustomViewHolder> {
    private PTHPractice mPTHPractice;
    private Context mContext;
    private OnPracticeItemClickListener mOnPracticeItemClickListener;


    public OnPracticeItemClickListener getmOnPracticeItemClickListener() {
        return mOnPracticeItemClickListener;
    }

    public void setmOnPracticeItemClickListener(OnPracticeItemClickListener mOnPracticeItemClickListener) {
        this.mOnPracticeItemClickListener = mOnPracticeItemClickListener;
    }

    public PracticeRecyclerViewAdapter(Context context, PTHPractice pthPractice) {
        this.mPTHPractice = pthPractice;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.practice_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Practice practice = mPTHPractice.getPractices().get(i);


        customViewHolder.label.setText(practice.getName());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPracticeItemClickListener.onItemClick(practice);
            }
        };
        customViewHolder.relativeLayout.setOnClickListener(listener);



    }

    @Override
    public int getItemCount() {
        return (null != mPTHPractice ? mPTHPractice.getPractices().size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageButton;
        protected TextView label;
        protected RelativeLayout relativeLayout;

        public CustomViewHolder(View view) {
            super(view);
            //this.imageButton = (ImageView) view.findViewById(R.id.pth_practice_logo);
            this.relativeLayout = (RelativeLayout)view.findViewById(R.id.pth_practice_row_layout);
            this.label = (TextView) view.findViewById(R.id.pth_practice_name);

        }
    }
}
