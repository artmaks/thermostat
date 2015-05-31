package com.example.tema.thermostat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by tema on 21.05.15.
 */
public class MainListAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private  ArrayList<Item> modelsArrayList;
    private final boolean deleteAccess;
    private FloatingActionButton fab;

    public MainListAdapter(Context context, ArrayList<Item> modelsArrayList, boolean deleteAccess, FloatingActionButton fab) {

        super(context, R.layout.list_item, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
        this.deleteAccess = deleteAccess;
        this.fab=fab;
    }

    public MainListAdapter(Context context, ArrayList<Item> modelsArrayList) {

        super(context, R.layout.list_item, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
        this.deleteAccess = false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater


        View rowView = null;
        if(!modelsArrayList.get(position).isGroupHeader){
            rowView = inflater.inflate(R.layout.list_item, parent, false);


            // 3. Get icon,title & counter views from the rowView
            TextView period = (TextView) rowView.findViewById(R.id.periodText);
            TextView temperature = (TextView) rowView.findViewById(R.id.temperatureText);

            // 4. Set the text for textView
            period.setText(modelsArrayList.get(position).Period);
            temperature.setText(modelsArrayList.get(position).Temperature + "º");
        }
        else{
            rowView = inflater.inflate(R.layout.list_group_item, parent, false);
            TextView titleView = (TextView) rowView.findViewById(R.id.dayTitle);
            titleView.setText(modelsArrayList.get(position).Title);

        }
        if(!deleteAccess||modelsArrayList.size()==1) {
            ImageButton deleteBtn = (ImageButton) rowView.findViewById(R.id.deleteButton);
            if(deleteBtn != null)
                deleteBtn.setVisibility(View.INVISIBLE);

        } else {


            ImageButton deleteBtn = (ImageButton) rowView.findViewById(R.id.deleteButton);
            if(deleteBtn != null)
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TemperatureManager.days.get(DaySchedule.day).daysperiods.remove(position);
                        modelsArrayList.remove(position);
                        notifyDataSetChanged();

                        if (fab!=null){
                            if (!TemperatureManager.days.get(DaySchedule.day).isFull()) {
                                fab.setEnabled(true);
                            }
                        }

                        Toast toast = Toast.makeText(getContext(),
                                "Delete element " + position,
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
        }
        return rowView;
    }

}
