package com.bcoop.bcoop.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.R;

import java.util.ArrayList;
import java.util.List;

public class HabilitatsConfiguration extends AppCompatActivity implements View.OnClickListener{

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


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.acceptButton:
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
                break;
        }
    }
}
