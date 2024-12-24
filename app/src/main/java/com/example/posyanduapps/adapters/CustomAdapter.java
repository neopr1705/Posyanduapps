package com.example.posyanduapps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.posyanduapps.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> reminderList;

    public CustomAdapter(Context context, List<String> reminderList) {
        super(context, 0, reminderList);  // passing the context, layout resource and data
        this.context = context;
        this.reminderList = reminderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.reminder_list_item, parent, false);
        }

        // Get the data item for this position
        String reminder = getItem(position);

        // Lookup view for data population
        TextView tvReminder = convertView.findViewById(R.id.tvReminder);

        // Populate the data into the template view using the data object
        tvReminder.setText(reminder);

        // Return the completed view to render on screen
        return convertView;
    }
}
