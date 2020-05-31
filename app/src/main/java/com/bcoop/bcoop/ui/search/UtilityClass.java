package com.bcoop.bcoop.ui.search;

import androidx.annotation.NonNull;

import com.bcoop.bcoop.Model.Servei;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UtilityClass {

    private static UtilityClass instance;

    private ArrayList<Servei> list;

    public ArrayList<Servei> getList() {
        return list;
    }

    public void setList(ArrayList<Servei> list) {
        this.list = list;
    }

    public ArrayList<Servei> getACTlist(){
        ArrayList<Servei> meusServeis = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //consulta a firestore
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
                                meusServeis.add(meuServeiAfegir);
                            }
                        }
                    }
                });

        UtilityClass.getInstance().setList(meusServeis);
        return meusServeis;
    }

    private UtilityClass(){}

    public static UtilityClass getInstance(){
        if(instance == null){
            instance = new UtilityClass();
        }
        return instance;
    }
}
