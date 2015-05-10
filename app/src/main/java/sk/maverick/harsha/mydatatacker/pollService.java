package sk.maverick.harsha.mydatatacker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {





        r = new Runnable() {
            public void run() {

                Log.v("Boot Service"," Inside Boot Service!! Yay!! ");


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

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    finally {
                /* Closing the HTTP URL CONNECTION */
                        http.disconnect();
                    }

                }
            };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                r.run();
            }
        }, 0, 1500000);

        return START_STICKY;

        }
}
