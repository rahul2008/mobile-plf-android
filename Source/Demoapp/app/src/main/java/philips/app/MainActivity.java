package philips.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;

import java.io.File;

import flowmanager.FlowManager;
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
        isSdCardFileCreated = new BaseAppUtil().createDirIfNotExists();
        final int resId = R.string.com_philips_app_fmwk_app_flow_url;
        FileUtility fileUtility = new FileUtility(this);
        final AppFrameworkApplication applicationContext = (AppFrameworkApplication) getApplicationContext();
        tempFile = fileUtility.createFileFromInputStream(resId, isSdCardFileCreated);
        new Thread(new Runnable() {
            public FlowManager targetFlowManager;

            @Override
            public void run() {
                applicationContext.setTargetFlowManager(new FlowManagerListener() {
                    @Override
                    public void onParseSuccess() {
                        Toast.makeText(MainActivity.this, "Parsing success", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < 20; i++) {
                                    Log.d(getClass() + "----", i + "");
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "for loop success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).start();
                    }
                }, MainActivity.this);
            }

        }).start();
    }

   /* @Override
    public void onParseSuccess() {
        Toast.makeText(MainActivity.this, "Parsing success", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<20 ; i++){
                    Log.d(getClass()+"----",i+"");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "for loop success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }*/
}
