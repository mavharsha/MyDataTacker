package sk.maverick.harsha.mydatatacker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Harsha on 5/9/2015.
 */
public class pollReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "On Boot Complete works", Toast.LENGTH_SHORT).show();
        Log.v("Boot Broadcast"," Inside Broadcast! Yay!! ");

        Intent it  =  new Intent(context, pollService.class);
        context.startService(it);
    }
}
