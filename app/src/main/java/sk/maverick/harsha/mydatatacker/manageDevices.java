/*
 * Copyright (c)
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 *
 */

package sk.maverick.harsha.mydatatacker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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


public class manageDevices extends ListActivity {

    ListView listView;
    final String TAG = "harsha.mydatatacker.manageDevices";
    String line="";
    String phonenum, deletephone;
    protected ArrayList<String> names = new ArrayList<String>();
    public ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();;
    ArrayAdapter itemsAdapter;
    TextView remaining;
    int temp;
    TelephonyManager telephonyManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_devices);

        Button add = (Button) findViewById(R.id.managedevice_adddevice_btn);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phonenum = telephonyManager.getLine1Number();

        phonenum = phonenum.substring(phonenum.length() - 10);


        listView = getListView();

        new manageAsync().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.v("Listview", "" + names.get(position));

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(manageDevices.this, addDevices.class));
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(manageDevices.this);
                builder.setCancelable(true)
                        .setTitle("Dialog!")
                        .setMessage("Confirm!")
                        .setPositiveButton("Edit Quota!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editQuota(position);
                                Log.v("Edit Quota", "Edit quota screen");
                                // startActivity(new Intent(manageDevices.this, .class));
                            }
                        }).setNegativeButton("Delete Device", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HashMap<String, String> map = new HashMap<String, String>();

                        map= arrayList.get(position);

                        deletephone = map.get("PhoneNo");
                        new deleteAsync().execute();
                        Log.v("Deleting device", ""+ deletephone);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                Log.v("Listview onlong press" , "" + names.get(position));
                return false;
            }
        });

    }

    private void editQuota(int position) {

        HashMap<String, String> map = new HashMap<String, String>();

        map= arrayList.get(position);

        Intent it = new Intent(manageDevices.this, EditUser.class);


        it.putExtra("Name",map.get("FirstName"));
        it.putExtra("Phone", map.get("PhoneNo"));
        it.putExtra("DataLimit", map.get("DataLimit"));

        Log.v("EditQuota", "" + map.get("FirstName") + " " + map.get("PhoneNo") + "  " + map.get("DataLimit"));

        startActivity(it);
        // it.putExtra("Name",map.get("FirstName"));
        // it.putExtra("Phone", map.get("PhoneNo"));
        // it.putExtra("DataUsed",map.get("DataLimit"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_devices, menu);
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
            try{


                url = new URL (new uri().getIp() +"User/GetAllFamilyUsers/?phoneNo="+phonenum+"");
                // url = new  URL("http://www.google.com");
            }catch (MalformedURLException e){
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
                if(http.getResponseCode() == 200){

                    Log.v("Post Execute","Response message" + http.getResponseCode());

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

                        HashMap<String, String> map = new HashMap<>();
                        map.put("FirstName",jsonObject.getString("FirstName") );
                        map.put("PhoneNo", jsonObject.getString("PhoneNo"));
                        map.put("DataLimit", jsonObject.getString("DataLimit"));
                        names.add(jsonObject.getString("FirstName"));
                        arrayList.add(map);

                    }

                    //String temp = obj.getString("country");
                    // String temp2 = obj.getString("sunrise");
                    //Log.v("Asynnc Json", obj.getString("country") + "  " + obj.getString("sunrise"));
                    Log.v("Login Async", "http connect works " + http.getResponseMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Long aLong) {
            Log.v("Post Execute", "In post execute");
            itemsAdapter = new ArrayAdapter<String>(manageDevices.this, android.R.layout.simple_list_item_1, names);
            listView.setAdapter(itemsAdapter);
        }
    }


    private class deleteAsync extends AsyncTask<URL, Integer, Long>{

        @Override
        protected Long doInBackground(URL... params) {

            URL url = null;
            try{

                /* Since it is a GET request,
                *  send user entered number(username), password (pass) and the current device phone number
                * (current_device_number) as parameters in the URL
                * */
                url = new URL (new uri().getIp() +"User/DeleteUser/?phoneNo="+deletephone);

            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try {

                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                Log.v("Login Async", " inside httpurlconnection ");
                http.setRequestMethod("DELETE");
                /* While using HTTP url connection use setOutput(true) for "POST" verb, for other verbs use setRequestMethod(Verb)*/
                http.connect();

                /* Response */
                if(http.getResponseCode() == 200){

                    line = "success";

                    Log.v("Login Async", "http connect works " + http.getResponseMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Long aLong) {

            if(line.equalsIgnoreCase("success")){
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
            names.clear();
            new manageAsync().execute();

        }
    }


}
