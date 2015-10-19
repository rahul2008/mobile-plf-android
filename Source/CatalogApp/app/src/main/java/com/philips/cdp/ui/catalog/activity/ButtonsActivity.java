package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.philips.cdp.ui.catalog.R;

public class ButtonsActivity extends CatalogActivity {

    private Switch changeButtonState;

    private Button themeButton, outlinedButton, transparentButton, whiteTranspararentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        changeButtonState = (Switch) findViewById(R.id.change_button_state);
        changeButtonState.setChecked(true);
        themeButton = (Button) findViewById(R.id.theme_button);
        outlinedButton = (Button) findViewById(R.id.outlined_button);
        transparentButton = (Button) findViewById(R.id.outlined_transparent_button);
        whiteTranspararentButton = (Button) findViewById(R.id.outlined_transparent_white_button);

        changeButtonState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                themeButton.setEnabled(isChecked);
                outlinedButton.setEnabled(isChecked);
                transparentButton.setEnabled(isChecked);
                whiteTranspararentButton.setEnabled(isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
