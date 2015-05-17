/*
 * Copyright (c)
 *
 *  *Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 *
 *
 */

package sk.maverick.harsha.mydatatacker;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.*;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class manageConnection extends ActionBarActivity {

    public static final String PREFS_NAME = "myprefs";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_connection);

        Button settingIntent = (Button) findViewById(R.id.manageconnection_settingsintent_btn);

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String temp = sharedpreferences.getString("battery_status", "couldnt find");

            if (temp.equalsIgnoreCase("low")) {
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(manageConnection.this);
                builder.setCancelable(true)
                        .setTitle("Dialog!")
                        .setMessage("Low Battery!")
                        .setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v("Low Battery", " Low Battery Log");
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

                editor.remove("battery_status");
                editor.apply();
        }


        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        TextView connection_type = (TextView) findViewById(R.id.manageconnection_tv_type);


        if(networkInfo!= null) {
            connection_type.setText(networkInfo.getTypeName());
        }else {
            connection_type.setText("None!");

        }

        settingIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_connection, menu);
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
