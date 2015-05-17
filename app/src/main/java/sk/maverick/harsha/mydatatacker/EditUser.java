/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

package sk.maverick.harsha.mydatatacker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



public class EditUser extends ActionBarActivity {

    public static final String PREFS_NAME = "myprefs";
    int quota, family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);

        EditText name = (EditText) findViewById(R.id.firstname_edituser_quota);
        EditText phone = (EditText) findViewById(R.id.phonenumber_useredit_edtxt);
        SeekBar seekBar = (SeekBar) findViewById(R.id.setquota_edituser_seek);
        Button update = (Button) findViewById(R.id.update_edituser_btn);
        final TextView remaining = (TextView) findViewById(R.id.remaining_edituser_text);

        Intent it = getIntent();

        String firstname= it.getStringExtra("Name");
        String phonenum = it.getStringExtra("Phone");
         String temp = it.getStringExtra("DataUsed");

        quota = Integer.parseInt(temp);
        
        name.setText(firstname);
        phone.setText(phonenum);
        seekBar.setProgress(quota);

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        family = sharedpreferences.getInt("family_limit", 10);
        remaining.setText("" + family);

        seekBar.setMax(quota+family);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(progress < quota){

                    add(progress);
                    remaining.setText(""+family);

                }else{
                    subtract(progress);
                    remaining.setText("" + family);

                }

            }




            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


    }

    private void subtract(int progress) {

        family = family - (progress -quota);

    }

    private void add(int progress) {

        family = family + (quota - progress);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_user, menu);
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
}
