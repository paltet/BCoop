package com.bcoop.bcoop.ui.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore firestore;
    final List<String> habilitatsUsuari = new ArrayList<>();
    private Button dbAdd;
    private Button dbAddItem;
    private EditText dbItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habilitats_configuration);
        listView = (ListView) findViewById(R.id.checkeable_list);
        btnLookup = (Button) findViewById(R.id.okButton);
        isAdmin();
        initItems();//amb firebase i posar els de per defecte

        dbAdd = findViewById(R.id.addDB);



        dbAdd.setVisibility(View.VISIBLE);
        dbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mViewDB = getLayoutInflater().inflate(R.layout.add_ability_db, null);

                final AlertDialog.Builder alertDB = new AlertDialog.Builder(HabilitatsConfiguration.this);
                alertDB.setView(mViewDB);
                final AlertDialog alertDialogDB = alertDB.create();
                alertDialogDB.setCanceledOnTouchOutside(true);

                alertDialogDB.show();
                dbItemText = mViewDB.findViewById(R.id.addAbilityDB);
                dbAddItem = mViewDB.findViewById(R.id.addAbilityDBItem);
                dbAddItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String item = dbItemText.getText().toString();
                        Map<String, Object> data = new HashMap<>();
                        data.put("nom", item);
                        db.collection("Habilitat").document(item).set(data);
                        alertDialogDB.dismiss();
                        initItems();
                    }
                });

            }
        });









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
                    final String nomHab = items.get(i).ItemString;
                    if (items.get(i).isChecked()) {

                        str += i + "\n";


                        Map<String, Object> habilitat = new HashMap<>();
                        Map<String, Object> habilitatDetall = new HashMap<>();
                        habilitatDetall.put("nom", nomHab);
                        habilitatDetall.put("valoracio", 0);
                        habilitat.put("habilitats." + nomHab, habilitatDetall);
                        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        if (email != null) {
                            final DocumentReference ref = db.collection("Usuari").document(email);
                            ref.update(habilitat);

                        }

                    }
                    else {
                        for (int j = 0 ; j < habilitatsUsuari.size() ; j++) {
                            if (habilitatsUsuari.get(j).equals(items.get(i).ItemString)){
                                final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                if (email != null) {
                                    final DocumentReference ref = db.collection("Usuari").document(email);
                                    Map<String,Object> updates = new HashMap<>();
                                    updates.put("habilitats." + nomHab, FieldValue.delete());
                                    ref.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Updated successfully");
                                            }
                                            else Log.d(TAG, "Cached get failed: ", task.getException());
                                        }
                                    });

                                }
                            }
                        }


                    }
                }
                Toast.makeText(HabilitatsConfiguration.this, str, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HabilitatsConfiguration.this, ConfigProfileActivity.class);
                startActivity(intent);
            }
        });
    }


    private void isAdmin() {

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        firestore = FirebaseFirestore.getInstance();
        final boolean[] adminlocal = {true};
        final DocumentReference documentReference = firestore.collection("Usuari").document(email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Usuari usuari = new Usuari();
                    usuari = documentSnapshot.toObject(Usuari.class);
                    adminlocal[0] = usuari.isEsAdministrador();
                    if(adminlocal[0])dbAdd.setVisibility(View.VISIBLE);
                    else dbAdd.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    //amb firestore i posarlos a checeked els que tingui ja el user
    private void initItems(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        firestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firestore.collection("Usuari").document(email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Usuari usuari = new Usuari();
                    usuari = documentSnapshot.toObject(Usuari.class);
                    Map<String, HabilitatDetall> detallHabilitatUsuari = usuari.getHabilitats();
                    for (Map.Entry<String, HabilitatDetall> entry : detallHabilitatUsuari.entrySet()) {
                        habilitatsUsuari.add(entry.getKey());
                        Log.d(TAG, "DocumentSnapshot data: " + entry.getKey());
                    }
                    for (int i = 0 ; i < habilitatsUsuari.size() ; i++) {
                        Log.d("value is", habilitatsUsuari.get(i));
                    }
                    items = new ArrayList<Item>();

                    db.collection("Habilitat")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            boolean b = false;
                                            int i = 0;
                                            while(i < habilitatsUsuari.size() && !b) {
                                                if (habilitatsUsuari.get(i).equals(document.getString("nom"))){
                                                    b = true;
                                                }
                                                ++i;
                                            }
                                            Item item = new Item(document.getString("nom"), b);
                                            items.add(item);
                                        }
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                    myItemsListAdapter = new ItemsListAdapter(HabilitatsConfiguration.this, items);
                                    listView.setAdapter(myItemsListAdapter);
                                }
                            });
                }
            }
        });
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
