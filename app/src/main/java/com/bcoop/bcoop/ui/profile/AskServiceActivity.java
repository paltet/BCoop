package com.bcoop.bcoop.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bcoop.bcoop.Model.Comentari;
import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.Model.Servei;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.chat.ChatFragment;
import com.bcoop.bcoop.ui.chat.ChatWithAnotherUserActivity;
import com.bcoop.bcoop.ui.notification.ConectFirebase;
import com.bcoop.bcoop.ui.search.MyServicesActivity;
import com.bcoop.bcoop.ui.search.SearchFragment;
import com.bcoop.bcoop.ui.search.UtilityClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;
import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class AskServiceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private Usuari currentUser;
    private Usuari proveidorUser;
    private List<String> habilitesProveidor;
    private Button dateButton;
    private Button ask_service_button;
    private String emailProveidor;//email del proveidorUser
    private EditText coins_a_pagar;
    private Date date;
    private Date currentDate;
    private EditText message;
    private String idServei;
    private String habilitat_seleccionada; //habilitat seleccionda per l'spinner
    private Spinner serviceSelectorSpinner;
    private int coins_to_pay;
    private String demanderName;
    private String demander;
    ArrayList<Servei> meusServeis = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_service);
        mAuth = FirebaseAuth.getInstance();
        //setDatesIni();//inicialitzo dates, les dos serán les actuals, si no es selecciona cap no complira el requisit de add service
        emailProveidor = getIntent().getStringExtra("otherUserEmail"); //email proveidor
        proveidorUser = new Usuari();
        currentUser = new Usuari();
        setCurrentUser();
        setProveidorUser();
        setDatesIni();
        coins_a_pagar = findViewById(R.id.editText);
        message = findViewById(R.id.editText3);
        dateButton = findViewById(R.id.selectDate);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });
        ask_service_button = findViewById(R.id.ask);
        ask_service_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coins_to_pay = Integer.parseInt(coins_a_pagar.getText().toString());
                Integer coins_currentUser = currentUser.getMonedes();
                boolean dataValida = true;
                //si la date >= currentDate, dataValida=true
                if(currentDate.compareTo(date) > 0)dataValida=false;
                if(coins_to_pay<=coins_currentUser & dataValida){//pot pagar el servei que demana i la data es posterior a la actual
                    String missatge = message.getText().toString();
                    demander = currentUser.getEmail();
                    demanderName = currentUser.getNom();
                    String estat = "pendent";
                    //nou document Servei
                    DocumentReference ref = firestore.collection("Servei").document();
                    idServei = ref.getId();
                    //els altres valors ja han estat agafats abans de clicar el botó add_service
                    Servei servei = new Servei(idServei, emailProveidor, demander, habilitat_seleccionada, date, coins_to_pay, missatge, estat, null, 10);
                    firestore.collection("Servei").document(idServei).set(servei);

                    //prova de afegir el servei creat a serveis del user proveidor
                    List<String> serveisProveidorUpdate = proveidorUser.getServeis();
                    serveisProveidorUpdate.add(idServei);
                    firestore.collection("Usuari").document(emailProveidor).update("serveis", serveisProveidorUpdate);


                    //actualitzem estats del servei...
                    //actualitzacionsConfirmacio();

                    //crear notificacio per al proveidor (s'esta fent una notificacio de tipus request servei)
                    firestore.collection("Servei").document(idServei).update("estat", "pendent");
                    ConectFirebase conectFirebase = new ConectFirebase();
                    Timestamp ts = new Timestamp(date);
                    Notification notification = new Notification(demander, demanderName, habilitat_seleccionada, idServei, coins_to_pay, 0, ts, ts, missatge);

                    conectFirebase.pushNotification(notification, emailProveidor);

                    //actualitzo la singleton class per poder refreshar rapid els meus serveis sense clicar el search
                    meusServeis = UtilityClass.getInstance().getList();
                    meusServeis.add(servei);
                    UtilityClass.getInstance().setList(meusServeis);
                    Collections.sort(meusServeis);

                    Toast.makeText(getApplicationContext(),  getApplicationContext().getString(R.string.solicitut_enviada), Toast.LENGTH_LONG).show();


                    // PER FER LA NOTIFICACIO PUSH => sendNotification(proveidor, demander, text);

                    //prova per veure els serveis que tinc FUNCIONA
                    firestore.collection("Servei")
                            .whereEqualTo("demander", demander)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document : task.getResult()){
                                            if(!document.get("estat").equals("finalitzat")) {//PER VEURE TOTS MENYS ELS FINALITZATS A LA LLISTA
                                                System.out.println(document.getId() + " => " + document.getData());
                                            }
                                        }
                                    }else {
                                        System.out.println("ERRRRRRORRRRRRRRRRRRRRRRRRRRRRRRRR");
                                    }
                                }
                            });
                }
                else{
                    if(coins_to_pay>coins_currentUser)Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.NotEnoughCoins), Toast.LENGTH_SHORT).show();
                        //si no tenim prous monedes pel servei o la data no es superior a currentDate
                    else Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.InvalidServiceDate), Toast.LENGTH_SHORT).show();
                }

                /*fem que hagi de tirar cap enrera per tonrar al xat
                Intent intent = new Intent();
                intent.putExtra("otherUserEmail", emailProveidor);
                intent.setClass(getApplicationContext(), ChatWithAnotherUserActivity.class);
                startActivity(intent);*/

                //per tornar directament al xat i tancar la activty de askService
                finish();

            }


        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //posa la data actual al calendari
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //actualitzem date amb el nou pick
        date = c.getTime();
        System.out.println("Date in in the format CHANGEEEEEEEED: dd-MM-yyyy");
        System.out.println(new SimpleDateFormat("dd-MM-yyyy").format(date));
        if(date.compareTo(currentDate) > 0){
            System.out.println("date ES MÉS GRAN QUE currentDate");
        }
    }

    //agafar el current user
    private void setCurrentUser(){
        firestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firestore.collection("Usuari").document(mAuth.getCurrentUser().getEmail());
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

    }

    private void setProveidorUser() {
        firestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firestore.collection("Usuari").document(emailProveidor);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    proveidorUser = new Usuari();
                    proveidorUser = documentSnapshot.toObject(Usuari.class);
                    //fem el get de les habilitats del proveidor per passarli al spinner
                    List<String> habilitatsUsuari = new ArrayList<>();
                    Map<String, HabilitatDetall> detallHabilitatUsuari = proveidorUser.getHabilitats();
                    for (Map.Entry<String, HabilitatDetall> entry : detallHabilitatUsuari.entrySet())
                        habilitatsUsuari.add(entry.getKey());
                    habilitesProveidor= habilitatsUsuari;
                    setSpinnerContent();
                }
            }
        });
    }


    //inicialitzar dates
    private void setDatesIni(){
        Calendar calendar = Calendar.getInstance();
        currentDate = calendar.getTime();
        date = currentDate;
        Log.d("DATA INICIAL:", String.valueOf(currentDate));
        System.out.println("Date in in the format: dd-MM-yyyy");
        System.out.println(new SimpleDateFormat("dd-MM-yyyy").format(date));
    }


    private void setSpinnerContent() {

        serviceSelectorSpinner = findViewById(R.id.service_spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this , android.R.layout.simple_list_item_1, habilitesProveidor);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSelectorSpinner.setAdapter(myAdapter);

        AdapterView.OnItemSelectedListener mListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                habilitat_seleccionada = parent.getSelectedItem().toString();//habilitat que es la que s'ha de guardar al guardar el Servei
                Toast.makeText(parent.getContext(), "Seleccionada:"+parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "none found = user_store_id.", Toast.LENGTH_SHORT).show(); //still nothing
            }
        };
        serviceSelectorSpinner.setOnItemSelectedListener(mListener); // Register this spinner for a mListener
    }



