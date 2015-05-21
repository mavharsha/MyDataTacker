/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

package sk.maverick.harsha.mydatatacker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class EditUser extends ActionBarActivity {

    public static final String PREFS_NAME = "myprefs";
    int quota, family;
    String firstname, phonenum,temp, line="", temp1;
    EditText quota1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);

        EditText name = (EditText) findViewById(R.id.firstname_edituser_quota);
        EditText phone = (EditText) findViewById(R.id.phonenumber_useredit_edtxt);
          quota1 = (EditText) findViewById(R.id.quota_edituser_seek);

        Button update = (Button) findViewById(R.id.update_edituser_btn);
        final TextView remaining = (TextView) findViewById(R.id.remaining_edituser_text);

        Intent it = getIntent();

        firstname= it.getStringExtra("Name");
        phonenum = it.getStringExtra("Phone");
        temp = it.getStringExtra("DataLimit");


        name.setText(firstname);
        phone.setText(phonenum);
        quota1.setText(temp);

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        family = sharedpreferences.getInt("family_limit", 1);
        remaining.setText("" + family);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 temp1 = quota1.getText().toString();
                Log.v("temp",""+ temp1);
                if(Integer.parseInt(temp)>family)
                {
                    Toast.makeText(getApplicationContext(), "Qutoa excceeded", Toast.LENGTH_SHORT).show();

                }else{

                    new editUserAsync().execute();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_user, menu);
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

    private class editUserAsync extends AsyncTask<URL, Integer, Long> {

        @Override
        protected Long doInBackground(URL... params) {


            URL url = null;
            try{

                url = new URL (new uri().getIp()+"User/UpdateDataLimit/");
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try {
                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                /* While using HTTP url connection use setOutput(true) for "POST" verb, for other verbs use setRequestMethod(Verb)*/
                http.setRequestMethod("PUT");
                http.connect();

                /* JSON Object("Day", int) */
                JSONObject data = new JSONObject();

                    data.put("FirstName", firstname);
                data.put("PhoneNo", phonenum);
                data.put("DataLimit", temp1);

                OutputStreamWriter output_writer = new OutputStreamWriter(http.getOutputStream());
                output_writer.write(data.toString());
                output_writer.flush();

                Log.v("URL", "" + url);
                Log.v("DataLimit", temp1+" "+ firstname);


                Log.v("SignUP", "Responsecode" + http.getResponseCode());
                /* Response */
                if(http.getResponseCode()== 200){
                    // Read resopnse
                    // InputStream in = new BufferedInputStream(http.getInputStream());
                    Log.v("Sign Up! Async", "http connect works " + http.getResponseMessage());
                    line = "success";

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

            /* If the respone is success, the create a shared preference to store family limit */

            if(line.equalsIgnoreCase("success")) {
                Toast.makeText(getApplicationContext(), "Successfully updated!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error in updating!", Toast.LENGTH_SHORT).show();

            }


        }
    }




}
