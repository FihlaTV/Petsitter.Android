package com.example.katsutoshi.petsitter.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.model.Pet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Katsutoshi on 24/03/2017.
 */

public class PetMenuViewAdapter extends BaseAdapter {

    Activity activity;
    List<String> options = new ArrayList<>();
    LayoutInflater inflater;

    public PetMenuViewAdapter(Activity activity) {
        this.activity = activity;
        this.options = Opcoes();
    }

    private List<String> Opcoes() {
        List<String> options = new ArrayList<>();
        options.add("Saúde");
        options.add("Notificação");
        options.add("Agenda");
        options.add("Histórico");
        //options.add("Dados do Animal");
        return options;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Object getItem(int position) {
        return options.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater)activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listview_item_pet_menu,null);

        ImageView img = (ImageView)itemView.findViewById(R.id.imageViewIco);
        TextView txtName = (TextView)itemView.findViewById(R.id.txtOption);

        txtName.setText(options.get(position));

        switch(position)
        {
            case 0:
                img.setImageResource(R.drawable.stethoscope);
                break;
            case 1:
                img.setImageResource(R.drawable.notification);
                break;
            case 2:
                img.setImageResource(R.drawable.notebook);
                break;
            case 3:
                img.setImageResource(R.drawable.history);
                break;
            /*
            case 5:
                img.setImageResource(R.drawable.pawprint);
                break;
                */
        }
        return itemView;
    }

}

