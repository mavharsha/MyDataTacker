package sk.maverick.harsha.mydatatacker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by Harsha on 5/7/2015.
 */
public class lowBatteryReceiver extends BroadcastReceiver {
    public static final String PREFS_NAME = "myprefs";

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Low Battery", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedpreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("battery_status","low");
        editor.commit();

        Intent it;
        it = new Intent(context, manageConnection.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
}
