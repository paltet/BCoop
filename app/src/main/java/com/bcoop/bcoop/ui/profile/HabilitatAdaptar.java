package com.bcoop.bcoop.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bcoop.bcoop.Model.Comentari;
import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.R;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class HabilitatAdaptar extends BaseExpandableListAdapter {

    private List<String> habilitats;
    private Map<String, HabilitatDetall> detall;
    private Context context;

    public HabilitatAdaptar(Context context, List<String> habilitats, Map<String, HabilitatDetall> detall) {
        this.habilitats = habilitats;
        this.detall = detall;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return habilitats.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return detall.size() + 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return habilitats.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return detall.get(habilitats.get(groupPosition)).getComentaris().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String hab = (String) getGroup(groupPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.habilitats_expandable_list, null);
        TextView nomHabilitat = convertView.findViewById(R.id.habilitatText);
        nomHabilitat.setText(hab);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Comentari coment = (Comentari) getChild(groupPosition, childPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.comentari_habilitat_expandable_list, null);

        TextView comentari = convertView.findViewById(R.id.comentariText);
        comentari.setText(coment.getContingut());

        String time = changeTimeFormat(coment.getTemps());

        TextView ago = convertView.findViewById(R.id.comentariTimeText);
        ago.setText(time);

        return convertView;
    }

    private String changeTimeFormat(Date temps) {
        String timeDate = temps.toString();
        String time = timeDate.substring(8, 10);
        time = time.concat("-");
        String mes = timeDate.substring(4, 7);
        int num = convertMonth(mes);
        if (num < 10)
            time = time.concat("0");
        time = time.concat(Integer.toString(num));
        time = time.concat("-");
        time = time.concat(timeDate.substring(timeDate.length()-4));
        time = time.concat(" at ");
        time = time.concat(timeDate.substring(11, 16));
        return time;
    }

    private int convertMonth(String mes) {
        int month = 1;
        switch (mes) {
            case "Jan":
                month = 1;
                break;
            case "Feb":
                month = 2;
                break;
            case "Mar":
                month = 3;
                break;
            case "Apr":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "Jun":
                month = 6;
                break;
            case "Jul":
                month = 7;
                break;
            case "Aug":
                month = 8;
                break;
            case "Sep":
                month = 9;
                break;
            case "Oct":
                month = 10;
                break;
            case "Nov":
                month = 11;
                break;
            case "Dec":
                month = 12;
                break;
        }
        return month;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
