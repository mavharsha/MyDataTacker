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

import org.achartengine.chart.PieChart;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GraphOwner extends ActionBarActivity {

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<Double> quotaUsed = new ArrayList<Double>();
    TelephonyManager telephonyManager;
    String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_owner);

        Button piegraph = (Button) findViewById(R.id.piegraph_ownergraph_btn);
        Button linegraph = (Button) findViewById(R.id.linegraph_ownergraph_btn);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phonenumber = telephonyManager.getLine1Number();

        phonenumber = phonenumber.substring(phonenumber.length()-10);


        piegraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new PieAsync().execute();
            }
        });

        linegraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(GraphOwner.this, GraphDeviceList.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph_owner, menu);
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


    private class PieAsync extends AsyncTask<URL, Integer, Long> {

        @Override
        protected Long doInBackground(URL... params) {

            URL url = null;
            try {

                url = new URL(new uri().getIp() +"UsageDetails/GetFamilyUsersDetails/?phoneNo="+phonenumber+"&duration=");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try {

                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                /* While using HTTP url connection use setOutput(true) for "POST" verb, for other verbs use setRequestMethod(Verb)*/
                http.connect();

                Log.v("URL",""+url);
                /* Response */
                if (http.getResponseCode() == 200) {

                    Log.v("Post Execute", "Response message" + http.getResponseCode());

                    InputStream input = http.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String line1;
                    while ((line1 = reader.readLine()) != null) {
                        buffer.append(line1 + "\n");
                    }
                    line1 = buffer.toString();

                    Log.v("Async  response", "Response line is " + line1);

                   /* Creating a json object of the response */
                    JSONArray jsonArray = new JSONArray(line1);
                    JSONObject jsonObject;


                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        Log.v("graph value", "" + Double.parseDouble(jsonObject.getString("DataUsed")));

                            name.add(jsonObject.getString("PhoneNo"));
                            quotaUsed.add(Double.parseDouble(jsonObject.getString("DataUsed")));

                    }

                    Log.v("Login Async", "http connect works " + http.getResponseMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Log.v("Post Execute", "In post execute");
            MyPieChart graphLineUser = new MyPieChart();
            Intent it = graphLineUser.getIntent(GraphOwner.this, name, quotaUsed);
            startActivity(it);
            quotaUsed.clear();
        }
    }











}
