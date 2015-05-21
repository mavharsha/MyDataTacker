/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

package sk.maverick.harsha.mydatatacker;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class GraphDeviceList extends ListActivity {

    ListView listView;
    String line, phone;
    public ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
    public ArrayList<String> names = new ArrayList<String>();
    public ArrayList<Double> dataperday = new ArrayList<Double>();
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_device_list);

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phone =  telephonyManager.getLine1Number();

        phone = phone.substring(phone.length()-10);


        listView = getListView();
        new manageAsync().execute();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> map = new HashMap<>();
                map = arrayList.get(position);
                phone = map.get("PhoneNo");
                new GetGraphAsync().execute();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph_device_list, menu);
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


    private class manageAsync extends AsyncTask<URL, Integer, Long> {

        @Override
        protected Long doInBackground(URL... params) {

            URL url = null;
            try {

                url = new URL (new uri().getIp() +"User/GetAllFamilyUsers/?phoneNo="+phone);
                // url = new  URL("http://www.google.com");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try {


                if(url!=null){
                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                /* While using HTTP url connection use setOutput(true) for "POST" verb, for other verbs use setRequestMethod(Verb)*/
                http.connect();

                    Log.v("URL",""+url);

                /* Response */
                if (http.getResponseCode() == 200) {

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


                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("FirstName", jsonObject.getString("FirstName"));
                        map.put("PhoneNo", jsonObject.getString("PhoneNo"));
                        arrayList.add(map);
                        names.add(jsonObject.getString("FirstName"));
                    }

                    Log.v("Login Async", "http connect works " + http.getResponseMessage());
                }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Log.v("Post Execute", "In post execute");
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(GraphDeviceList.this, android.R.layout.simple_list_item_1, names);
            listView.setAdapter(itemsAdapter);
        }

    }

    private class GetGraphAsync extends AsyncTask<URL, Integer, Long> {

        @Override
        protected Long doInBackground(URL... params) {

            URL url = null;
            try {

                url = new URL("http://192.168.1.71:7649/WebApi/api/UsageDetails/GetUsageDetails/?phoneNo="+phone+"&duration=Weekly");
                // url = new  URL("http://www.google.com");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try {

                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                /* While using HTTP url connection use setOutput(true) for "POST" verb, for other verbs use setRequestMethod(Verb)*/
                http.connect();

                Log.v("URL", "" + url);

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
                        dataperday.add(Double.parseDouble(jsonObject.getString("DataUsed")));
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
            GraphLine graphLineUser = new GraphLine();
            Intent it = graphLineUser.getIntent(GraphDeviceList.this, dataperday);
            dataperday.clear();
            startActivity(it);

        }


    }
}
