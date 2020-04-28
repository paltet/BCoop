package com.bcoop.bcoop.ui.prize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.bcoop.bcoop.Model.Premi;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MyPremi extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Premi> premiList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_premi);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(getString(R.string.veurePremis));
        actionBar.setDisplayHomeAsUpEnabled(true);
        premiList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Usuari").document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) document.get("premis");
                        for (Map<String, Object> hm : list) {
                            String nom = (String) hm.get("nom");
                            String descripció = (String) hm.get("descripció");
                            String imatge = (String) hm.get("imatge");
                            Integer preu = ((Long) hm.get("preu")).intValue();
                            Timestamp time = (Timestamp) hm.get("time");
                            premiList.add(new Premi(nom,descripció,imatge, preu, time));
                        }
                    }

                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
                PremiAdapter adapter = new PremiAdapter(MyPremi.this, R.layout.my_list_item, premiList, true);
                listView.setAdapter(adapter);
            }

        });
    }
}
