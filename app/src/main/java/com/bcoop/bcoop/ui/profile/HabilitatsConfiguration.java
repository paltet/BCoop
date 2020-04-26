package com.bcoop.bcoop.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.R;

import java.util.ArrayList;
import java.util.List;

public class HabilitatsConfiguration extends AppCompatActivity {

    public class Item {
        boolean checked;
        String ItemString;

        Item(String t, boolean b) {
            ItemString = t;
            checked = b;
        }

        public boolean isChecked() {
            return checked;
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView text;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.habilitats_total_item, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                viewHolder.text = (TextView) rowView.findViewById(R.id.nomHabilitat);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;
                    Toast.makeText(getApplicationContext(),
                            itemStr + "setOnClickListener\nchecked: " + newState,
                            Toast.LENGTH_LONG).show();
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
    }

    Button btnLookup;
    List<Item> items;
    ListView listView;
    ItemsListAdapter myItemsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habilitats_configuration);
        listView = (ListView) findViewById(R.id.checkeable_list);
        btnLookup = (Button) findViewById(R.id.okButton);

        initItems();//amb firebase i posar els de per defecte
        myItemsListAdapter = new ItemsListAdapter(this, items);
        listView.setAdapter(myItemsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(HabilitatsConfiguration.this,
                        ((Item) (parent.getItemAtPosition(position))).ItemString,
                        Toast.LENGTH_LONG).show();
            }
        });

        btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "Check items:\n";

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isChecked()) {
                        str += i + "\n";
                    }
                }
                Toast.makeText(HabilitatsConfiguration.this,
                        str,
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(HabilitatsConfiguration.this, ConfigProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    //amb firestore i posarlos a checeked els que tingui ja el user
    private void initItems(){
        items = new ArrayList<Item>();
        TypedArray arrayText = getResources().obtainTypedArray(R.array.abilitiesUser);

        for(int i=0; i<arrayText.length(); i++){
            String s = arrayText.getString(i);
            boolean b = false;
            if(i == 5)b = true;
            Item item = new Item(s, b);
            items.add(item);
        }
        arrayText.recycle();
    }

}

/*
    List<String> selectedItems;
    private Button acceptButton;
    ListView listViewHabilitats;
    ArrayAdapter<String> adapter;

    //shan dagafar de firestore
    String habilitatsTotals[] = {"Habilitat1", "Habilitat2", "Habilitat3", "Habilitat4", "Habilitat5", "Habilitat6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habilitats_configuration);

        listViewHabilitats = findViewById(R.id.checkeable_list);
        acceptButton = findViewById(R.id.okButton);
        acceptButton.setOnClickListener(this);

        adapter = new ArrayAdapter<>(this, R.layout.habilitats_total_item, R.id.nomHabilitat, habilitatsTotals);
        listViewHabilitats.setAdapter(adapter);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItems = new ArrayList<>();
                SparseBooleanArray itemChecked = listViewHabilitats.getCheckedItemPositions();
                for(int i = 0; i < itemChecked.size(); i++){
                    int key = itemChecked.keyAt(i);
                    boolean value = itemChecked.get(key);
                    if(value){
                        selectedItems.add(listViewHabilitats.getItemAtPosition(key).toString());
                    }
                }
                //comprovem si els agafa bé
                if(selectedItems.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No hi ha cap habilitat seleccionada", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Habilitats seleccionades:"+selectedItems, Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.acceptButton:
                selectedItems = new ArrayList<>();
                SparseBooleanArray itemsChecked = listViewHabilitats.getCheckedItemPositions();
                for(int i = 0; i < itemsChecked.size(); i++){
                    int key = itemsChecked.keyAt(i);
                    boolean value = itemsChecked.get(key);
                    if(value){
                        selectedItems.add(listViewHabilitats.getItemAtPosition(key).toString());
                    }
                }
                //comprovem si els agafa bé
                if(selectedItems.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No hi ha cap habilitat seleccionada", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Habilitats seleccionades:"+selectedItems, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }*/
