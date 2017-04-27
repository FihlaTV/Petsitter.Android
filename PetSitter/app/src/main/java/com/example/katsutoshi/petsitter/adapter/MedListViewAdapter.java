package com.example.katsutoshi.petsitter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.model.Medication;
import com.example.katsutoshi.petsitter.model.Pet;

import java.util.List;

/**
 * Created by Katsutoshi on 23/04/2017.
 */

public class MedListViewAdapter extends BaseAdapter {

    Activity activity;
    List<Medication> lstMeds;
    LayoutInflater inflater;

    public MedListViewAdapter(Activity activity, List<Medication> lstMeds) {
        this.activity = activity;
        this.lstMeds = lstMeds;
    }

    @Override
    public int getCount() {
        return lstMeds.size();
    }

    @Override
    public Object getItem(int position) {
        return lstMeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater)activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listview_item_med,null);

        TextView txtName = (TextView)itemView.findViewById(R.id.txtMedName);
        TextView txtDesc = (TextView)itemView.findViewById(R.id.txtMedDesc);

        txtDesc.setText(lstMeds.get(position).getDescription());
        txtName.setText(lstMeds.get(position).getVetName());

        return itemView;
    }

}
