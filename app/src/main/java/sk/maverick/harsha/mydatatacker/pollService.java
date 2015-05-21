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
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    int Unique = 8798;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        r = new Runnable() {
            public void run() {

                Log.v("Boot Service"," Inside Boot Service!! Yay!! ");

                    URL url = null;
                    try {
                        url = new URL("http://www.google.com");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        Log.v("MyService", "Malformed URL");
                    }

                    HttpURLConnection http = null;
                    try {
                        if (url != null) {
                            http = (HttpURLConnection) url.openConnection();
                            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                            http.setDoOutput(true);
                            http.connect();

                    /* JSON Object("Day", int) */
                        JSONObject data = new JSONObject();


                        OutputStreamWriter output_writer = new OutputStreamWriter(http.getOutputStream());
                        output_writer.write(data.toString());
                        output_writer.flush();


                    /* Response */
                        if (http.getResponseCode() == 200) {

                            // InputStream in = new BufferedInputStream(http.getInputStream());
                            Log.v("Async", "http connect works " + http.getResponseMessage());

                        } else {
                            Log.v("Async", "http failed " + http.getResponseMessage());

                        }
                    }

                    } catch (NullPointerException | IOException e) {
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
                r.run();
                checkForNotification();

            }
        }, 1000, 1000 * 60 * 1 );

        return START_STICKY;

        }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkForNotification() {

        notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Unique);


        Intent intent = new Intent(this, ownerHomeScreen.class );
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notificationBuiler  = new Notification.Builder(this)
                .setContentTitle("MyDataTracker Alert!")
                .setContentText("Overusage!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        notificationManager.notify(Unique,notificationBuiler);


    }
}
