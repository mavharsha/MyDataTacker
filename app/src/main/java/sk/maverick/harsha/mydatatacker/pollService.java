/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

package sk.maverick.harsha.mydatatacker;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Harsha on 5/9/2015.
 */
public class pollService extends Service {

    Runnable r;
    Timer timer;
    TelephonyManager telephonyManager;
    NotificationManager notificationManager;
    ArrayList <String> name = new ArrayList<>();
    String ownerphone = "";
    int Unique = 8798;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static Boolean notify=false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        ownerphone = telephonyManager.getLine1Number();


        ownerphone = ownerphone.substring(ownerphone.length()-10);




        r = new Runnable() {
            public void run() {

                Log.v("Boot Service"," Inside Boot Service!! Yay!! ");

                URL url = null;
                try {
                    url = new URL(new uri().getIp() +"User/GetAllLimitExceedUsers/?phoneNo="+ownerphone);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.v("MyService", "Malformed URL");
                }

                HttpURLConnection http = null;
                try {
                    if (url != null) {
                        http = (HttpURLConnection) url.openConnection();
                        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        http.connect();


                        Log.v("URL",""+url);
                    /* Response */
                        if (http.getResponseCode() == 200) {

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

                                name.add(jsonObject.getString("FirstName"));
                                Log.v("graph value", "" +jsonObject.getString("FirstName"));

                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                /* Closing the HTTP URL CONNECTION */
                    if (http != null) {
                        http.disconnect();
                    }
                }

            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(!notify) {
                    r.run();
                    checkForNotification();
                }
            }
        }, 1000, 1000 * 60 * 20 );

        return START_STICKY;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkForNotification() {



        notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Unique);

        String alertrname="";

        for(int i=0; i<name.size(); i++)
        {
            alertrname = alertrname.concat(name.get(i));
            alertrname = alertrname.concat("  ");

        }

        name.clear();
        Intent intent = new Intent(this, ownerHomeScreen.class );
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notificationBuiler  = new Notification.Builder(this)
                .setContentTitle("MyDataTracker Alert!")
                .setContentText("Overusage! "+alertrname)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        notificationManager.notify(Unique,notificationBuiler);

        notify=true;

    }
}
