package com.tooltip;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    TooltipWindow tipWindow;
    Button anchor_view, anchor_view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tooltip.R.layout.activity_main);
        anchor_view = (Button) findViewById(com.tooltip.R.id.anchor_view);
        anchor_view2 = (Button) findViewById(com.tooltip.R.id.anchor_view2);
        anchor_view.setOnClickListener(this);
        anchor_view2.setOnClickListener(this);

    }

    @Override
    public void onClick(View anchor) {
        if (anchor.getId() == com.tooltip.R.id.anchor_view2) {
            tipWindow = new TooltipWindow(MainActivity.this, "TOP");
            if (!tipWindow.isTooltipShown())
                tipWindow.showToolTip(anchor, "TOP");
        } else if (anchor.getId() == com.tooltip.R.id.anchor_view) {
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