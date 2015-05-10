package sk.maverick.harsha.mydatatacker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Harsha on 4/29/2015.
 */
public class myReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm;
        cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();

        if(ninfo != null){

            Toast.makeText(context,"" + ninfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if((ninfo.getTypeName()).equalsIgnoreCase("WIFI")){
                Intent it  =  new Intent(context, myService.class);
                context.startService(it);
                Toast.makeText(context,"Inside WIFI", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Intent it  =  new Intent(context, myService.class);
            Toast.makeText(context,"None" , Toast.LENGTH_SHORT).show();
            context.stopService(it);
        }


    }
}
