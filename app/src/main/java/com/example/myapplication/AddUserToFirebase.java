package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddUserToFirebase {
    FirebaseFirestore fb;
    FirebaseAuth mAuth;
    Context context;
    OnAddUserToFirestore onAddUserToFirestore;



    static String id;
    public AddUserToFirebase( FirebaseFirestore fb, FirebaseAuth mAuth){

        this.fb=fb;
        this.mAuth=mAuth;
    }
    public static String getId() {
        return id;
    }
    public void anonimouseSignUp() {
        mAuth.getInstance();
        fb=FirebaseFirestore.getInstance();
        try {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(MainActivity.class.newInstance(), task -> {
                        if (task.isSuccessful()) {
                            userExist();
                        } else {
                        }
                    });
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
    private void userExist() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            fb.collection("Users")
                    .whereEqualTo("uid", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                onAddUserToFirestore.addUser();
                            } else {

                                addUser();

                            }
                        }
                    });

        } else {

        }
    }
    private void addUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid().toString();

        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", uid);
        fb.collection("Users")
                .add(userData)
                .addOnSuccessListener(documentReference -> {
                    id = documentReference.getId();
                    onAddUserToFirestore.addUser();
                })
                .addOnFailureListener(e -> {

                });


    }

    public void setOnAddUserToFirestore(OnAddUserToFirestore onAddUserToFirestore) {
        this.onAddUserToFirestore = onAddUserToFirestore;
    }

    public static class HelpEmotion {
        public String getHelp() {
            return help;
        }

        public int getHz() {
            return hz;
        }

        public boolean isYes() {
            return yes;
        }

        private String help;
        private int hz;
        private boolean yes;

        public HelpEmotion(String help, int hz, boolean yes) {
            this.help = help;
            this.hz = hz;
            this.yes = yes;
        }
    }
}
