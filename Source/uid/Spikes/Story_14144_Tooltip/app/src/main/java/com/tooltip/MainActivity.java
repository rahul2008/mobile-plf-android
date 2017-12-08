package com.tooltip;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    TooltipWindow tipWindow;
    Button anchor_view, anchor_view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tooltip.R.layout.activity_main);
        anchor_view = (Button) findViewById(com.tooltip.R.id.anchor_bottom_view);
        anchor_view2 = (Button) findViewById(com.tooltip.R.id.anchor_top_view);
        anchor_view.setOnClickListener(this);
        anchor_view2.setOnClickListener(this);

    }

    @Override
    public void onClick(View anchor) {
        if (anchor.getId() == com.tooltip.R.id.anchor_top_view) {
            tipWindow = new TooltipWindow(MainActivity.this, "TOP");
            if (!tipWindow.isTooltipShown())
                tipWindow.showToolTip(anchor, "TOP");
        } else if (anchor.getId() == com.tooltip.R.id.anchor_bottom_view) {
            tipWindow = new TooltipWindow(MainActivity.this, "BOTTOM");
            if (!tipWindow.isTooltipShown())
                tipWindow.showToolTip(anchor, "BOTTOM");

        }
    }

    @Override
    protected void onDestroy() {
        if (tipWindow != null && tipWindow.isTooltipShown())
            tipWindow.dismissTooltip();
        super.onDestroy();
    }
}