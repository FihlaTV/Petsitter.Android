package com.example.katsutoshi.petsitter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.model.Agenda;

import java.util.List;

/**
 * Created by Katsutoshi on 19/06/2017.
 */

public class AgendaListViewAdapter extends BaseAdapter {

    Activity activity;
    List<Agenda> lstAgenda;
    LayoutInflater inflater;


    public AgendaListViewAdapter(Activity activity, List<Agenda> lstAgenda) {
        this.activity = activity;
        this.lstAgenda = lstAgenda;
    }

    @Override
    public int getCount() {
        return lstAgenda.size();
    }

    @Override
    public Object getItem(int position) {
        return lstAgenda.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater)activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_view_agenda,null);

        TextView txtName = (TextView)itemView.findViewById(R.id.txtTitle);
        TextView txtDesc = (TextView)itemView.findViewById(R.id.txtDescription);
        TextView txtDate = (TextView)itemView.findViewById(R.id.txtDate);

        txtDesc.setText("Descrição:" + lstAgenda.get(position).getDescription());
        txtName.setText("Atividade:" + lstAgenda.get(position).getNotificationTitle());
        txtDate.setText("Data:" + lstAgenda.get(position).getDate() + " " + lstAgenda.get(position).getTime());

        return itemView;
    }
}
