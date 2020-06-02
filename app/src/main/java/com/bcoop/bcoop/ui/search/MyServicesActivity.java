package com.bcoop.bcoop.ui.search;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.Model.Servei;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.chat.ChatWithAnotherUserActivity;
import com.bcoop.bcoop.ui.notification.ConectFirebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class MyServicesActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Servei> meusServeis = new ArrayList<>();
    private FirebaseAuth mAuth;
    Usuari currentUser, proveidorUser = new Usuari();
    boolean b1 = false;
    boolean b2 = false;
    private Button acceptar;
    private EditText nivell;

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

    private void pujarExperiencia() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final DocumentReference documentReference = db.collection("Usuari").document(email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Usuari usuari = new Usuari();
                    usuari = documentSnapshot.toObject(Usuari.class);
                    int experiencia = usuari.getExperiencia();
                    int nivell = usuari.getNivell();

                    if (nivell == 1) {
                        nivell = 2;
                        //notificacio
                        LvlUp(nivell);
                    }
                    else if (nivell == 2 && experiencia >= 300){
                        nivell = 3;
                        //notificacio
                        LvlUp(nivell);
                    }
                    else if (nivell == 3 && experiencia >= 700){
                        nivell = 4;
                        //notificacio
                        LvlUp(nivell);
                    }
                    else if (nivell == 4 && experiencia >= 1200){
                        nivell = 5;
                        //notificacio
                        LvlUp(nivell);
                    }
                    else if (nivell == 5 && experiencia >= 1600){
                        nivell = 6;
                        //notificacio
                        LvlUp(nivell);
                    }
                    else if (nivell == 6 && experiencia >= 2500){
                        nivell = 7;
                        //notificacio
                        LvlUp(nivell);
                    }
                    else if (nivell == 7 && experiencia >= 3700){
                        nivell = 8;
                        //notificacio
                        LvlUp(nivell);
                    }
                    else if (nivell == 8 && experiencia >= 5000){
                        nivell = 9;
                        //notificacio
                        LvlUp(nivell);
                    }
                    else if (nivell == 9 && experiencia >= 7000) {
                        nivell = 10;
                        //notificacio
                        LvlUp(nivell);
                    }

                    db.collection("Usuari").document(email).update("experiencia", experiencia + 100);
                    db.collection("Usuari").document(email).update("nivell", nivell);
                }
            }
        });
    }


    private void LvlUp(int level) {
        final Dialog dialogView = new Dialog(this);
        dialogView.setContentView(R.layout.popup_levelup);
        dialogView.setCancelable(true);

        TextView coins = dialogView.findViewById(R.id.levelNumber);
        coins.setText(Integer.toString(level));

        Button dialogBttn = (Button) dialogView.findViewById(R.id.accept);

        dialogView.show();

        dialogBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }

        });
    }
}

