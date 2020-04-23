package com.bcoop.bcoop.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.profile.ProfileFragment;

import java.util.ArrayList;

public class configurationHabilitats extends AppCompatActivity {

    private Button buttonConfHab;

    CheckBox c1, c2, c3, c4, c5, c6, c7, c8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_habilitats);
        c1 = findViewById(R.id.idCheck1);
        c2 = findViewById(R.id.idCheck2);
        c3 = findViewById(R.id.idCheck3);
        c4 = findViewById(R.id.idCheck4);
        c5 = findViewById(R.id.idCheck5);
        c6 = findViewById(R.id.idCheck6);
        c7 = findViewById(R.id.idCheck7);
        c8 = findViewById(R.id.idCheck8);

       /* ArrayList<String> userHabilitats = getUserHabilitats();
        checkedByDefault(userHabilitats);*/


        buttonConfHab = findViewById(R.id.Accept);
        buttonConfHab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar();
            }
        });

    }

    private void checkedByDefault(ArrayList<String> userHabilitats) {
        for(int i = 0; i < userHabilitats.size(); ++i){
            if(userHabilitats.get(i) == "option1"){c1.setChecked(true);}
            if(userHabilitats.get(i) == "option2"){c2.setChecked(true);}
            if(userHabilitats.get(i) == "option3"){c3.setChecked(true);}
            if(userHabilitats.get(i) == "option4"){c4.setChecked(true);}
            if(userHabilitats.get(i) == "option5"){c5.setChecked(true);}
            if(userHabilitats.get(i) == "option6"){c6.setChecked(true);}
            if(userHabilitats.get(i) == "option7"){c7.setChecked(true);}
            if(userHabilitats.get(i) == "option8"){c8.setChecked(true);}
        }
    }

    //validacio del checkbox, veure les opcions escollides
    private void validar() {
        String message = "Habilitats seleccionades: \n";

        if(c1.isChecked()){
            message+="option1 \n";
            //cridar a afegir al arrayList de habilitats del user, si ja esta afegida no afegirla
        }
        if(c2.isChecked()){
            message+="option2 \n";
            //cridar a afegir al arrayList de habilitats del user, si ja esta afegida no afegirla
        }
        if(c3.isChecked()){
            message+="option3 \n";
            //cridar a afegir al arrayList de habilitats del user, si ja esta afegida no afegirla
        }
        if(c4.isChecked()){
            message+="option4 \n";
            //cridar a afegir al arrayList de habilitats del user, si ja esta afegida no afegirla
        }
        if(c5.isChecked()){
            message+="option5 \n";
            //cridar a afegir al arrayList de habilitats del user, si ja esta afegida no afegirla
        }
        if(c6.isChecked()){
            message+="option6 \n";
            //cridar a afegir al arrayList de habilitats del user, si ja esta afegida no afegirla
        }
        if(c7.isChecked()){
            message+="option7 \n";
            //cridar a afegir al arrayList de habilitats del user, si ja esta afegida no afegirla
        }
        if(c8.isChecked()){
            message+="option8 \n";
            //cridar a afegir al arrayList de habilitats del user, si ja esta afegida no afegirla
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}