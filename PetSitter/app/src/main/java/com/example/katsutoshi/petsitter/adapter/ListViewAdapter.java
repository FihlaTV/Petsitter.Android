package com.example.katsutoshi.petsitter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.model.Pet;

import java.util.List;

/**
 * Created by Katsutoshi on 24/03/2017.
 */

public class ListViewAdapter extends BaseAdapter {

    Activity activity;
    List<Pet> lstPets;
    LayoutInflater inflater;

    public ListViewAdapter(Activity activity, List<Pet> lstPets) {
        this.activity = activity;
        this.lstPets = lstPets;
    }

    @Override
    public int getCount() {
        return lstPets.size();
    }

    @Override
    public Object getItem(int position) {
        return lstPets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater)activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listview_item,null);

        TextView txtName = (TextView)itemView.findViewById(R.id.txtPetName);

        txtName.setText(lstPets.get(position).getName());

        return itemView;
    }

}
