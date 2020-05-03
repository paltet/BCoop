package com.bcoop.bcoop.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class ChatFragment extends Fragment {
    private ListView myChatList;

    public ChatFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        //final TextView textView = root.findViewById(R.id.text_chat);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Usuari").document(email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuari currentUsuari = documentSnapshot.toObject(Usuari.class);
                MyChatsAdapter myChatsAdapter = new MyChatsAdapter(getContext(), currentUsuari.getXats());
                myChatList.setAdapter(myChatsAdapter);
            }
        });
        return root;
    }
}
