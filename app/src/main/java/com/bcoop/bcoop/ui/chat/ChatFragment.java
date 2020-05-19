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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                Map<String, List<String>> xats = currentUsuari.getXats();
                List<String> chatsWith = new ArrayList<>();
                List<String> lastChat = new ArrayList<>();

                for (Map.Entry<String, List<String>> entry : xats.entrySet()) {
                    chatsWith.add(entry.getKey());
                    lastChat.add(entry.getValue().get(entry.getValue().size()-1));
                }

                myChatsAdapter = new MyChatsAdapter(getContext(), chatsWith, lastChat);
                myChatList.setAdapter(myChatsAdapter);
                myChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ChatFragment.super.getActivity(), ChatWithAnotherUserActivity.class);
                        String pk = (String) parent.getAdapter().getItem(position);
                        intent.putExtra("otherUserEmail", pk);
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
