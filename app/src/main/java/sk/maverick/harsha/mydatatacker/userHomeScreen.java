/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

package sk.maverick.harsha.mydatatacker;

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
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class userHomeScreen extends ActionBarActivity {

    Button graph, manageconnections;
    String line = "";
    public ArrayList<Double> arrayList = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> newarraylist = new ArrayList<ArrayList<Integer>>();
    TelephonyManager telephonyManager;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_screen);


        graph  = (Button) findViewById(R.id.user_homescreen_graphdetails_btn);
        manageconnections = (Button) findViewById(R.id.user_homescreen_manageconn_btn);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phone = telephonyManager.getLine1Number();

        if(phone.length()>10)
        {
            phone = telephonyManager.getLine1Number().substring(1);
        }


        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RetrieveUserGraph().execute();

            }
        });

        manageconnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userHomeScreen.this, manageConnection.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_home_screen, menu);
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



    private class RetrieveUserGraph extends AsyncTask <URL, Integer, Long>{

        @Override
        protected Long doInBackground(URL... params) {

            //String phoneNumber = new login().getNumber();
            URL url = null;
            try{
                url = new URL (new uri().getIp() +"UsageDetails/GetUsageDetails/?phoneNo="+phone+"&duration=Weekly");
                // url = new  URL("http://www.google.com");
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try {

                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();

                /* Response */
                if(http.getResponseCode() == 200){

                    Log.v("Post Execute", "Response message" + http.getResponseCode());
                    InputStream in = http.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    while ((line = reader.readLine()) != null) {

                        buffer.append(line + "\n");
                    }
                    line = buffer.toString();

                    Log.v("Async  response", "Response" + line);

                   /* Creating a json object of the response */
                    JSONArray jsonArray = new JSONArray(line);
                    JSONObject jsonObject;


                    for(int i=0; i<jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);

                        Log.v("DataUsed", "" + jsonObject.getString("DataUsed"));
                        arrayList.add(Double.parseDouble(jsonObject.getString("DataUsed")));
                    }

                    Log.v("Login Async", "http connect works " + http.getResponseMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Long aLong) {
            GraphLine graphLineUser = new GraphLine();
            Intent it = graphLineUser.getIntent(getApplicationContext(),arrayList);
            startActivity(it);
            arrayList.clear();
        }
    }


}
