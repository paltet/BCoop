package com.bcoop.bcoop.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcoop.bcoop.Model.Comentari;
import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import javax.xml.validation.Validator;

public class HabilitatAdaptar extends BaseExpandableListAdapter {

    private List<String> habilitats;
    private Map<String, HabilitatDetall> detall;
    private Context context;
    private Boolean onlyThree;
    private ExpandableListView expandableListView;

    public HabilitatAdaptar(Context context, List<String> habilitats, Map<String, HabilitatDetall> detall, ExpandableListView expandableListView) {
        this.habilitats = habilitats;
        this.detall = detall;
        this.context = context;
        this.onlyThree = true;
        this.expandableListView = expandableListView;
    }

    @Override
    public int getGroupCount() {
        return habilitats.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = detall.get(habilitats.get(groupPosition)).getComentaris().size();
        if (onlyThree && size > 3)
            return 4;
        return detall.get(habilitats.get(groupPosition)).getComentaris().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return habilitats.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        int size = detall.get(habilitats.get(groupPosition)).getComentaris().size();
        return detall.get(habilitats.get(groupPosition)).getComentaris().get(size - childPosition - 1);
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

        int valoracio = detall.get(habilitats.get(groupPosition)).getValoracio();

        LinearLayout estrelles = convertView.findViewById(R.id.valoracioLayout);
        for (int i = 0; i < 5; ++i) {
            View view = LayoutInflater.from(context).inflate(R.layout.star_layout, null);
            ImageView est = view.findViewById(R.id.habilitatValoracioStar1);
            if (valoracio > i)
                est.setImageResource(R.drawable.star_full);
            else est.setImageResource(R.drawable.star_empty);
            estrelles.addView(view);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (onlyThree) {
            if (childPosition < 3) {
                convertView = mostrarDades(groupPosition, childPosition, convertView);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.more_comentari_habilitat_expandable_list, null);
                ImageView more = convertView.findViewById(R.id.moreImageView);
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onlyThree = false;
                        expandableListView.collapseGroup(groupPosition);
                        expandableListView.expandGroup(groupPosition);
                    }
                });
            }
        }
        else convertView = mostrarDades(groupPosition, childPosition, convertView);

        return convertView;
    }

    private View mostrarDades(int groupPosition, int childPosition, View convertView) {
        Comentari coment = (Comentari) getChild(groupPosition, childPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.comentari_habilitat_expandable_list, null);

        TextView comentari = convertView.findViewById(R.id.comentariText);
        comentari.setText(coment.getContingut());

        TextView ago = convertView.findViewById(R.id.comentariTimeText);
        changeTimeFormat(coment.getTemps(), ago);

        return convertView;
    }


    private void changeTimeFormat(Date temps, TextView ago) {
        String timeDate = temps.toString();
        String time = timeDate.substring(8, 10);
        time = time.concat("/");
        String mes = timeDate.substring(4, 7);
        int num = convertMonth(mes);
        if (num < 10)
            time = time.concat("0");
        time = time.concat(Integer.toString(num));
        time = time.concat("/");
        time = time.concat(timeDate.substring(timeDate.length()-4));
        ago.setText(R.string.time_at);
        time = time.concat(ago.getText().toString());
        time = time.concat(timeDate.substring(11, 16));
        ago.setText(time);
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
