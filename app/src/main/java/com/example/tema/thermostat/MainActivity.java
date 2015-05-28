package com.example.tema.thermostat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView t=(TextView)findViewById(R.id.textView);

        //t.setText(new SimpleDateFormat("EEEE").format(7));

        TemperatureManager manager=new TemperatureManager((TextView)findViewById(R.id.textView3));
        MainListAdapter adapter = new MainListAdapter(this, manager.getNextDays());
        ListView listView = (ListView)findViewById(R.id.mainListView);
        listView.setAdapter(adapter);



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

    private ArrayList<Item> generateData(){
        ArrayList<Item> models = new ArrayList<Item>();
        models.add(new Item("Today (17 April)"));
        models.add(new Item("Now - 16:00", "20.0"));
        models.add(new Item("16:00 - 22:00", "18.0"));
        models.add(new Item("22:00 - 23:59", "20.0"));
        models.add(new Item("18 April"));
        models.add(new Item("Now - 16:00", "20.0"));
        models.add(new Item("16:00 - 22:00", "18.0"));
        models.add(new Item("22:00 - 23:59", "20.0"));
        models.add(new Item("19 April"));
        models.add(new Item("Now - 16:00", "20.0"));
        models.add(new Item("16:00 - 22:00", "18.0"));
        models.add(new Item("22:00 - 23:59", "20.0"));
        return models;
    }
}
