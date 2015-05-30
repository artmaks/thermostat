package com.example.tema.thermostat;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {

    public static float targetTemperature;
    private TemperatureManager manager;
    private MainListAdapter adapter;
    private boolean updateCurrentTemp;
    private boolean firstLaunch;
    final Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager=new TemperatureManager();
        firstLaunch=true;
    }


    @Override
    protected void onResume (){
        super.onResume();
        TextView vacation=(TextView)findViewById(R.id.vacation_text);

        if (!TemperatureManager.isVacationMode){
            initializeUsualMode();
            vacation.setVisibility(View.GONE);
        }
        else {
            vacation.setVisibility(View.VISIBLE);
            vacation.setText("Vacation mode is on");
            initializeVacationMode();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myHandler.removeCallbacks(updateDays);
    }

    public void initializeVacationMode(){
        ListView listView = (ListView)findViewById(R.id.mainListView);
        listView.setVisibility(View.GONE);

        TextView currentTemp=(TextView)findViewById(R.id.textView);
        targetTemperature=TemperatureManager.vacation_temp;
        initializeView();
        currentTemp.setText("Current: " + String.format(Locale.ENGLISH, "%.1f", targetTemperature));
    }

    public void initializeUsualMode(){
        if (firstLaunch){
            firstLaunch=false;
        }
        else {
            manager.updateAfterReturn();
        }

        adapter = new MainListAdapter(this, manager.getNextDays());
        ListView listView = (ListView)findViewById(R.id.mainListView);
        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);

        updateCurrentTemp=true;

        targetTemperature=manager.getTargetTemprature();
        initializeView();

        myHandler.postDelayed(updateDays, 1000);
    }

    private  Runnable updateDays=new Runnable() {
        @Override
        public void run() {
            final TextView currentTemp=(TextView)findViewById(R.id.textView);
            manager.incrementcurrentTime(300000);

            if (updateCurrentTemp) {
                updateCurrentTemp = false;
                currentTemp.setText("Current: " + String.format(Locale.ENGLISH, "%.1f", targetTemperature));
            }

            if (manager.isAnotherDay()) {
                // update lis of days
                updateDays();
            }

            if (manager.isNewPeriod()) {
                // update target temp
                updateTemp();
                // update current temperature next tick
                updateCurrentTemp = true;
            }

            myHandler.postDelayed(this, 1000);
        }
    };

    public void updateTemp(){
        targetTemperature = manager.getTargetTemprature();
        initializeView();
    }

    public void updateDays(){
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

    public void initializeView() {
        TextView target = (TextView)findViewById(R.id.targetText);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        target.setText(df.format(targetTemperature));
    }

    /**
     * Увеличить target (Нажатие кнопки +)
     */
    public void incrementTarget(View v) {

        //check Limits of temp (thirty degrees)
        float epsilon=0.01f;
        if (Math.abs(targetTemperature-30)<epsilon){
            return;
        }

        TextView target = (TextView)findViewById(R.id.targetText);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        targetTemperature += 0.1;
        target.setText(df.format(targetTemperature));
        updateCurrentTemp=true;
    }


    /**
     * Уменьшить target (Нажатие кнопки -)
     */
    public void decrementTarget(View v) {

        //check limits of temp (five degrees)
        float epsilon=0.1f;
        if (Math.abs(targetTemperature-5)<epsilon){
            return;
        }

        TextView target = (TextView)findViewById(R.id.targetText);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        targetTemperature -= 0.1;
        target.setText(df.format(targetTemperature));
        updateCurrentTemp=true;
    }
}
