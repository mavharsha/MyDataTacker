
/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

package sk.maverick.harsha.mydatatacker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class ownerHomeScreen extends ActionBarActivity {

    public static final String PREFS_NAME = "myprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);



        Button graph = (Button) findViewById(R.id.homescreen_details_btn);
        Button familysetting = (Button) findViewById(R.id.home_settings);
        Button manageconn = (Button) findViewById(R.id.manage_network);

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Graphs come here!",Toast.LENGTH_SHORT).show();
               // startActivity(new Intent(homeScreen.this, manageConnection.class));
            }
        });

        familysetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ownerHomeScreen.this, Settings.class));

            }
        });


        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ownerHomeScreen.this, manageConnection.class));
                // startActivity(new Intent(homeScreen.this, manageConnection.class));
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home_settings) {
            return true;

        }else if(id == R.id.manage_network)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
