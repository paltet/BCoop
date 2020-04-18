package com.bcoop.bcoop.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.R;

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
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return habilitats.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return detall.get(habilitats.get(groupPosition));
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
        // TextView nomHabilitat = convertView.findViewById(R.id.nomHab)
        // nomHabilitat.setText(hab);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        HabilitatDetall detallHab = (HabilitatDetall) getChild(groupPosition, childPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.comentari_habilitat_expandable_list, null);
        // TextView valoracio = convertView.findViewById(R.id.valoracio)
        // valoracio.setText(detallHab.getNom());
        // listView comentari = convertView.findViewById(R.id.valoracio)
        // pintar comentaris
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
