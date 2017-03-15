package com.tooltip;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by 310230979 on 3/9/2017.
 */


public class TooltipWindow {
    private static final int MSG_DISMISS_TOOLTIP = 100;
    private Context ctx;
    private PopupWindow tipWindow;
    private View contentView;
    private LayoutInflater inflater;
    LinearLayout linearLayout, linearLayouts;

    public TooltipWindow(Context ctx, String tooltip) {
        this.ctx = ctx;
        tipWindow = new PopupWindow(ctx);

        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (tooltip.equalsIgnoreCase("BOTTOM")) {
            contentView = inflater.inflate(R.layout.tooltip_bottom_layout, null);
            linearLayout = (LinearLayout) contentView.findViewById(R.id.linearLayout);

        } else {
            contentView = inflater.inflate(R.layout.tooltip_top_layout, null);
            linearLayouts = (LinearLayout) contentView.findViewById(R.id.linearLayouts);
        }
        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(ctx, R.drawable.tooltip_test));
        if (tooltip.equalsIgnoreCase("BOTTOM")) {

             drawable = DrawableCompat.wrap(linearLayout.getBackground());
        }
        ColorStateList myList = ContextCompat.getColorStateList(ctx, R.color.colorPrimaryDark);

        DrawableCompat.setTintList(drawable, myList);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.MULTIPLY);




    }

    void showToolTip(View anchor, String tooltip) {

        tipWindow.setHeight(LayoutParams.WRAP_CONTENT);
        tipWindow.setWidth(LayoutParams.WRAP_CONTENT);
        tipWindow.setOutsideTouchable(true);
        tipWindow.setTouchable(true);
        tipWindow.setFocusable(true);
        //tipWindow.setAnimationStyle(android.R.style.Animation_Dialog);
         tipWindow.setBackgroundDrawable(new BitmapDrawable(
                ctx
                        .getResources(),
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        ));

        tipWindow.setContentView(contentView);

        int screen_pos[] = new int[2];
// Get location of anchor view on screen
        anchor.getLocationOnScreen(screen_pos);

// Get rect for anchor view
        Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0]
                + anchor.getWidth(), screen_pos[1] + anchor.getHeight());

// Call view measure to calculate how big your view should be.
        contentView.measure(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        int contentViewHeight = contentView.getMeasuredHeight();
        int contentViewWidth = contentView.getMeasuredWidth();
// In this case , i dont need much calculation for x and y position of
// tooltip
// For cases if anchor is near screen border, you need to take care of
// direction as well
// to show left, right, above or below of anchor view
        int position_x = 0;
        int position_y = 0;
        if (tooltip.equalsIgnoreCase("BOTTOM")) {
            position_x = anchor_rect.centerX() - (contentViewWidth / 2);
            position_y = anchor_rect.bottom - (anchor_rect.height() - 20);

        } else {
            position_x = anchor_rect.centerX() - (contentViewWidth / 2);
            position_y = anchor_rect.top - (anchor_rect.height() + 25);

        }

        tipWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, position_x, position_y);

// send message to handler to dismiss tipWindow after X milliseconds
     //   handler.sendEmptyMessageDelayed(MSG_DISMISS_TOOLTIP, 9000);
    }

    boolean isTooltipShown() {
        if (tipWindow != null && tipWindow.isShowing())
            return true;
        return false;
    }

    void dismissTooltip() {
        if (tipWindow != null && tipWindow.isShowing())
            tipWindow.dismiss();
    }

   /* Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_TOOLTIP:
                    if (tipWindow != null && tipWindow.isShowing())
                        tipWindow.dismiss();
                    break;
            }
        }

        ;
    };*/

}
