package com.philips.cl.di.dev.pa.buyonline;

import com.philips.cl.di.dev.pa.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {

    private int mWidthWeight;
    private int mHeightWeight;

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SquareImageView(Context context) {
        super(context);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareView);
        if (a != null) {
            mWidthWeight = a.getInt(R.styleable.SquareView_width_weight, 1);
            mHeightWeight = a.getInt(R.styleable.SquareView_height_weight, 1);
            a.recycle();
        }
    }

    public void setSquareWeight(int widthWeight, int heightWeight) {
        mWidthWeight = widthWeight;
        mHeightWeight = heightWeight;
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, or internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize * mHeightWeight / mWidthWeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
