package sk.maverick.harsha.mydatatacker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class billingCycle extends ActionBarActivity {

    DatePicker datepicker;
    Button confirm;
    int date;
    String phone;
    String line ="";
    String dayOfMonth;
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_cycle);

        /* Initialization of Views*/
        datepicker = (DatePicker) findViewById(R.id.datePicker);
        confirm = (Button) findViewById(R.id.billingcycle_button);

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
         phone = telephonyManager.getLine1Number();

        phone = phone.substring(phone.length()-10);

        /* OnClick listener for confirm button*/
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), datepicker.getDayOfMonth() + "/" + datepicker.getMonth() + "/" + datepicker.getYear(), Toast.LENGTH_SHORT).show();

                new sendDate().execute();


            }


        });

    }   /* End of onCreate */

    private void openDialog() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setTitle("Dialog!")
                .setMessage("Billing Day Confirmed!")
                .setPositiveButton("Cool!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(billingCycle.this, Settings.class));
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_billing_cycle, menu);
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

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    private class sendDate extends AsyncTask<URL, Integer, Long> {


        @Override
        protected Long doInBackground(URL... params) {

            URL url = null;
            try{
                url = new URL (new uri().getIp()+"User/UpdateStartDate/");
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try{
                http = (HttpURLConnection) url.openConnection();

                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                /* While using HTTP url connection use setOutput(true) for "POST" verb, for other verbs use setRequestMethod(Verb)*/

                http.setRequestMethod("PUT");
                http.connect();

                int month = datepicker.getMonth();


               //String dayOfMonth;
                dayOfMonth = Integer.toString(month) + "/" +Integer.toString(datepicker.getMonth()) + "/" +Integer.toString(datepicker.getYear());

                Log.v("DayofMonth",dayOfMonth);
                /* JSON Object("Day", int) */
                JSONObject data = new JSONObject();
                data.put("PhoneNo",phone);
                data.put("StartDate", dayOfMonth);

                OutputStreamWriter  output_writer = new OutputStreamWriter(http.getOutputStream());
                output_writer.write(data.toString());
                output_writer.flush();


                    /* Response */
                if(http.getResponseCode()==200)
                {
                    // InputStream in = new BufferedInputStream(http.getInputStream());
                    Log.v("Async", "http connect works " + http.getResponseCode());
                    line = "success";
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {

                /* Closing the HTTP URL CONNECTION */
                http.disconnect();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Long aLong) {



            if(line.equalsIgnoreCase("success")){

                Toast.makeText(getApplicationContext(), "Billing Cycle Set!", Toast.LENGTH_SHORT).show();

            }else {

                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();

            }




        }
    }
}