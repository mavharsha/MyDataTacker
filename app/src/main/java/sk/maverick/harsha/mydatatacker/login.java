package sk.maverick.harsha.mydatatacker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login extends ActionBarActivity {

    EditText user, password;
    Button login;
    TextView signup;
    String username, pass, line, current_device_number;
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //startActivity(new Intent(this, billingCycle.class));

        /* Initializing the views */
        user = (EditText) findViewById(R.id.phone_number_et);
        password = (EditText) findViewById(R.id.password_et);
        login = (Button) findViewById(R.id.login_button);
        signup = (TextView) findViewById(R.id.signup_tv);

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        current_device_number = telephonyManager.getLine1Number();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 username = user.getText().toString();
                 pass = password.getText().toString();


               if( regexValidator.validatePhoneNumber(username)==false)
               {
                   Toast.makeText(getApplicationContext(), "Please check the Phone number entered", Toast.LENGTH_SHORT).show();

               }

                if( regexValidator.validatePassword(pass)==false)
                {
                    Toast.makeText(getApplicationContext(), "Please check the Phone number entered", Toast.LENGTH_SHORT).show();

                }

               if(regexValidator.validatePhoneNumber(username)==true & regexValidator.validatePassword(pass)==true )
                {
                    new loginAsync().execute();
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(sk.maverick.harsha.mydatatacker.login.this, signUp.class));
               // Log.v("Login","SignUP login");
            }
        });

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        finish();
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

    private class loginAsync extends AsyncTask<URL, Integer, Long>{

        @Override
        protected Long doInBackground(URL... params) {

            URL url = null;
            try{

                /* Since it is a GET request,
                *  send user entered number(username), password (pass) and the current device phone number
                * (current_device_number) as parameters in the URL
                * */

                 url = new URL ("http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139");

            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try {

                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                /* While using HTTP url connection use setOutput(true) for "POST" verb, for other verbs use setRequestMethod(Verb)*/
                http.connect();

                /* Response */
                if(http == null)
                {
                    Log.v("Async", "HTTP null response, response message :"+ http.getResponseMessage());

                }else
                {
                    InputStream in = http.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));


                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    line = buffer.toString();

                    Log.v("Async  response", "Response" + line);

                    /* Creating a json object of the response */
                   // JSONObject obj = new JSONObject(line);

                    //String temp = obj.getString("country");
                   // String temp2 = obj.getString("sunrise");

                    //Log.v("Asynnc Json", obj.getString("country") + "  " + obj.getString("sunrise"));

                    Log.v("Login Async", "http connect works " + http.getResponseMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.v("Login Async","Login Async HTTP Connection catch block");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Long aLong) {

            /* If the response for the and the entered number and current phone number is same, owner */
            startActivity(new Intent(sk.maverick.harsha.mydatatacker.login.this, homeScreen.class));

        }
    }






}
