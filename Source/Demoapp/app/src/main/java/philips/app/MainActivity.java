package philips.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;

import java.io.File;

import flowmanager.screens.utility.BaseAppUtil;
import philips.app.base.AppFrameworkApplication;
import philips.app.base.FileUtility;

public class MainActivity extends AppCompatActivity {

    private boolean isSdCardFileCreated;
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // We are creating file on SD card only for testing purpose
        isSdCardFileCreated = new BaseAppUtil().createDirIfNotExists();
        final int resId = R.string.com_philips_app_fmwk_app_flow_url;
        FileUtility fileUtility = new FileUtility(this);
        final AppFrameworkApplication applicationContext = (AppFrameworkApplication) getApplicationContext();
        tempFile = fileUtility.createFileFromInputStream(resId, isSdCardFileCreated);
                applicationContext.setTargetFlowManager(new FlowManagerListener() {
                    @Override
                    public void onParseSuccess() {
                        Toast.makeText(MainActivity.this, "Parsing success", Toast.LENGTH_SHORT).show();
                    }
                }, MainActivity.this);
    }
}
