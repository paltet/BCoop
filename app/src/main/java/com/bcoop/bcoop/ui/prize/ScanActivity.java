package com.bcoop.bcoop.ui.prize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.bcoop.bcoop.MainActivity;
import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.databinding.ConfirmServiceBinding;
import com.bcoop.bcoop.ui.notification.ConectFirebase;
import com.bcoop.bcoop.ui.notification.NotificationAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static android.content.ContentValues.TAG;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;

public class ScanActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(getString(R.string.scan));
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.Scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(ScanActivity.this);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
            }
        });

        findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.editText);
                String code = editText.getText().toString();
                updateData(code);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            } else {
                updateData(result.getContents());
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void updateData(String code) {
        DocumentReference docRef = db.collection("Premi Venut").document(code);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.getBoolean("isUsed"))
                            Toast.makeText(getApplicationContext(), R.string.prize_used, Toast.LENGTH_LONG).show();
                        else {
                            db.collection("Premi Venut").document(code).update("isUsed", true);
                            Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_LONG).show();
                            updatePremi(document.get("user").toString(), code);

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), R.string.prize_no_exist, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void updatePremi(String user, String code) {
        db.collection("Usuari").document(user).collection("premis").orderBy("time", DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.get("id").toString().equals(code)) {
                            db.collection("Usuari").document(user).collection("premis")
                                    .document(document.getId()).update("use", true);
                        }
                    }
                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }

            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
