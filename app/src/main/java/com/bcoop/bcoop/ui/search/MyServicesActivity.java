package com.bcoop.bcoop.ui.search;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bcoop.bcoop.Model.Comentari;
import com.bcoop.bcoop.Model.HabilitatDetall;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyServicesActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ArrayList<Servei> meusServeis = new ArrayList<>();
    private FirebaseAuth mAuth;
    Usuari currentUser, proveidorUser = new Usuari();
    MeusServeiAdapter adapter;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    Integer num_estrelles = 0;
    private String uriImage;
    private Bitmap bitmap;
    boolean star1Empty = true;
    private EditText nivell;
    private Button report;
    private EditText EditReport;
    private Button ReportSendButton;


    /*
    ProjectListAdapter projectListAdapter = new ProjectListAdapter();
    TextView empty=(TextView)findViewById(R.id.empty);
projectsListView.setEmptyView(empty);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);
        //utilitzo una classe singleton per agafar els meus serveis
        meusServeis = UtilityClass.getInstance().getList();
        Collections.sort(meusServeis);
        adapter = new MeusServeiAdapter(this, R.layout.service_item, meusServeis);
        ListView listView = findViewById(R.id.listView42);
        listView.setAdapter(adapter);
        TextView empty = findViewById(R.id.textEmpty);
        listView.setEmptyView(empty);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Servei serveiClick = meusServeis.get(position);

                //System.out.println("TAMANY ABANS DE CLICAR:"+meusServeis.size());
                //meusServeis.remove(serveiClick);
                //adapter.notifyDataSetChanged();
                //System.out.println("TAMANY DESPRES DE CLICAR:"+meusServeis.size());

                UtilityClass.getInstance().setList(meusServeis);

                String emailProveidor = serveiClick.getProveidor();
                db = FirebaseFirestore.getInstance();
                final DocumentReference documentReference1 = db.collection("Usuari").document(emailProveidor);
                documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            proveidorUser = documentSnapshot.toObject(Usuari.class);
                            uriImage = proveidorUser.getFoto();
                            System.out.println("AQUESTA ES LA URI" + uriImage);
                            if (uriImage != null) {
                                StorageReference storageReference = storage.getReferenceFromUrl(uriImage);
                                try {
                                    final File file = File.createTempFile("image", uriImage.substring(uriImage.lastIndexOf('.')));
                                    storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                            b3 = true;
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            b2 = true;
                        }
                    }
                });

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

                if (serveiClick.getEstat().equals("finalitzat")) {
                    if (serveiClick.estrellesValoracio == 10) {
                        finalitzatValorar(serveiClick);
                    } else {
                        finalitzatDialog(serveiClick);
                    }
                }

                if (serveiClick.getEstat().equals("pendent")) {
                    pendentDialog(serveiClick);
                }

                if (serveiClick.getEstat().equals("en curs")) {
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
        report = dialogView.findViewById(R.id.Report1);


        habilitatName.setText(servei.getHabilitat());
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(servei.getDate());
        dateFi1.setText(date);
        valor1.setText(Integer.toString(servei.getEstrellesValoracio()));
        comment1.setText(servei.getComentariValoracio().getContingut());

        dialogView.show();


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewReport = getLayoutInflater().inflate(R.layout.activity_report_service, null);
                final AlertDialog.Builder alertReport = new AlertDialog.Builder(MyServicesActivity.this);
                alertReport.setView(viewReport);
                final AlertDialog alertDialogReport = alertReport.create();
                alertDialogReport.setCanceledOnTouchOutside(true);

                alertDialogReport.show();
                EditReport = viewReport.findViewById(R.id.incidenciaForm);
                ReportSendButton = viewReport.findViewById(R.id.sendButton);
                ReportSendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        String item = EditReport.getText().toString();
                        Map<String, Object> data = new HashMap<>();
                        data.put("Informe", item);
                        data.put("user", email);
                        Date date = Calendar.getInstance().getTime();
                        data.put("data", date.toString());
                        db.collection("Reports").add(data);
                        alertDialogReport.dismiss();
                        Toast.makeText(MyServicesActivity.this, R.string.ReportEnviat, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

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
                    servei.setEstat("finalitzat");
                    dialogView.dismiss();

                    //borrar el servei i afegir el nou
                    Servei serveiModified = new Servei();
                    serveiModified = servei;
                    meusServeis.remove(servei);
                    meusServeis.add(serveiModified);
                    Collections.sort(meusServeis);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void finalitzatValorar(Servei servei){
        //xml del PAU
        final Dialog dialogView = new Dialog(this);
        dialogView.setContentView(R.layout.activity_value_service);
        dialogView.setCancelable(true);

        //falta la imatge
        ImageView profileImage = dialogView.findViewById(R.id.otherUserImage);
        TextView username = dialogView.findViewById(R.id.usernameText);
        TextView habilitat = dialogView.findViewById(R.id.habilityText);
        TextView dateServei = dialogView.findViewById(R.id.DateText);
        EditText comentariText = dialogView.findViewById(R.id.comment1);
        //imatges de les estrelles
        ImageView star1 = dialogView.findViewById(R.id.star1);
        ImageView star2 = dialogView.findViewById(R.id.star2);
        ImageView star3 = dialogView.findViewById(R.id.star3);
        ImageView star4 = dialogView.findViewById(R.id.star4);
        ImageView star5 = dialogView.findViewById(R.id.star5);


        Button valorarButton = dialogView.findViewById(R.id.sendButton);

        username.setText(proveidorUser.getNom());
        habilitat.setText(servei.getHabilitat());
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date1 = simpleDateFormat.format(servei.getDate());
        dateServei.setText(date1);

        if(b1&b2&b3) { profileImage.setImageBitmap(bitmap);}


        dialogView.show();

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (star1Empty) {
                    star1.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                    star2.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                    star3.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                    star4.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                    star5.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                    num_estrelles = 1;
                    star1Empty = false;
                    System.out.println("NUMERO DE ESTRELLES: " + num_estrelles);
                } else {
                    star1.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                    star2.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                    star3.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                    star4.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                    star5.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                    num_estrelles = 0;
                    star1Empty = true;
                    System.out.println("NUMERO DE ESTRELLES: " + num_estrelles);
                }
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star2.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star3.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                star4.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                star5.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                num_estrelles = 2;
                star1Empty = true;
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star2.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star3.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star4.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                star5.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                num_estrelles = 3;
                star1Empty = true;
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star2.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star3.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star4.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star5.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                num_estrelles = 4;
                star1Empty = true;
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star2.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star3.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star4.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                star5.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
                num_estrelles = 5;
                star1Empty = true;
            }
        });


        //guardar valoració a la db
        valorarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comentari2 = comentariText.getText().toString();
                if (!comentari2.equals("")) {
                    Comentari comentariValoracio1 = new Comentari(comentari2, currentUser.getNom());
                    //afegeixo la valoració al servei
                    db.collection("Servei").document(servei.getIdServei()).update("estrellesValoracio", num_estrelles);
                    servei.setEstrellesValoracio(num_estrelles);
                    //posem comentari al servei
                    db.collection("Servei").document(servei.getIdServei()).update("comentariValoracio", comentariValoracio1);
                    servei.setComentariValoracio(comentariValoracio1);
                    //actualitzo detalls de la Habilitat del proveidor (per si hi ha comentari)
                    //actualitzo DetallsHabilitat de la habilitat del proveidor
                    //fem el get de numero valoracions, sumatotalValoracions.
                    Integer numeroValoracions;
                    Integer sumaTotalValoracions;
                    Map<String, HabilitatDetall> habilitatsDetallProveidor = proveidorUser.getHabilitats();
                    for (Map.Entry<String, HabilitatDetall> entry : habilitatsDetallProveidor.entrySet()){
                        if(entry.getKey().equals(servei.getHabilitat())) {

                            HabilitatDetall habilitatDetall = entry.getValue();
                            //gets i sets
                            numeroValoracions = habilitatDetall.getNumeroValoracions() + 1;
                            sumaTotalValoracions = habilitatDetall.getSumaTotalValoracions() + num_estrelles;
                            habilitatDetall.setNumeroValoracions(numeroValoracions);
                            habilitatDetall.setSumaTotalValoracions(sumaTotalValoracions);
                            int mean = sumaTotalValoracions / numeroValoracions;
                            habilitatDetall.setValoracio(mean);
                            //tot llest per les valoracions => posar-ho al firestore
                            entry.setValue(habilitatDetall);
                            proveidorUser.setHabilitats(habilitatsDetallProveidor);
                            db.collection("Usuari").document(proveidorUser.getEmail()).update("habilitats", habilitatsDetallProveidor);
                            //agafar els comentaris, afegir el nou comentari a la llista i fer update
                            List<Comentari> comentarisHabilitatServei = habilitatDetall.getComentaris();
                            Comentari comentariValoracio2 = new Comentari(comentari2, currentUser.getNom());
                            comentarisHabilitatServei.add(comentariValoracio2);
                            db.collection("Usuari").document(proveidorUser.getEmail()).update("habilitats", habilitatsDetallProveidor);
                        }

                    }

                }
                else {
                    //afeigir la valoracio al servei
                    db.collection("Servei").document(servei.getIdServei()).update("estrellesValoracio", num_estrelles);
                    //actualitzo només la valoracio mean
                    servei.setEstrellesValoracio(num_estrelles);
                    Integer numeroValoracions;
                    Integer sumaTotalValoracions;
                    Map<String, HabilitatDetall> habilitatsDetallProveidor = proveidorUser.getHabilitats();
                    for (Map.Entry<String, HabilitatDetall> entry : habilitatsDetallProveidor.entrySet()) {
                        if (entry.getKey().equals(servei.getHabilitat())) {
                            HabilitatDetall habilitatDetall = entry.getValue();
                            //gets i sets
                            numeroValoracions = habilitatDetall.getNumeroValoracions() + 1;
                            sumaTotalValoracions = habilitatDetall.getSumaTotalValoracions() + num_estrelles;
                            habilitatDetall.setNumeroValoracions(numeroValoracions);
                            habilitatDetall.setSumaTotalValoracions(sumaTotalValoracions);
                            int mean = sumaTotalValoracions / numeroValoracions;
                            habilitatDetall.setValoracio(mean);
                            //tot llest per les valoracions => posar-ho al firestore
                            entry.setValue(habilitatDetall);
                            proveidorUser.setHabilitats(habilitatsDetallProveidor);
                            db.collection("Usuari").document(proveidorUser.getEmail()).update("habilitats", habilitatsDetallProveidor);
                        }
                    }
                }
                Toast.makeText(getApplicationContext(),  getApplicationContext().getString(R.string.valoration_enviada), Toast.LENGTH_LONG).show();
                dialogView.dismiss();
                //borrar el servei i afegir el nou
                Servei serveiModified = new Servei();
                serveiModified = servei;
                meusServeis.remove(servei);
                meusServeis.add(serveiModified);
                Collections.sort(meusServeis);
                adapter.notifyDataSetChanged();
                pujarExperiencia();
            }
        });
    }





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

