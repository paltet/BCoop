package com.bcoop.bcoop.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bcoop.bcoop.R;

import java.util.List;

public class ReportAdaptar extends BaseAdapter {

    private Context context;
    private List<String> informText;
    private List<String> informDate;
    private boolean empty = false;

    public ReportAdaptar(Context context, List<String> informText, List<String> informDate) {
        this.context = context;
        this.informText = informText;
        this.informDate = informDate;
    }

    @Override
    public int getCount() {
        if (informText.isEmpty()) {
            empty = true;
            return 1;
        }
        return informText.size();
    }

    @Override
    public Object getItem(int position) {
        return informText.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_single_report, null);
        TextView message = convertView.findViewById(R.id.reportBody);
        TextView date = convertView.findViewById(R.id.reportDate);
        if (empty) {
            message.setText(R.string.no_reports);
            date.setText("");
        }
        else {
            message.setText(informText.get(position));
            date.setText(informDate.get(position));
        }
        return convertView;
    }
}
