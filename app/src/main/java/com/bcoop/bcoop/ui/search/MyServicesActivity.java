package com.bcoop.bcoop.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.Model.Servei;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.chat.ChatWithAnotherUserActivity;
import com.bcoop.bcoop.ui.notification.ConectFirebase;
import com.bcoop.bcoop.ui.notification.NotificationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MyServicesActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Servei> meusServeis = new ArrayList<>();
    private FirebaseAuth mAuth;
    Usuari currentUser, proveidorUser = new Usuari();
    boolean b1 = false;
    boolean b2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);
        //utilitzo una classe singleton per agafar els meus serveis
        meusServeis = UtilityClass.getInstance().getList();
        Collections.sort(meusServeis);
        MeusServeiAdapter adapter = new MeusServeiAdapter(this, R.layout.service_item, meusServeis);
        ListView listView = findViewById(R.id.listView42);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Servei serveiClick = meusServeis.get(position);
                if(serveiClick.getEstat().equals("finalitzat")){
                    finalitzatDialog(serveiClick);
                }

                if(serveiClick.getEstat().equals("pendent")){
                    pendentDialog(serveiClick);
                }

                if(serveiClick.getEstat().equals("en curs")){
                    db = FirebaseFirestore.getInstance();
                    final DocumentReference documentReference = db.collection("Usuari").document(mAuth.getCurrentUser().getEmail());
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                currentUser = documentSnapshot.toObject(Usuari.class);
                                b1 = true;
                            }
                        }
                    });

                    String emailProveidor = serveiClick.getProveidor();
                    db = FirebaseFirestore.getInstance();
                    final DocumentReference documentReference1 = db.collection("Usuari").document(emailProveidor);
                    documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                proveidorUser = documentSnapshot.toObject(Usuari.class);
                                b2 = true;
                            }
                        }
                    });
                    enCursDialog(serveiClick);

                }
            }
        });


    }

    private void finalitzatDialog(final Servei servei){
        final Dialog dialogView = new Dialog(this);
        dialogView.setContentView(R.layout.dialog_valor_myservices);
        dialogView.setCancelable(true);

        TextView habilitatName = dialogView.findViewById(R.id.habilitatName);
        TextView dateFi1 = dialogView.findViewById(R.id.dateFi1);
        TextView valor1 = dialogView.findViewById(R.id.valor1);
        TextView comment1 = dialogView.findViewById(R.id.comment1);


        habilitatName.setText(servei.getHabilitat());
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(servei.getDate());
        dateFi1.setText(date);
        valor1.setText(Integer.toString(servei.getEstrellesValoracio()));
        comment1.setText(servei.getComentariValoracio().getContingut());

        dialogView.show();

    }

    private void pendentDialog(Servei servei){
        final Dialog dialogView = new Dialog(this);
        dialogView.setContentView(R.layout.layout_dialog_pendent);
        dialogView.setCancelable(true);

        Button dialogBtnChat = (Button) dialogView.findViewById(R.id.chat);

        dialogBtnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("otherUserEmail", servei.getProveidor());
                intent.setClass(getApplicationContext(), ChatWithAnotherUserActivity.class);
                startActivity(intent);
            }
        });
        dialogView.show();
    }


    private void enCursDialog(Servei servei){
        //pagar el servei
        final Dialog dialogView = new Dialog(this);
        dialogView.setContentView(R.layout.confirm_service);
        dialogView.setCancelable(true);

        TextView coins = dialogView.findViewById(R.id.confirm_service_coins);
        coins.setText(Integer.toString(servei.getCoins_to_pay()));

        Button dialogBttnPay = (Button) dialogView.findViewById(R.id.confirm_service_pay);

        dialogView.show();

        dialogBttnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b1 & b2) {
                    Integer coins_to_pay = servei.getCoins_to_pay();
                    String emailProveidor = servei.getProveidor();
                    String demander = currentUser.getEmail();
                    //actualitzem monedes de proveidor i demander
                    System.out.println("HE APRETAT EL BOTO PAGAR");
                    Integer monedesDemanderUpdate = currentUser.getMonedes() - coins_to_pay;//el que confirma el servei es el demander
                    Integer monedesProveidorUpdate = proveidorUser.getMonedes() + coins_to_pay;
                    db.collection("Usuari").document(emailProveidor).update("monedes", monedesProveidorUpdate);
                    db.collection("Usuari").document(demander).update("monedes", monedesDemanderUpdate);

                    //enviar notificacio de transfer de monedes
                    ConectFirebase conectFirebase = new ConectFirebase();


                    //notificacio trading information (transfer de monedes)
                    Notification notification1 = new Notification(coins_to_pay, "proveidor");
                    Notification notification2 = new Notification(coins_to_pay, "demander");
                    conectFirebase.pushNotification(notification1, emailProveidor);
                    conectFirebase.pushNotification(notification2, demander);

                    //change l'estat a "finalitzat"
                    db.collection("Servei").document(servei.getIdServei()).update("estat", "finalitzat");

                    //tornem a actualitzar la listView

                    /*
                    meusServeis = UtilityClass.getInstance().getACTlist();
                    Collections.sort(meusServeis);
                    MeusServeiAdapter adapterACT = new MeusServeiAdapter(getApplicationContext(), R.layout.service_item, meusServeis);
                    ListView listView = findViewById(R.id.listView);
                    listView.setAdapter(adapterACT);*/

                    //tanco dialog
                    recreate();

                    /*
                    finish();
                    startActivity(getIntent());*/
                }
            }
        });

    }

    private void finalitzatValorar(Servei servei){
        //xml del PAU
    }

/*
    private void setCurrentUser(){
        db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("Usuari").document(mAuth.getCurrentUser().getEmail());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    currentUser = new Usuari();
                    currentUser = documentSnapshot.toObject(Usuari.class);
                    Integer coins_currentUser = currentUser.getMonedes();
                    Log.d("MONEDESSSSSSSSSSSSSSS:", String.valueOf(coins_currentUser));
                }
            }
        });

    }*/


/*
    private void setProveidorUser(Servei servei) {
        String emailProveidor = servei.getProveidor();
        db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("Usuari").document(emailProveidor);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    proveidorUser = documentSnapshot.toObject(Usuari.class);
                }
            }
        });
    }*/



/*
    private void searchMeusServeis() {
        //omplim la llista de serveis amb els serveis que en soc demander
        ArrayList<Servei> list = new ArrayList<>();
        ListView listView = findViewById(R.id.listView);
        String myEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Servei")
                .whereEqualTo("demander", myEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Servei meuServei = document.toObject(Servei.class);
                                Servei meuServeiAfegir = new Servei(meuServei.getIdServei(), meuServei.getProveidor(), meuServei.getDemander(), meuServei.getHabilitat(),
                                        meuServei.getDate(), meuServei.getCoins_to_pay(), meuServei.getMessage(), meuServei.getEstat(),
                                        meuServei.getComentariValoracio(), meuServei.getEstrellesValoracio());
                                list.add(meuServei);
                                meusServeis.add(meuServeiAfegir);
                            }
                        }
                    }
                });

    }*/

}

