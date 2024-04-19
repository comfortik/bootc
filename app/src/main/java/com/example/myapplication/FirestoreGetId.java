package com.example.myapplication;


import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreGetId {
    FirebaseFirestore fb;
    public FirestoreGetId(FirebaseFirestore fb){
        this.fb=fb;
    }

    public void getId(String currentUserId, final OnIdReturnedListener listener){
        fb.collection("Users")
                .whereEqualTo("uid", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                        listener.onIdReturned(id);
                    } else {
                        listener.onIdReturned(null);
                    }
                });
    }



}

