package com.ringolatechapps.ringonotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GRID_ADAPTER extends ArrayAdapter<dataModel> {

    public GRID_ADAPTER(Context context, ArrayList<dataModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_layout, parent, false);
        }
        dataModel mdatamodel = getItem(position);
        TextView mHeading = listitemView.findViewById(R.

                id.heading);
        TextView mData = listitemView.findViewById(R.id.data);
        TextView mDate = listitemView.findViewById(R.id.date_id);

        mHeading.setText(mdatamodel.getmFileName());
        mData.setText(mdatamodel.getmData());
        mDate.setText(getMonthh(mdatamodel.getDate().getMonth() + 1) + " " + String.valueOf(mdatamodel.getDate().getDate()));

        return listitemView;
    }

    String getMonthh(int i) {
        String month;
        switch (i) {
            case 0:
                month = "January";
                break;

            case 1:
                month = "February";
                break;

            case 2:
                month = "March";
                break;

            case 3:
                month = "April";
                break;

            case 4:
                month = "May";
                break;

            case 5:
                month = "June";
                break;

            case 6:
                month = "July";
                break;

            case 7:
                month = "August";
                break;

            case 8:
                month = "September";
                break;

            case 9:
                month = "October";
                break;

            case 10:
                month = "November";
                break;

            case 11:
                month = "December";
                break;

            default:
                month = "";


        }
        return month;
    }
}
