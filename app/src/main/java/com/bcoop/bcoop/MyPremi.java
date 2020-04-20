package com.bcoop.bcoop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.ui.prize.Premi;
import com.bcoop.bcoop.ui.prize.PremiAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyPremi extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Premi> premiList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_premi);
        premiList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Usuari").document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    premiList = (List<Premi>) document.get("premis");
                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
                PremiAdapter adapter = new PremiAdapter(MyPremi.this, R.layout.my_list_item, premiList);
                listView.setAdapter(adapter);
            }

        });
    }
}
