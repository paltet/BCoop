package com.bcoop.bcoop.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private MyChatsAdapter myChatsAdapter;

    public ChatFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_chat, container, false);
        myChatList = root.findViewById(R.id.currentChatsList);

        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Usuari").document(email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuari currentUsuari = documentSnapshot.toObject(Usuari.class);
                myChatsAdapter = new MyChatsAdapter(getContext(), currentUsuari.getXats());
                myChatList.setAdapter(myChatsAdapter);
                myChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ChatFragment.super.getActivity(), ChatWithAnotherUserActivity.class);
                        String pk = (String) parent.getAdapter().getItem(position);
                        String[] usuaris = pk.split(",");
                        if (usuaris[0].equals(email))
                            intent.putExtra("otherUserEmail", usuaris[1]);
                        else intent.putExtra("otherUserEmail", usuaris[0]);
                        startActivityForResult(intent, 123);
                    }
                });
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123)
            myChatsAdapter.notifyDataSetChanged();
    }
}
