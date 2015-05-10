package sk.maverick.harsha.mydatatacker;

import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
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

    long dataused, olddataused, temp;
    Handler handler = new Handler();
    Runnable r;
    int i=0;
    Timer timer;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        olddataused = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
        Log.v("Service","oncreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v("Service","onStartCommand");

        r = new Runnable() {
            public void run() {

                dataused = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
                temp = dataused - olddataused;

                temp = temp/(1048576);

                if(temp != 0) {

                    Log.d("message", "Hello! "+ i++ +" " + temp);


                    //Url
                    //HttpUrlConnection
                    //HttpUrlConnection.setRequestProperty
                    //HttpUrlConnection.setRequestMethod
                    //HttpUrlConnection.connect
                    //If the response is 200, then successful
                    //Else then could not post the data

                    URL url = null;
                    try {
                        url = new URL("http://www.google.com");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        Log.v("MyService", "Malformed URL");
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

                        data.put("dataused", temp);

                        OutputStreamWriter output_writer = new OutputStreamWriter(http.getOutputStream());
                        output_writer.write(data.toString());
                        output_writer.flush();


                    /* Response */
                        if (http == null) {
                            Log.v("Async", "http is null");

                        } else {
                            // InputStream in = new BufferedInputStream(http.getInputStream());
                            Log.v("Async", "http connect works " + http.getResponseMessage());
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
                // Print your Hello here...
                r.run();
            }
        }, 0, 15000);
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
