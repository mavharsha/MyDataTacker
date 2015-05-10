package sk.maverick.harsha.mydatatacker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

    EditText firstname, lastname, phonenumber, email, password, datalimit, datacyle;
    String firstname_st, lastname_st, phonenumber_st, email_st, password_st, datalimit_st, datacyle_st;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        /* Initializing the Views */
        firstname = (EditText) findViewById(R.id.firstname_edit_signup);
        lastname = (EditText) findViewById(R.id.lastname_edit_signup);
        phonenumber = (EditText) findViewById(R.id.phonenumber_edit_signup);
        email = (EditText) findViewById(R.id.email_edit_signup);
        password = (EditText) findViewById(R.id.password_edit_signup);
        datacyle = (EditText) findViewById(R.id.startdate_edit_signup);
        datalimit = (EditText) findViewById(R.id.datalimit_edit_signup);
        register = (Button) findViewById(R.id.register_button_signup);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstname_st = firstname.getText().toString();
                lastname_st = lastname.getText().toString();
                phonenumber_st = phonenumber.getText().toString();
                email_st = email.getText().toString();
                password_st = password.getText().toString();
                datacyle_st = datacyle.getText().toString();
                datalimit_st = datalimit.getText().toString();


                if(regexValidator.validateName(firstname_st) == false)
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ firstname.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + firstname.getHint().toString() );
                }

                if(regexValidator.validateName(lastname_st) == false)
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ lastname.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + lastname.getHint().toString() );
                }

                if(regexValidator.validateEmail(email_st) == false)
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ email.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + email.getHint().toString() );
                }


                if(regexValidator.validatePhoneNumber(phonenumber_st) == false)
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ phonenumber.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + phonenumber.getHint().toString() );
                }


                if(regexValidator.validateEmail(email_st) == false)
                {
                    Toast.makeText(getApplicationContext(), "Wrong "+ email.getHint().toString() , Toast.LENGTH_SHORT).show();
                    Log.v("Sign Up","Wrong" + email.getHint().toString() );
                }


                //regexValidator.validateEmail(email_st);
               // regexValidator.validateName(lastname_st);
                Log.v("SignUp", firstname_st
                        + " " + lastname_st
                        + " " + phonenumber_st
                        + " " + email_st +
                        " " + password_st +
                        " " + datacyle_st +
                        " " + datalimit_st);

                new AsyncSignUp().execute();


            }
        });
    }


    public void onClick(View v) {

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.START | Gravity.LEFT, 0, 0);

        if (v.getId() == R.id.firstname_edit_signup) {
            toast.makeText(getApplicationContext(), "FirstName", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.lastname_edit_signup) {
            toast.makeText(getApplicationContext(), "LastName", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.email_edit_signup) {
            toast.makeText(getApplicationContext(), "Email", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.phonenumber_edit_signup) {
            toast.makeText(getApplicationContext(), "PhoneNumber", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.password_edit_signup) {
            toast.makeText(getApplicationContext(), "Password", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.startdate_edit_signup) {
            toast.makeText(getApplicationContext(), "Start Day of Billing ", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.datalimit_edit_signup) {
            toast.makeText(getApplicationContext(), "Data limit", Toast.LENGTH_SHORT).show();
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

                url = new URL (" http://192.168.1.71:7649/WebApi/api/User/Register/");
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
                data.put("DataLimit",datalimit_st);

                OutputStreamWriter output_writer = new OutputStreamWriter(http.getOutputStream());
                output_writer.write(data.toString());
                output_writer.flush();


                /* Response */
                if (http == null) {
                    Log.v("Async", "http is null");

                } else {
                    // Read resopnse
                    // InputStream in = new BufferedInputStream(http.getInputStream());
                    Log.v("Sign Up! Async", "http connect works " + http.getResponseMessage());
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




        }
    }
}