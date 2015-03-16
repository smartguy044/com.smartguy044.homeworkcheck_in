package com.smartguy044.homeworkcheck_in;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ClassList extends Activity {

    ListView lv;
    Cursor c;
    String kids[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
    int i = 0;
    int t = 0;
    SQLiteDatabase db;
    final Context context = this;
    ArrayAdapter<String> adapter;

    //ADD OVERFLOW MENU TO APP
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_list);
        lv = (ListView) findViewById(R.id.listView1);

        //PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        // Declaring arrayadapter to store the items and return them as a view
        adapter = new ArrayAdapter<String>(this,
               R.layout.multiple_choice, kids);

        db = openOrCreateDatabase("CLASS", Context.MODE_PRIVATE, null);

        //READING THE DATA FROM THE TABLE CLASS AND SETTING IN AN ARRAYADAPTER
        c = db.rawQuery("SELECT * FROM CLASS; ", null);
        i = 0;

        while (c.moveToNext()) {

            kids[i] = c.getString(0);
            i++;
        }
        t = i;

        lv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //GOING BACK TO MAIN ACTIVITY
            case R.id.back:
                startActivity(new Intent(getApplicationContext(),
                     MainActivity.class));
            return true;

            //CLEAR ALL CHECK MARKS
            case R.id.clear:

                lv.clearChoices();
                for (int i = 0; i < lv.getCount(); i++)
                    lv.setItemChecked(i, false);
                return true;

            //ERASE CLASS LIST
            case R.id.erase:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Erase Class List");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to ERASE the whole class list?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, erase
                                // the entire class list

                                db = openOrCreateDatabase("CLASS", Context.MODE_PRIVATE,
                                        null);

                                //DELETE ALL THE DATA FROM TABLE
                                db.delete("CLASS", null, null);

                                db.close();

                                for (int j = 0; j < t; j++) {
                                    kids[j] = "";
                                    lv.setAdapter(adapter);
                                }
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;

            //GOING TO SETTINGS
            /*case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}