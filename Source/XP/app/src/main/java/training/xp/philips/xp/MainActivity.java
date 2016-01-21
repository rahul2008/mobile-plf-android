package training.xp.philips.xp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static int x = 0; //Position on x-axis
    private static int y = 0; //Position on y-axis
    private static String dir = ""; //Direction the rover is facing

    //Logger to log information
    private Logger logger = Logger.getLogger("MarsRoboRover") ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        init();
    }

    void init(){
        String currentPosition = "3 3 E";
        String commands = "MMRMMRMRRM";

        String[] positions = currentPosition.split(" ");
        x = Integer.valueOf(positions[0]);
        y = Integer.valueOf(positions[1]);
        dir = positions[2];

        for (char command : commands.toCharArray()) {
            rove(command);
        }

        //Output Status and Result
        System.out.println("currentPosition..." + currentPosition);
        System.out.println("commands..." + commands);
        System.out.println("newPosition..." + x + " " + y + " " + dir);
    }

    /**
     * All logic for movement is in this method
     * @param command char
     */
    private static void rove(char command) {
//		System.out.println("Start....." + x + " " + y + " " + direction + " " + command);
        if (dir.equalsIgnoreCase("N")) {
            switch (command) {
                case 'L':
                    dir = "W";
                    break;
                case 'R':
                    dir = "E";
                    break;
                case 'M':
                    y++;
                    break;
            }
        } else if (dir.equalsIgnoreCase("E")) {
            switch (command) {
                case 'L':
                    dir = "N";
                    break;
                case 'R':
                    dir = "S";
                    break;
                case 'M':
                    x++;
                    break;
            }
        } else if (dir.equalsIgnoreCase("S")) {
            switch (command) {
                case 'L':
                    dir = "E";
                    break;
                case 'R':
                    dir = "W";
                    break;
                case 'M':
                    y--;
                    break;
            }
        } else if (dir.equalsIgnoreCase("W")) {
            switch (command) {
                case 'L':
                    dir = "S";
                    break;
                case 'R':
                    dir = "N";
                    break;
                case 'M':
                    x--;
                    break;
            }
        }

//		System.out.println("End....." + x + " " + y + " " + direction + " " + command);
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
