package ${packageName};

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ${packageName}library.R;

public class ${escapeXmlString(appTitle)}Activity extends ${superClass} {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demoactivity);

    }

}
