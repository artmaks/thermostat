package com.example.tema.thermostat;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;



//TODO: изменить цвет plusButton, minusButton, когда они в режиме недоступности setEnabled(false)
public class MainActivity extends AppCompatActivity {

    public static float targetTemperature;
    public static float currentTemperature;

    private TemperatureManager manager;
    private MainListAdapter adapter;
    private boolean firstLaunch;
    final Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!readJson()){
            manager=new TemperatureManager();
        }
        currentTemperature = manager.current_temp;
        TextView currentTemp = (TextView) findViewById(R.id.textView);
        currentTemp.setText("Current: " + String.format(Locale.ENGLISH, "%.1f", currentTemperature) + "º");
        DateFormat df = new SimpleDateFormat("HH:mm");
        ((TextView) findViewById(R.id.nowTime)).setText("Now: " + df.format(manager.currentTime));
        firstLaunch = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        TextView vacation = (TextView) findViewById(R.id.vacation_text);
        Button btnOff = (Button) findViewById(R.id.vacationOff);

        if (!TemperatureManager.isVacationMode) {
            initializeUsualMode();
            vacation.setVisibility(View.GONE);
            btnOff.setVisibility(View.GONE);
        } else {
            vacation.setVisibility(View.VISIBLE);
            btnOff.setVisibility(View.VISIBLE);
            vacation.setText("Vacation mode is on");
            initializeVacationMode();
        }
    }


    private String getJson(){
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(TemperatureManager.class, new TemperatureManagerSerialiser());
        gsonBuilder.setPrettyPrinting();
        final Gson gson = gsonBuilder.create();

        return gson.toJson(manager);
    }

    public void saveJson(){
        File myFile = new File(getFilesDir(),"thermostat_property.json");
        FileOutputStream fOut;
        try {
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
            myOutWriter.write(getJson());
            myOutWriter.close();
            fOut.close();
        }
        catch (Exception e){

        }

    }

    @Override
    protected void onSaveInstanceState (Bundle outState){
        saveJson();
    }

    public boolean  readJson(){
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(TemperatureManager.class, new TemperatureManagerDesealiser());
        final Gson gson = gsonBuilder.create();
        File myFile = new File(getFilesDir(),"thermostat_property.json");
        try {
        if (myFile.exists()) {
            FileInputStream fIn = new FileInputStream(myFile);
            Reader myReader = new InputStreamReader(fIn);
            manager=gson.fromJson(myReader, TemperatureManager.class);
            myReader.close();
            return true;
        }} catch (Exception e){

        }
        return false;
    }
    @Override
    protected void onPause() {
        super.onPause();
        myHandler.removeCallbacks(updateDays);
    }

    public void initializeVacationMode() {
        ListView listView = (ListView) findViewById(R.id.mainListView);
        listView.setVisibility(View.GONE);

        ImageButton plus=(ImageButton)findViewById(R.id.plusButton);
        ImageButton minus=(ImageButton)findViewById(R.id.minusButton);
        plus.setEnabled(false);
        minus.setEnabled(false);

        ((TextView) findViewById(R.id.nowTime)).setVisibility(View.GONE);

        targetTemperature = TemperatureManager.vacation_temp;
        initializeView();
        myHandler.postDelayed(updateCurTempInVacationMode, 2000);

    }

    private Runnable updateCurTempInVacationMode=new Runnable() {
        @Override
        public void run() {
            setUpdateCurrentTemp();
            myHandler.postDelayed(this, 2000);
        }
    };

    public void initializeUsualMode() {
        if (firstLaunch) {
            firstLaunch = false;
        } else {
            manager.updateAfterReturn();
        }
        ImageButton plus=(ImageButton)findViewById(R.id.plusButton);
        ImageButton minus=(ImageButton)findViewById(R.id.minusButton);
        plus.setEnabled(true);
        minus.setEnabled(true);

        adapter = new MainListAdapter(this, manager.getNextDays());
        ListView listView = (ListView) findViewById(R.id.mainListView);
        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);

        targetTemperature = manager.getTargetTemprature();
        initializeView();

        myHandler.postDelayed(updateDays, 1000);
    }

    public static double logb(double a, double b) {
        return Math.log(a) / Math.log(b);
    }

    public static double log2(double a) {
        return logb(a, 2);
    }

    private Runnable updateDays = new Runnable() {
        @Override
        public void run() {
            DateFormat df = new SimpleDateFormat("HH:mm dd");
            manager.incrementcurrentTime(300000);
            ((TextView) findViewById(R.id.nowTime)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.nowTime)).setText("Now: " + df.format(manager.currentTime));

            ImageView currPeriod = (ImageView)findViewById(R.id.currentPeriod);
            if(manager.isDayPeriod()) {
                currPeriod.setImageResource(R.drawable.puresun);
            } else {
                currPeriod.setImageResource(R.drawable.purenight);
            }

            // update current temp
            setUpdateCurrentTemp();

            if (manager.isAnotherDay()) {
                // update lis of days
                updateDays();
            }

            if (manager.isNewPeriod()) {
                // update target temp
                updateTemp();
            }

            myHandler.postDelayed(this, 1000);
        }
    };

    public void setUpdateCurrentTemp(){
        TextView currentTemp = (TextView) findViewById(R.id.textView);

        float epsilon = 0.01f;
        float differ = Math.abs(targetTemperature - currentTemperature);

        if (differ > epsilon) {

            if (differ <= 2.01f) {
                currentTemperature = targetTemperature;
            } else {
                float change = (float) differ / (float) log2(differ);
                currentTemperature = currentTemperature > targetTemperature ? currentTemperature - change : currentTemperature + change;
            }

            currentTemp.setText("Current: " + String.format(Locale.ENGLISH, "%.1f", currentTemperature) + "º");
        } else  if (TemperatureManager.isVacationMode && myHandler!=null){
            myHandler.removeCallbacks(updateCurTempInVacationMode);
        }

    }
    public void updateTemp() {
        targetTemperature = manager.getTargetTemprature();
        initializeView();
    }

    public void updateDays() {
        adapter.clear();
        adapter.addAll(manager.getNextDays());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_week_schedule) {
            Intent intent = new Intent(MainActivity.this, WeekSchedule.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_vacation_mode) {
            Intent intent = new Intent(MainActivity.this, VacationMode.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static float target_t=0.0f;

    public void initializeView() {
        TextView target = (TextView) findViewById(R.id.targetText);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        target.setText(df.format(targetTemperature) + "º");

        ImageButton plusButton = (ImageButton) findViewById(R.id.plusButton);
        ImageButton minusButton = (ImageButton) findViewById(R.id.minusButton);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementTarget();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementTarget();
            }
        });

        minusButton.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        target_t=targetTemperature;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        targetTemperature=target_t;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    decrementLongTarget();
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
                        target_t=targetTemperature;
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        targetTemperature=target_t;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    incrementLongTarget();
                    mHandler.postDelayed(this, 100);
                }
            };
        });
    }

    /**
     * Увеличить target (Нажатие кнопки +)
     */
    public void incrementTarget() {

        //check Limits of temp (thirty degrees)
        float epsilon = 0.01f;
        if (Math.abs(targetTemperature - 30) < epsilon) {
            return;
        }

        TextView target = (TextView) findViewById(R.id.targetText);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        targetTemperature += 0.1;
        target.setText(df.format(targetTemperature));

    }

    public void incrementLongTarget() {

        //check Limits of temp (thirty degrees)
        float epsilon = 0.01f;
        if (Math.abs(target_t - 30) < epsilon) {
            return;
        }

        TextView target = (TextView) findViewById(R.id.targetText);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        target_t += 0.1;
        target.setText(df.format(target_t));
    }

    /**
     * Уменьшить target (Нажатие кнопки -)
     */
    public void decrementTarget() {

        //check limits of temp (five degrees)
        float epsilon = 0.1f;
        if (Math.abs(targetTemperature - 5) < epsilon) {
            return;
        }

        TextView target = (TextView) findViewById(R.id.targetText);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        targetTemperature -= 0.1;
        target.setText(df.format(targetTemperature));
    }

    public void decrementLongTarget(){
        //check limits of temp (five degrees)
        float epsilon = 0.1f;
        if (Math.abs(target_t - 5) < epsilon) {
            return;
        }

        TextView target = (TextView) findViewById(R.id.targetText);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        target_t -= 0.1;
        target.setText(df.format(target_t));
    }

    public void vacationOff(View v) {
        TemperatureManager.isVacationMode = false;
        onResume();
    }
}
