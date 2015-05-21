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
    String username, pass, current_device_number;
    String line="";
    TelephonyManager telephonyManager;
    final String TAG = ".harsha.mydatatacker.login";
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
        current_device_number = current_device_number.substring(1);

        user.setText(""+current_device_number);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               username = user.getText().toString();
                pass = password.getText().toString();

                if (!regexValidator.validatePhoneNumber(username)) {
                    Toast.makeText(getApplicationContext(), "Please check the Phone number entered", Toast.LENGTH_SHORT).show();

                }

                if (!regexValidator.validatePassword(pass)) {
                    Toast.makeText(getApplicationContext(), "Please check the Phone number entered", Toast.LENGTH_SHORT).show();

                }

                new loginAsync().execute();

                if (regexValidator.validatePhoneNumber(username) & regexValidator.validatePassword(pass)) {
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sk.maverick.harsha.mydatatacker.login.this, signUp.class));
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
                Log.v("Login Async", "name and pass "+ username + pass + current_device_number);

                /* Since it is a GET request,
                *  send user entered number(username), password (pass) and the current device phone number
                * (current_device_number) as parameters in the URL
                * */
                url = new URL (new uri().getIp() +"Login/Authenticate/?phoneNo="+username+"&password="+pass+"&devicePhoneNo="+current_device_number);

            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try {

                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                Log.v("Login Async", " inside httpurlconnection ");

                /* While using HTTP url connection use setOutput(true) for "POST" verb, for other verbs use setRequestMethod(Verb)*/
                http.connect();

                Log.v("Login Async", "name and pass "+ username + pass + current_device_number + http.getResponseCode());


                /* Response */
                if(http.getResponseCode() == 200){

                    line = "success";

                    Log.v("Login Async", "http connect works " + http.getResponseMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.v(TAG,"Login Async HTTP Connection catch block");
            }
            return null;
        }


       @Override
        protected void onPostExecute(Long aLong) {

            /* If the response for the and the entered number and current phone number is same, owner */

            if(line.equalsIgnoreCase("success") & username.equalsIgnoreCase(current_device_number))

            {
                startActivity(new Intent(sk.maverick.harsha.mydatatacker.login.this, ownerHomeScreen.class));
            }
            else if(line.equalsIgnoreCase("success"))
            {
                startActivity(new Intent(sk.maverick.harsha.mydatatacker.login.this, userHomeScreen.class));

            }else
            {
                Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String getNumber(){

        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        return telephonyManager.getLine1Number();

    }

}
