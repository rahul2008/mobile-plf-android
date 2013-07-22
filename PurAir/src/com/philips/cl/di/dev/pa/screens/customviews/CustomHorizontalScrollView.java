package com.philips.cl.di.dev.pa.screens.customviews;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;

import com.philips.cl.di.dev.pa.interfaces.SizeCallback;

/**
 * A HorizontalScrollView (HSV) implementation that disallows touch events (so no scrolling can be done by the user).
 * 
 * This HSV MUST contain a single ViewGroup as its only child, and this ViewGroup will be used to display the children Views
 * passed in to the initViews() method.
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {
    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomHorizontalScrollView(Context context) {
        super(context);
        init(context);
    }

    void init(Context context) {
        // remove the fading as the HSV looks better without it
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
    }

    /**
     * @param children
     *            The child Views to add to parent.
     * @param scrollToViewIdx
     *            The index of the View to scroll to after initialisation.
     * @param sizeCallback
     *            A SizeCallback to interact with the HSV.
     */
    public void initViews(View[] children, int scrollToViewIdx, SizeCallback sizeCallback) {
        // A ViewGroup MUST be the only child of the HSV
        ViewGroup parent = (ViewGroup) getChildAt(0);

        // Add all the children, but add them invisible so that the layouts are calculated, but you can't see the Views
        for (int i = 0; i < children.length; i++) {
            children[i].setVisibility(View.INVISIBLE);
            parent.addView(children[i]);
        }

        // Add a layout listener to this HSV
        // This listener is responsible for arranging the child views.
        OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener(parent, children, scrollToViewIdx, sizeCallback);
        getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Do not allow touch events.
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Do not allow touch events.
        return false;
    }

    /**
     * An OnGlobalLayoutListener impl that passes on the call to onGlobalLayout to a SizeCallback, before removing all the Views
     * in the HSV and adding them again with calculated widths and heights.
     */
    class MyOnGlobalLayoutListener implements OnGlobalLayoutListener {
        ViewGroup parent;
        View[] children;
        int scrollToViewIdx;
        int scrollToViewPos = 0;
        SizeCallback sizeCallback;

        /**
         * @param parent
         *            The parent to which the child Views should be added.
         * @param children
         *            The child Views to add to parent.
         * @param scrollToViewIdx
         *            The index of the View to scroll to after initialisation.
         * @param sizeCallback
         *            A SizeCallback to interact with the HSV.
         */
        public MyOnGlobalLayoutListener(ViewGroup parent, View[] children, int scrollToViewIdx, SizeCallback sizeCallback) {
            this.parent = parent;
            this.children = children;
            this.scrollToViewIdx = scrollToViewIdx;
            this.sizeCallback = sizeCallback;
        }

        @SuppressWarnings("deprecation")
		@Override
        public void onGlobalLayout() {
            final HorizontalScrollView me = CustomHorizontalScrollView.this;

            // The listener will remove itself as a layout listener to the HSV
            me.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            // Allow the SizeCallback to 'see' the Views before we remove them and re-add them.
            // This lets the SizeCallback prepare View sizes, ahead of calls to SizeCallback.getViewSize().
            sizeCallback.onGlobalLayout();
            parent.removeViewsInLayout(0, children.length);
            final int w = me.getMeasuredWidth();
            final int h = me.getMeasuredHeight();

            // Add each view in turn, and apply the width and height returned by the SizeCallback.
            int[] dims = new int[2];
            scrollToViewPos = 0;
            for (int i = 0; i < children.length; i++) {
                sizeCallback.getViewSize(i, w, h, dims);
               // children[i].setVisibility(View.VISIBLE);
                if(i==scrollToViewIdx)
                {
                	children[i].setVisibility(View.VISIBLE);
                }
                parent.addView(children[i], dims[0], dims[1]);
                if (i < scrollToViewIdx) {
                    scrollToViewPos += dims[0];
                }
                
            }

            // We need to post this action, rather than call immediately.
            // If we try immediately, it will not scroll.
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    me.scrollBy(scrollToViewPos, 0);
                }
            });
        }
    }

    }
