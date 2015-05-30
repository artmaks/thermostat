package com.example.tema.thermostat;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;


public class VacationMode extends ActionBarActivity {

    private static float target_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_mode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vacation_mode, menu);
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

    @Override
    protected void onResume (){
        super.onResume();
        Switch mySwitch=(Switch)findViewById(R.id.switch1);

        mySwitch.setChecked(TemperatureManager.isVacationMode);

        // handle switch
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    TemperatureManager.isVacationMode=true;
                    ((TextView)findViewById(R.id.vacationNow)).setText("Now: on");
                } else {
                    TemperatureManager.isVacationMode=false;
                    ((TextView)findViewById(R.id.vacationNow)).setText("Now: off");
                }
            }
        });

        target_temp=TemperatureManager.vacation_temp;
        initializeView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (TemperatureManager.isVacationMode){
            TemperatureManager.vacation_temp=target_temp;
        }
    }


    public void initializeView() {
        TextView target = (TextView)findViewById(R.id.textView9);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        target.setText(df.format(target_temp));
    }


    public void incrementTarget(View v) {

        //check Limits of temp (thirty degrees)
        float epsilon=0.01f;
        if (Math.abs(target_temp-30)<epsilon){
            return;
        }

        TextView target = (TextView)findViewById(R.id.textView9);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        target_temp += 0.1;
        target.setText(df.format(target_temp));

    }



    public void decrementTarget(View v) {

        //check limits of temp (five degrees)
        float epsilon=0.1f;
        if (Math.abs(TemperatureManager.vacation_temp-5)<epsilon){
            return;
        }

        TextView target = (TextView)findViewById(R.id.textView9);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        target_temp -= 0.1;
        target.setText(df.format(target_temp));

    }
}
