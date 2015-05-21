package com.example.tema.thermostat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tema on 21.05.15.
 */
public class MainListAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private final ArrayList<Item> modelsArrayList;

    public MainListAdapter(Context context, ArrayList<Item> modelsArrayList) {

        super(context, R.layout.list_item, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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
            temperature.setText(modelsArrayList.get(position).Temperature);
        }
        else{
            rowView = inflater.inflate(R.layout.list_group_item, parent, false);
            TextView titleView = (TextView) rowView.findViewById(R.id.dayTitle);
            titleView.setText(modelsArrayList.get(position).Title);

        }
        return rowView;
    }

}
