package com.example.tema.thermostat;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;


public class WeekSchedule extends ActionBarActivity {

    private float day_temp;
    private float night_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView)findViewById(R.id.staticDayListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(WeekSchedule.this, DaySchedule.class);

                DaySchedule.day=id+1==7?0:(int)id+1;
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume (){
        super.onResume();
        day_temp=TemperatureManager.day_temper;
        night_temp=TemperatureManager.night_temper;


        TextView dayDegr=(TextView)findViewById(R.id.dayTemp);
        TextView nightDegr=(TextView)findViewById(R.id.nightTemp);
        ImageButton nightMin=(ImageButton)findViewById(R.id.minusNightButton);
        ImageButton nightPlus=(ImageButton)findViewById(R.id.plusNightButton);
        ImageButton dayMin=(ImageButton)findViewById(R.id.minusDayButton);
        ImageButton dayPlus=(ImageButton)findViewById(R.id.plusDayButton);

        initializeView(dayDegr, true, dayPlus, dayMin);
        initializeView(nightDegr, false, nightPlus, nightMin);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TemperatureManager.day_temper=day_temp;
        TemperatureManager.night_temper=night_temp;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_week_schedule, menu);
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

    public void initializeView(final TextView target, final boolean isDay, ImageButton plusButton, ImageButton minusButton) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        if (isDay){
            target.setText(df.format(day_temp));
        } else {
            target.setText(df.format(night_temp));
        }


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementTarget(target, isDay);
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementTarget(target, isDay);
            }
        });

        minusButton.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    decrementTarget(target, isDay);
                    mHandler.postDelayed(this, 100);
                }
            };
        });

        plusButton.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    incrementTarget(target, isDay);
                    mHandler.postDelayed(this, 100);
                }
            };
        });
    }


    public void incrementTarget(TextView target, boolean isDay) {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        float target_temp=isDay?day_temp:night_temp;
        //check Limits of temp (thirty degrees)
        float epsilon=0.01f;
        if (Math.abs(target_temp-30)<epsilon){
            return;
        }

        if (isDay){
            day_temp += 0.1;
            target.setText(df.format(day_temp));
        } else {
            night_temp += 0.1;
            target.setText(df.format(night_temp));
        }

    }



    public void decrementTarget(TextView target, boolean isDay) {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        float target_temp=isDay?day_temp:night_temp;
        //check Limits of temp (thirty degrees)
        float epsilon=0.01f;
        if (Math.abs(target_temp-5)<epsilon){
            return;
        }

        if (isDay){
            day_temp -= 0.1;
            target.setText(df.format(day_temp));
        } else {
            night_temp -= 0.1;
            target.setText(df.format(night_temp));
        }

    }
}