//FINS AQUI DEMANAR SERVEI

    //CONFIRMAR SERVEI (INTERCANVI DE MONEDES + VALORAR(estrelles+comentari) de la habilitat del proveidor, i posar estat a "finalitzat" del servei)
    private void actualitzacionsConfirmacio(){

        //posar el servei amb idServei a "finalitzat" i donar-li una valoració i comentari(agafarlos de front)
        Comentari comentariValoracio = new Comentari("aquest es el comentari de valoració1", currentUser.getNom());
        int estrellesValoracio = 3;

        //actualitzo servei
        firestore.collection("Servei").document(idServei).update("estat", "en curs");
        firestore.collection("Servei").document(idServei).update("comentariValoracio", comentariValoracio);
        firestore.collection("Servei").document(idServei).update("estrellesValoracio", estrellesValoracio);
        //actualitzo DetallsHabilitat de la habilitat del proveidor
        //fem el get de numero valoracions, sumatotalValoracions.
        //actualitzem els valors i fem la valoracio de la Habilitat amb la mean
        Integer numeroValoracions;
        Integer sumaTotalValoracions;
        Map<String, HabilitatDetall> habilitatsDetallProveidor = proveidorUser.getHabilitats();
        for (Map.Entry<String, HabilitatDetall> entry : habilitatsDetallProveidor.entrySet()){
            if(entry.getKey().equals(habilitat_seleccionada)) {
                HabilitatDetall habilitatDetall = entry.getValue();
                //gets i sets
                numeroValoracions = habilitatDetall.getNumeroValoracions() + 1;
                sumaTotalValoracions = habilitatDetall.getSumaTotalValoracions() + estrellesValoracio;
                habilitatDetall.setNumeroValoracions(numeroValoracions);
                habilitatDetall.setSumaTotalValoracions(sumaTotalValoracions);
                int mean = sumaTotalValoracions / numeroValoracions;
                habilitatDetall.setValoracio(mean);
                //tot llest per les valoracions => posar-ho al firestore
                entry.setValue(habilitatDetall);
                proveidorUser.setHabilitats(habilitatsDetallProveidor);
                firestore.collection("Usuari").document(emailProveidor).update("habilitats", habilitatsDetallProveidor);
                //agafar els comentaris, afegir el nou comentari a la llista i fer update
                List<Comentari> comentarisHabilitatServei = habilitatDetall.getComentaris();
                comentarisHabilitatServei.add(comentariValoracio);
                firestore.collection("Usuari").document(emailProveidor).update("habilitats", habilitatsDetallProveidor);
            }

        }

        /*
        //actualitzem monedes de proveidor i demander
        int monedesDemanderUpdate = currentUser.getMonedes() - coins_to_pay;//el que confirma el servei es el demander
        int monedesProveidorUpdate = proveidorUser.getMonedes() + coins_to_pay;
        firestore.collection("Usuari").document(emailProveidor).update("monedes", monedesProveidorUpdate);
        firestore.collection("Usuari").document(currentUser.getEmail()).update("monedes", monedesDemanderUpdate);
        */

        //enviar notificacio de confirmacio i de transfer de monedes
        ConectFirebase conectFirebase = new ConectFirebase();
        Timestamp ts = new Timestamp(date);
        //notificacio service valoration
        String comentariValoracioNoti = "comentari valoracio servei";//agafarlo de front
        Notification notification = new Notification(demander, demanderName, habilitat_seleccionada, ts, estrellesValoracio,
                comentariValoracioNoti);

        conectFirebase.pushNotification(notification, emailProveidor);

        /*
        //notificacio trading information (transfer de monedes)
        Notification notification1 = new Notification(coins_to_pay, "proveidor");
        Notification notification2 = new Notification(coins_to_pay, "demander");
        System.out.println("AQUESTESSS MONEDESSSSSSSSSSSSSSSSSSSSSS"+coins_to_pay);
        conectFirebase.pushNotification(notification1, emailProveidor);
        conectFirebase.pushNotification(notification2, demander);*/


    }


}





