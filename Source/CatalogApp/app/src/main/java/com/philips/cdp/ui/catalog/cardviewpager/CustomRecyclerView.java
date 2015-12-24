package com.philips.cdp.ui.catalog.cardviewpager;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CustomRecyclerView extends RecyclerView {

    private Point mStartMovePoint = new Point(0, 0);
    private int mStartMovePositionFirst = 0;
    private int mStartMovePositionSecond = 0;

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {

        final boolean ret = super.onTouchEvent(e);
        final LayoutManager lm = getLayoutManager();
        View childView = lm.getChildAt(0);
        View childViewSecond = lm.getChildAt(1);

        if ((e.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE
                && mStartMovePoint.x == 0) {

            mStartMovePoint.x = (int) e.getX();
            mStartMovePoint.y = (int) e.getY();
            mStartMovePositionFirst = lm.getPosition(childView);
            if (childViewSecond != null)
                mStartMovePositionSecond = lm.getPosition(childViewSecond);

        }// if MotionEvent.ACTION_MOVE

        if ((e.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {

            int currentX = (int) e.getX();
            int width = childView.getWidth();

            int xMovement = currentX - mStartMovePoint.x;
            // move back will be positive value
            final boolean moveBack = xMovement > 0;

            int calculatedPosition = mStartMovePositionFirst;
            if (moveBack && mStartMovePositionSecond > 0)
                calculatedPosition = mStartMovePositionSecond;

            if (Math.abs(xMovement) > (width / 3))
                calculatedPosition += moveBack ? -1 : 1;

            if (calculatedPosition >= getAdapter().getItemCount())
                calculatedPosition = getAdapter().getItemCount() - 1;

            if (calculatedPosition < 0 || getAdapter().getItemCount() == 0)
                calculatedPosition = 0;

            mStartMovePoint.x = 0;
            mStartMovePoint.y = 0;
            mStartMovePositionFirst = 0;
            mStartMovePositionSecond = 0;

            smoothScrollToPosition(calculatedPosition);
        }// if MotionEvent.ACTION_UP

        return ret;
    }
}
