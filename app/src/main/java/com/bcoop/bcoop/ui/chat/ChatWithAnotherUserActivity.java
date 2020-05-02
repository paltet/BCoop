package com.bcoop.bcoop.ui.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.Model.Missatge;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.Model.Xat;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class ChatWithAnotherUserActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private String chatWith;
    private Usuari currentUsuari;
    private Usuari usuariChatWith;
    private String id;
    private Xat xat;
    private ChatAdapter chatAdapter;
    private ListView messagesList;
    private ImageView userImage;
    private TextView username;
    private EditText message;
    private ImageView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        chatWith = getIntent().getStringExtra("otherUserEmail");

        if (currentUser.getEmail().compareTo(chatWith) > 0)
            id = chatWith.concat(",").concat(currentUser.getEmail());
        else id = currentUser.getEmail().concat(",").concat(chatWith);

        messagesList = findViewById(R.id.messagesList);
        userImage = findViewById(R.id.userImage);
        userImage.setImageResource(R.drawable.profile);
        username = findViewById(R.id.usernameText);
        message = findViewById(R.id.messageForm);
        send = findViewById(R.id.sendMessage);

        final DocumentReference documentReference = firestore.collection("Usuari").document(currentUser.getEmail());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUsuari = documentSnapshot.toObject(Usuari.class);
                if (currentUsuari.getXats().contains(id))
                    openChat();
                else createNewChat();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void createNewChat() {
        Xat xat;
        if (currentUser.getEmail().compareTo(chatWith) > 0)
            xat = new Xat(chatWith, currentUser.getEmail());
        else xat = new Xat(currentUser.getEmail(), chatWith);
        final DocumentReference documentReferenceXat = firestore.collection("Xat").document(id);
        documentReferenceXat.set(xat);

        final DocumentReference documentReferenceMyUsuari = firestore.collection("Usuari").document(currentUser.getEmail());
        documentReferenceMyUsuari.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    currentUsuari = documentSnapshot.toObject(Usuari.class);
                    currentUsuari.addXats(id);
                    documentReferenceMyUsuari.update("xats", currentUsuari.getXats()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final DocumentReference documentReferenceUsuari = firestore.collection("Usuari").document(chatWith);
                            documentReferenceUsuari.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    usuariChatWith = documentSnapshot.toObject(Usuari.class);
                                    usuariChatWith.addXats(id);
                                    documentReferenceUsuari.update("xats", usuariChatWith.getXats()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Open Xat
                                            openChat();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void openChat() {
        final DocumentReference documentReferenceUsuari = firestore.collection("Usuari").document(chatWith);
        documentReferenceUsuari.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fotoUrl = (String) documentSnapshot.get("foto");
                getImageFromStorage(fotoUrl);
                String usrn = (String) documentSnapshot.get("nom");
                username.setText(usrn);
            }
        });
        final DocumentReference documentReference = firestore.collection("Xat").document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    xat = documentSnapshot.toObject(Xat.class);
                    chatAdapter = new ChatAdapter(ChatWithAnotherUserActivity.this, xat);
                    messagesList.setAdapter(chatAdapter);
                    messagesList.setSelection(messagesList.getCount() -1);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (documentSnapshot.exists()) {
                                xat = documentSnapshot.toObject(Xat.class);
                                chatAdapter.addedMessages(xat.getMissatges());
                                messagesList.setSelection(messagesList.getCount() -1);
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendMessage() {
        String text = message.getText().toString();
        if (text.length() > 0) {
            Missatge missatge = new Missatge(currentUser.getEmail(), chatWith, text, null, Calendar.getInstance().getTime());
            xat.addMissatge(missatge);
            final DocumentReference documentReference = firestore.collection("Xat").document(id);
            documentReference.update("missatges", xat.getMissatges());
            message.setText(null);
        }
    }

    private void getImageFromStorage(String uriImage) {
        if (uriImage != null) {
            StorageReference storageReference = storage.getReferenceFromUrl(uriImage);
            try {
                final File file = File.createTempFile("image", uriImage.substring(uriImage.lastIndexOf('.')));
                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        userImage.setImageBitmap(bitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}