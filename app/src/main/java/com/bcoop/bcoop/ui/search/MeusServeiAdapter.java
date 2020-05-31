package com.bcoop.bcoop.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bcoop.bcoop.Model.Servei;
import com.bcoop.bcoop.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MeusServeiAdapter extends ArrayAdapter<Servei> {

    private ArrayList<Servei> meusServeis;

    public MeusServeiAdapter(@NonNull Context context, int resource, ArrayList<Servei> meusServeis) {
        super(context, resource);
        this.meusServeis = meusServeis;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println("entro a getView");
        Servei servei = meusServeis.get(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.service_item, parent, false);

        TextView habilitat = view.findViewById(R.id.habilitat2);
        TextView proveidor= view.findViewById(R.id.proveidor2);
        TextView estat = view.findViewById(R.id.estat2);
        TextView data = view.findViewById(R.id.date2);

        habilitat.setText(servei.getHabilitat());
        proveidor.setText(servei.getProveidor());
        estat.setText(servei.getEstat());

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(servei.getDate());
        data.setText(date);


        notifyDataSetChanged();
        return view;
    }

    public int getCount(){
        return meusServeis.size();
    }
}
