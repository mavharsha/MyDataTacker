

/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

package sk.maverick.harsha.mydatatacker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class addDevices extends ActionBarActivity {

    String firstname, lastname, email,phonenumber, ownerphone,limit;
    String line="";
    EditText name, last, email_et, phone, quota;
    public static final String PREFS_NAME = "myprefs";


    Button button;
    TextView remaining_family;
    TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_devices);

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("family_limit", 100);
        editor.apply();


        name = (EditText) findViewById(R.id.firstname_edit_adddevice);
        last = (EditText) findViewById(R.id.lastname_edit_adddevice);
        email_et = (EditText) findViewById(R.id.email_edit_adddevice);
        quota = (EditText) findViewById(R.id.quota_adddevice_et);
        phone = (EditText) findViewById(R.id.phonenumber_edit_adddevice);
        Button update = (Button) findViewById(R.id.register_btn_adddevice);
        remaining_family = (TextView) findViewById(R.id.remaining_adddevice_text);

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

       final int temporary = sharedpreferences.getInt("family_limit", 100);

        remaining_family.setText(""+temporary);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstname = name.getText().toString();
                lastname = last.getText().toString();
                email = email_et.getText().toString();
                phonenumber = phone.getText().toString();
                limit = quota.getText().toString();
                ownerphone = telephonyManager.getLine1Number();


                ownerphone = ownerphone.substring(ownerphone.length()-10);

                if (Integer.parseInt(limit) > temporary) {
                    Toast.makeText(getApplicationContext(), "Limit Exceeded", Toast.LENGTH_SHORT).show();
                } else {

                    new addDeviceAsync().execute();

                }


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_devices, menu);
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

    private class addDeviceAsync extends AsyncTask<URL, Integer, Long> {

        @Override
        protected Long doInBackground(URL... params) {

            Log.v("Inside async","yay");
            URL url = null;
            try{

                url = new URL (new uri().getIp()+"User/AddUser/");
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try {
                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                /* While using HTTP url connection use setOutput(true) for "POST" verb, for other verbs use setRequestMethod(Verb)*/
                http.setDoOutput(true);
                http.connect();

                /* JSON Object("Day", int) */
                JSONObject data = new JSONObject();

                data.put("FirstName", firstname);
                data.put("LastName", lastname);
                data.put("Email", email);
                data.put("PhoneNo", phonenumber);
                data.put("OwnerPhoneNo", ownerphone);
                data.put("DataLimit",limit);

                OutputStreamWriter output_writer = new OutputStreamWriter(http.getOutputStream());
                output_writer.write(data.toString());
                output_writer.flush();

                Log.v("SignUP", "Responsecode" + http.getResponseCode());
                /* Response */
                if(http.getResponseCode()== 200){
                    // Read resopnse
                    // InputStream in = new BufferedInputStream(http.getInputStream());
                    Log.v("Sign Up! Async", "http connect works " + http.getResponseMessage());
                    line = "Success";
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                http.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {

            if(line.equalsIgnoreCase("success")){

                Toast.makeText(getApplicationContext(), "Added to family plan", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                int t = 100 -  Integer.valueOf(limit);
                editor.putInt("family_limit",t );
                editor.apply();


            }else
            {
                Toast.makeText(getApplicationContext(), "Couldn't add", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
