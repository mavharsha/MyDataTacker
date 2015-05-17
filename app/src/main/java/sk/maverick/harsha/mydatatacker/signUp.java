/*
 * Copyright (c)
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 *
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
import android.widget.Toast;
import org.json.JSONObject;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class signUp extends ActionBarActivity {

    EditText firstname, lastname, phonenumber, email, password, owner_limit,family_limit ,datacyle;
    String firstname_st, lastname_st, phonenumber_st, email_st, password_st, owner_limit_st, family_limit_st,datacyle_st;
    String line = "";
    Button register;
    public static final String PREFS_NAME = "myprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        /* Initializing the Views */
        firstname = (EditText) findViewById(R.id.firstname_edit_signup);
        lastname = (EditText) findViewById(R.id.lastname_edit_signup);
        phonenumber = (EditText) findViewById(R.id.phonenumber_edit_signup);
        email = (EditText) findViewById(R.id.email_edit_signup);
        password = (EditText) findViewById(R.id.password_edit_signup);
        datacyle = (EditText) findViewById(R.id.startdate_edit_signup);
        owner_limit = (EditText) findViewById(R.id.ownerlimit_edit_signup);
        family_limit = (EditText) findViewById(R.id.familylimit_edit_signup);
        register = (Button) findViewById(R.id.register_button_signup);

        TelephonyManager  telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String temp = telephonyManager.getLine1Number();

        phonenumber.setText(temp.substring(2));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstname_st = firstname.getText().toString();
                lastname_st = lastname.getText().toString();
                phonenumber_st = phonenumber.getText().toString();
                email_st = email.getText().toString();
                password_st = password.getText().toString();
                datacyle_st = datacyle.getText().toString();
                owner_limit_st = owner_limit.getText().toString();
                family_limit_st = family_limit.getText().toString();

                if(!regexValidator.validateName(firstname_st))
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ firstname.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + firstname.getHint().toString() );
                }

                if(!regexValidator.validateName(lastname_st))
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ lastname.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + lastname.getHint().toString() );
                }

                if(!regexValidator.validateEmail(email_st))
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ email.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + email.getHint().toString() );
                }


                if(!regexValidator.validatePhoneNumber(phonenumber_st))
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ phonenumber.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + phonenumber.getHint().toString() );
                }


                if(!regexValidator.validateEmail(email_st))
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ email.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + email.getHint().toString() );
                }

                if(!regexValidator.validatePassword(password_st))
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ password.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + password.getHint().toString() );
                }

                if(!regexValidator.validateNumber(family_limit_st))
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ family_limit.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + family_limit.getHint().toString() );
                }
                if(!regexValidator.validateNumber(owner_limit_st))
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ owner_limit.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up", "Wrong" + owner_limit.getHint().toString());
                }

                Log.v("SignUp", firstname_st
                        + " " + lastname_st
                        + " " + phonenumber_st
                        + " " + email_st +
                        " " + password_st +
                        " " + datacyle_st +
                        " " + owner_limit_st +
                        " " + family_limit_st +
                        " " + owner_limit_st);

                if(regexValidator.validateName(firstname_st)&
                        regexValidator.validateName(lastname_st) &
                        regexValidator.validateEmail(email_st)  &
                        regexValidator.validatePhoneNumber(phonenumber_st) &
                        regexValidator.validateEmail(email_st)  &
                        regexValidator.validatePassword(password_st)&
                        regexValidator.validateNumber(family_limit_st)&
                        regexValidator.validateNumber(owner_limit_st)){

                    new AsyncSignUp().execute();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void onClick(View v) {

        if (v.getId() == R.id.firstname_edit_signup) {
            Toast.makeText(getApplicationContext(), "FirstName", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.lastname_edit_signup) {
            Toast.makeText(getApplicationContext(), "LastName", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.email_edit_signup) {
            Toast.makeText(getApplicationContext(), "Email", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.phonenumber_edit_signup) {
            Toast.makeText(getApplicationContext(), "PhoneNumber", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.password_edit_signup) {
            Toast.makeText(getApplicationContext(), "Password", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.startdate_edit_signup) {
            Toast.makeText(getApplicationContext(), "Start Day of Billing ", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.ownerlimit_edit_signup) {
            Toast.makeText(getApplicationContext(), "Data limit", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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


    private class AsyncSignUp extends AsyncTask<URL, Integer, Long> {

        @Override
        protected Long doInBackground(URL... params) {


            URL url = null;
            try{

                url = new URL (new uri().getIp()+"/User/Register/");
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

                data.put("FirstName", firstname_st);
                data.put("LastName", lastname_st);
                data.put("PhoneNo", phonenumber_st);
                data.put("Email", email_st);
                data.put("Password", password_st);
                data.put("StartDate", datacyle_st);
                data.put("DataLimit",owner_limit_st);

                OutputStreamWriter output_writer = new OutputStreamWriter(http.getOutputStream());
                output_writer.write(data.toString());
                output_writer.flush();

                Log.v("SignUP", "Responsecode"+http.getResponseCode());
                /* Response */
                if(http.getResponseCode()== 200){
                    // Read resopnse
                    // InputStream in = new BufferedInputStream(http.getInputStream());
                    Log.v("Sign Up! Async", "http connect works " + http.getResponseMessage());
                    line = http.getResponseMessage();

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

            if (line.equalsIgnoreCase("Success")) {

                SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("family_limit", Integer.parseInt(family_limit_st));
                editor.apply();

                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(signUp.this);
                builder.setCancelable(true)
                        .setTitle("Congrats!")
                        .setMessage("You have been Registered!")
                        .setPositiveButton("Go back to Sign In!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(signUp.this, login.class));
                                onDestroy();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}