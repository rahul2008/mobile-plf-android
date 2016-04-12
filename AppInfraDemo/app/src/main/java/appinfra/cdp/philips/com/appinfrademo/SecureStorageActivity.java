package appinfra.cdp.philips.com.appinfrademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import appinfra.cdp.philips.com.appinfra.security.SecureStorage;
import appinfra.cdp.philips.com.appinfra.security.SecureStorageInterface;

public class SecureStorageActivity extends AppCompatActivity  {
    SecureStorage mSecureStorage=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_storage);
        mSecureStorage = new SecureStorage(getApplicationContext(), SecureStorageInterface.DEVICE_FILE);


        final EditText userKey = (EditText) findViewById(R.id.Key_editText);
        final  EditText data = (EditText) findViewById(R.id.data_editText);
        final  TextView encryptedDataTextView = (TextView) findViewById(R.id.encripted_Ouput_textView);
        final  TextView decryptedDataTextView = (TextView) findViewById(R.id.decripted_Output_textView);

        Button encryptButton = (Button) findViewById(R.id.encript_button);

        assert encryptButton != null;
        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String encryptedData= mSecureStorage.storeValueForKey(userKey.getText().toString(), data.getText().toString());
                if(null==encryptedData){
                    Toast.makeText(SecureStorageActivity.this,"Key or Value incorrect",Toast.LENGTH_SHORT).show();;
                }
                encryptedDataTextView.setText(encryptedData);

            }
        });

        Button decryptButton = (Button) findViewById(R.id.decypt_button);

        assert decryptButton != null;
        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String decryptedData= mSecureStorage.fetchValueForKey(userKey.getText().toString());
                if(null==decryptedData){
                    Toast.makeText(SecureStorageActivity.this,"Key not found",Toast.LENGTH_SHORT).show();;
                }
                decryptedDataTextView.setText(decryptedData);
                System.out.println("MySuccess2");
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteData_button);

        assert deleteButton != null;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSecureStorage.RemoveValueForKey(userKey.getText().toString());
                data.setText(null);
                encryptedDataTextView.setText(null);
                decryptedDataTextView.setText(null);
            }
        });



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
