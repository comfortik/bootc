package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainViewModel extends ViewModel {

    private final FirebaseFirestore fb;
    private final FirebaseAuth mAuth;
    private final AddUserToFirebase add;
    private final FirestoreGetId firestoreGetId;
    private MutableLiveData<List<Product>> productListLiveData;

    public MainViewModel() {
        fb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        add = new AddUserToFirebase(fb, mAuth);
        firestoreGetId = new FirestoreGetId(fb);
    }
    public LiveData<List<Product>> getProductList() {
        if (productListLiveData == null) {
            productListLiveData = new MutableLiveData<>();
            fetchData();
        }
        return productListLiveData;
    }
    private void fetchData() {
        add.anonimouseSignUp();
        add.setOnAddUserToFirestore(()->{
//            firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
//                fb.collection("Users")
//                        .document(userId)
//                        .collection("Products")
//                        .add(new Product("milk", "20:04:24"));
//            });
//            firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
//                fb.collection("Users")
//                        .document(userId)
//                        .collection("Products")
//                        .add(new Product("juice", "12:05:24"));
//            });
//            firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
//                fb.collection("Users")
//                        .document(userId)
//                        .collection("Products")
//                        .add(new Product("helpmepls", "01:10:23"));
//            });
                    getProducts();
                });


    }
    private void getProducts() {
        firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
            fb.collection("Users")
                    .document(userId)
                    .collection("Products")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Product> productList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot != null) {
                                Product product= documentSnapshot.toObject(Product.class);
                                setFreshness(product);

                                productList.add(product);
                            }
                        }
                        Collections.sort(productList, Comparator.comparing(Product::getData));
                        productListLiveData.postValue(productList);
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        });
    }
    public LocalDate convertStringToDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yy");
        return LocalDate.parse(dateStr, formatter);
    }
    public LiveData<List<Product>> getFreshnessProduct(int id)
    {
        MutableLiveData<List<Product>> filteredProductLiveData = new MutableLiveData<>();
        productListLiveData.observeForever(productList -> {
            if (productList != null && !productList.isEmpty()) {
                List<Product> filteredList = productList.stream()
                        .filter(product -> product.getFreshnessId() == id)
                        .collect(Collectors.toList());
                filteredProductLiveData.postValue(filteredList);
            }
        });
        return filteredProductLiveData;
    }
    public void setFreshness(Product product){
        LocalDate today = LocalDate.now();
        LocalDate srok = convertStringToDate(product.getData());
        if(today.isAfter(srok)){
            product.setFreshnessId(3);
        }
        else if(today.isAfter(srok.minusDays(3))){
            product.setFreshnessId(2);
        }
        else{
            product.setFreshnessId(1);
        }
    }



}
