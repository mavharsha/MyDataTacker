/*
 * Copyright (c)
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 *
 */

package sk.maverick.harsha.mydatatacker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;


public class manageDevices extends ListActivity {

    ListView listView;
    private ArrayList<String> songs = new ArrayList<String>();
    ArrayAdapter itemsAdapter;
    TextView remaining;
    SeekBar seekBar;
    int temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_devices);

        Button add = (Button) findViewById(R.id.managedevice_adddevice_btn);
        remaining = (TextView) findViewById(R.id.seekBar_layout_remaining);


        listView = getListView();
        songs.add("aaa");
        songs.add("bbb");
        songs.add("ccc");
        songs.add("ddd");

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songs);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.v("Listview", "" + songs.get(position));

            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(manageDevices.this, addDevices.class));
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(manageDevices.this);
                builder.setCancelable(true)
                        .setTitle("Dialog!")
                        .setMessage("Confirm!")
                        .setPositiveButton("Edit Quota!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                editQuota();

                                Log.v("Edit Quota", "Edit quota screen");
                                // startActivity(new Intent(manageDevices.this, .class));
                            }
                        }).setNegativeButton("Delete Device", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.v("Deleting device", "Delete device");

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


                Log.v("Listview onlong press" +
                        "", "" + songs.get(position));
                return false;
            }
        });

        try {

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                   temp = progress;
                    Log.v("Seekbar"," Progress " + progress);
                    remaining.setText(progress);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }catch (Exception e){

        }



    }

    private void editQuota() {

        seekBar.setProgress(temp);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.seekbar_layout);
        dialog.setTitle("Hell yeah!");
        dialog.show();


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
}
