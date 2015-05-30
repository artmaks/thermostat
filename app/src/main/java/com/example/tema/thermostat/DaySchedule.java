package com.example.tema.thermostat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;


public class DaySchedule extends ActionBarActivity {

    Calendar StartdateAndTime = Calendar.getInstance();
    Calendar EnddateAndTime = Calendar.getInstance();
    TextView startText;
    TextView endText;
    boolean type;

    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            if(type) {
                StartdateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                StartdateAndTime.set(Calendar.MINUTE, minute);
                startText.setText(hourOfDay + ":" + minute);
            } else {
                EnddateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                EnddateAndTime.set(Calendar.MINUTE, minute);
                endText.setText(hourOfDay + ":" + minute);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        MainListAdapter adapter = new MainListAdapter(this, generateData());
        ListView listView = (ListView)findViewById(R.id.dayListView);
        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(1);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Custom dialog");
        // создаем view из dialog.xml
        LinearLayout view = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialogperiod, null);
        // устанавливаем ее, как содержимое тела диалога
        adb.setView(view);
        startText = (TextView)view.findViewById(R.id.startText);
        endText = (TextView)view.findViewById(R.id.endText);
        Button closeBtn = (Button)view.findViewById(R.id.closeDialog);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog(1);
            }
        });

        Button addBtn = (Button)view.findViewById(R.id.addPeriodButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Запомнить период
                Toast toast = Toast.makeText(getApplicationContext(),
                        "New period: from" + StartdateAndTime.get(Calendar.HOUR_OF_DAY) + ":" + StartdateAndTime.get(Calendar.MINUTE)
                        + " to " + EnddateAndTime.get(Calendar.HOUR_OF_DAY) + ":" + EnddateAndTime.get(Calendar.MINUTE),
                        Toast.LENGTH_SHORT);
                toast.show();
                dismissDialog(1);
            }
        });
        return adb.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Item> generateData(){
        ArrayList<Item> models = new ArrayList<Item>();
        models.add(new Item("Now - 16:00", "20.0"));
        models.add(new Item("16:00 - 22:00", "18.0"));
        models.add(new Item("22:00 - 23:59", "20.0"));
        return models;
    }

    public void chooseStart(View v) {
        openDialog(StartdateAndTime);
        type = true;
    }

    public void chooseEnd(View v) {
        openDialog(EnddateAndTime);
        type = false;
    }

    void openDialog(Calendar dateAndTime) {
        new TimePickerDialog(DaySchedule.this,
                t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE),
                true).show();
    }

}
