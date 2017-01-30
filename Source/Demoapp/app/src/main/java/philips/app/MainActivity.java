package philips.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;

import philips.app.base.AppFrameworkApplication;

public class MainActivity extends AppCompatActivity implements FlowManagerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AppFrameworkApplication applicationContext = (AppFrameworkApplication) getApplicationContext();
        applicationContext.setTargetFlowManager(this);
    }

    @Override
    public void onParseSuccess() {
        Toast.makeText(MainActivity.this, "Parsing success", Toast.LENGTH_SHORT).show();
    }
}
