package com.bcoop.bcoop.ui.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcoop.bcoop.Model.Missatge;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.Model.Xat;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

class ChatAdapter extends BaseAdapter {

    private List<Missatge> missatges;
    private Context context;
    private String currentUser;
    private String otherUser;

    private FirebaseStorage storage;
    private FirebaseFirestore firestore;

    private Boolean firstMe;

    public ChatAdapter() {}

    public ChatAdapter(Context context, Xat xat) {
        this.context = context;
        this.missatges = xat.getMissatges();
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (xat.getUsuari1().equals(currentUser))
            otherUser = xat.getUsuari2();
        else {
            currentUser = xat.getUsuari2();
            otherUser = xat.getUsuari1();
        }
    }

    @Override
    public int getCount() {
        return missatges.size();
    }

    @Override
    public Object getItem(int position) {
        return missatges.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Missatge missatge = (Missatge) getItem(position);
        if (missatge.getRemitent().equals(currentUser)) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_message_mine, null);
            TextView message = convertView.findViewById(R.id.message_body);
            message.setText(missatge.getText());
        }
        else {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_message_other, null);
            TextView message = convertView.findViewById(R.id.message_body);
            message.setText(missatge.getText());
        }

        return convertView;
    }

    public void addedMessages(List<Missatge> missatgeList) {
        missatges = missatgeList;
        notifyDataSetChanged();
    }
}