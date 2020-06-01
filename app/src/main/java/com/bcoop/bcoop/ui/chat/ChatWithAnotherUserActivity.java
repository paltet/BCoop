package com.bcoop.bcoop.ui.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.bcoop.bcoop.ui.chat.chatnotification.APIService;
import com.bcoop.bcoop.ui.chat.chatnotification.Client;
import com.bcoop.bcoop.ui.chat.chatnotification.Data;
import com.bcoop.bcoop.ui.chat.chatnotification.MyResponse;
import com.bcoop.bcoop.ui.chat.chatnotification.Sender;
import com.bcoop.bcoop.ui.profile.AskServiceActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatWithAnotherUserActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private String chatWith;
    private Usuari currentUsuari;
    private Usuari usuariChatWith;
    private String xatID;
    private Xat xat;
    private ChatAdapter chatAdapter;
    private ListView messagesList;
    private ImageView userImage;
    private TextView username;
    private EditText message;
    private ImageView send;
    private ImageView adjunt;
    private APIService apiService;
    private boolean first = false;
    private boolean otherFirst = false;
    private boolean firstReadOfLastChat = false;
    private List<Missatge> missatges;

    private static final int PERMISSION_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private Uri imgUri;
    private StorageReference imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        chatWith = getIntent().getStringExtra("otherUserEmail");

        messagesList = findViewById(R.id.messagesList);
        userImage = findViewById(R.id.userImage);
        userImage.setImageResource(R.drawable.profile);
        username = findViewById(R.id.usernameText);
        message = findViewById(R.id.messageForm);
        adjunt = findViewById(R.id.attachMessage);
        send = findViewById(R.id.sendMessage);
        ImageView serviceRequest = findViewById(R.id.serviceRequestButton);
        serviceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatWithAnotherUserActivity.this, AskServiceActivity.class);
                intent.putExtra("otherUserEmail", chatWith);
                startActivity(intent);
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        final DocumentReference documentReference = firestore.collection("Usuari").document(currentUser.getEmail());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUsuari = documentSnapshot.toObject(Usuari.class);
                if (!currentUsuari.getXats().containsKey(chatWith))
                    first = true;
                openChat();
            }
        });

        final DocumentReference documentReferenceOther = firestore.collection("Usuari").document(chatWith);
        documentReferenceOther.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuariChatWith = documentSnapshot.toObject(Usuari.class);
                if (!usuariChatWith.getXats().containsKey(currentUser.getEmail())) {
                    otherFirst = true;
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(true, message.getText().toString());
            }
        });

        adjunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
                    else openGallery();
                }
                else openGallery();
            }
        });
    }

    private void createNewChat() {
        final DocumentReference documentReferenceXat = firestore.collection("Xat").document();
        xatID = documentReferenceXat.getId();
        documentReferenceXat.set(xat);
        final DocumentReference documentReferenceMyUsuari = firestore.collection("Usuari").document(currentUser.getEmail());
        documentReferenceMyUsuari.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    currentUsuari = documentSnapshot.toObject(Usuari.class);
                    currentUsuari.addXatWithUser(chatWith, xatID);
                    documentReferenceMyUsuari.update("xats", currentUsuari.getXats()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final DocumentReference documentReferenceUsuari = firestore.collection("Usuari").document(chatWith);
                            documentReferenceUsuari.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    usuariChatWith = documentSnapshot.toObject(Usuari.class);
                                    usuariChatWith.addXatWithUser(currentUser.getEmail(), xatID);
                                    documentReferenceUsuari.update("xats", usuariChatWith.getXats()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Open Xat
                                            first = false;
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
                if (documentSnapshot.exists()) {
                    usuariChatWith = documentSnapshot.toObject(Usuari.class);
                    getImageFromStorage(usuariChatWith.getFoto());
                    username.setText(usuariChatWith.getNom());
                }
            }
        });
        chatAdapter = new ChatAdapter(ChatWithAnotherUserActivity.this);
        messagesList.setAdapter(chatAdapter);
        if (first) {
            if (currentUser.getEmail().compareTo(chatWith) > 0)
                xat = new Xat(chatWith, currentUser.getEmail());
            else xat = new Xat(currentUser.getEmail(), chatWith);
            return;
        }
        List<String> ids = currentUsuari.getXats().get(chatWith);
        xatID = currentUsuari.getXats().get(chatWith).get(currentUsuari.getXats().get(chatWith).size() - 1);
        if (ids.size() > 1) {
            List<List<Missatge>> previousMessages = new ArrayList<>();
            String u1, u2;
            if (currentUser.getEmail().compareTo(chatWith) > 0) {
                u1 = chatWith;
                u2 = currentUser.getEmail();
            } else {
                u1 = currentUser.getEmail();
                u2 = chatWith;
            }
            missatges = new ArrayList<>(ids.size());
            final CollectionReference collectionReferenceXats = firestore.collection("Xat");
            collectionReferenceXats.whereEqualTo("usuari1", u1);
            collectionReferenceXats.whereEqualTo("usuari2", u2);
            collectionReferenceXats.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            List<Integer> ind = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (ids.contains(document.getId())) {
                                    Xat xat = document.toObject(Xat.class);
                                    int index;
                                    boolean trobat = false;
                                    for (index = 0; index < ids.size() & !trobat; ++index) {
                                        if (ids.get(index).equals(document.getId()))
                                            trobat = true;
                                    }
                                    index = index - 1;
                                    previousMessages.add(xat.getMissatges());
                                    ind.add(index);
                                }
                            }
                            int last = ids.size() - 1;
                            for (int i = 0; i < ids.size()-1; ++i)
                                for (int pos = 0; pos < ind.size(); ++pos) {
                                    if (i == ind.get(pos))
                                        missatges.addAll(previousMessages.get(pos));
                                    else if (ind.get(pos) == ids.size() - 1)
                                        last = pos;
                                }
                            chatAdapter.setPrevious(missatges);
                            chatAdapter.addMissatges(previousMessages.get(last));
                            messagesList.setSelection(messagesList.getCount() - 1);
                        }
                    } else
                        Toast.makeText(ChatWithAnotherUserActivity.this, "Cannot load", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            messagesList.setSelection(messagesList.getCount() - 1);
            firstReadOfLastChat = true;
        }


        final DocumentReference documentReference = firestore.collection("Xat").document(xatID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    xat = documentSnapshot.toObject(Xat.class);
                    if (firstReadOfLastChat) {
                        chatAdapter.addMissatges(xat.getMissatges());
                        firstReadOfLastChat = false;
                    }
                    else chatAdapter.addMissatge(xat.getMissatges().get(xat.getMissatges().size()-1));
                    messagesList.setSelection(messagesList.getCount() - 1);
                }
            }
        });
    }

    private void sendMessage(boolean isText, String text) {
        if (text.length() > 0) {
            if (first || otherFirst) {
                if (otherFirst) {
                    if (currentUser.getEmail().compareTo(chatWith) > 0)
                        xat = new Xat(chatWith, currentUser.getEmail());
                    else xat = new Xat(currentUser.getEmail(), chatWith);
                }
                createNewChat();
            }
            Missatge missatge;
            if (isText)
                missatge = new Missatge(currentUser.getEmail(), chatWith, text, null);
            else missatge = new Missatge(currentUser.getEmail(), chatWith, null, text);
            xat.addMissatge(missatge);
            final DocumentReference documentReference = firestore.collection("Xat").document(xatID);
            documentReference.update("missatges", xat.getMissatges());
            message.setText(null);
            sendNotification(chatWith, currentUser.getEmail(), text);
        }
    }

    private void sendNotification(final String chatWith, final String email, final String text) {
        final DocumentReference documentReferenceUsuari = firestore.collection("Usuari").document(chatWith);
        documentReferenceUsuari.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String token = (String) documentSnapshot.get("token");
                Data data = new Data(email, username.getText().toString(), text, chatWith);
                Sender sender = new Sender(data, token);
                apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    }
                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                    }
                });
            }
        });
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

    private void saveImage() {
        String name = Calendar.getInstance().getTime().toString();
        String[] time = name.split("GMT");
        time = time[0].split(":");
        name = time[0].concat(time[1]).concat(time[2]);
        time = name.split(" ");
        name = time[0];
        for (int i = 1; i < time.length; ++i)
            name = name.concat(time[i]);

        name = xatID.concat(name);

        String extension = imgUri.getLastPathSegment();
        extension = extension.substring(extension.lastIndexOf('.'));

        imagePath = storage.getReference().child("ImageMessage").child(name.concat(extension));
        imagePath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sendMessage(false, imagePath.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatWithAnotherUserActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            openGallery();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
            imgUri = data.getData();
            saveImage();
        }
    }
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        super.onKeyDown(keyCode, event);
        return false;
    }*/
}