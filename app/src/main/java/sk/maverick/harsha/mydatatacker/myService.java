
/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

package sk.maverick.harsha.mydatatacker;

import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import org.json.JSONObject;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Harsha on 4/29/2015.
 */
public class myService extends Service {

    long dataused, olddataused, temp=0;
    final String TAG = ".mydatatacker.myService";
    String phone;
    Handler handler = new Handler();
    Runnable r;
    int i=0;
    Timer timer;
    TelephonyManager telephonyManager;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        olddataused = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        Log.v(TAG,"Service onCreate");
        phone= telephonyManager.getLine1Number();
        phone = phone.substring(phone.length()-10);




        Log.v(TAG,"Service onStartCommand");

        r = new Runnable() {
            public void run() {

                dataused = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
                temp = dataused - olddataused;
                temp = temp/(1048576);

                Log.v("Service","Inside loop , Temp value is "+temp);

                if(temp > 0) {

                Log.v("Service","greater than 0");

                URL url = null;
                try {
                    url = new URL(new uri().getIp() + "UsageDetails/InsertDetails/");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.v(TAG, "Malformed URL");
                }

                HttpURLConnection http = null;
                try {
                    http = (HttpURLConnection) url.openConnection();
                    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                    http.setDoOutput(true);
                    http.connect();

                    JSONObject data = new JSONObject();
                    data.put("PhoneNo", phone);
                    data.put("DataUsed", temp);
                    Log.v("MyService", "" + phone + " " + temp);


                    Log.v("Json ", "" + data.toString());

                    OutputStreamWriter output_writer = new OutputStreamWriter(http.getOutputStream());
                    output_writer.write(data.toString());
                    Log.v("URL",""+url);
                    output_writer.flush();

                    Log.v(TAG, http.getResponseMessage());

                    /* Response */
                    if(http.getResponseCode() == 200)
                    {
                        Log.v(TAG, "http connect works" + temp + " added");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    /* Closing the HTTP URL CONNECTION */
                    http.disconnect();
                }

                }

                olddataused = dataused;

            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                r.run();
            }
        }, 1000,60000);

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        Log.v("Service", "Stopped");
        Log.v("Service","service is destroyed if it is running");
        timer.cancel();
        timer.purge();
        handler.removeCallbacks(r);
        super.onDestroy();
    }





}
