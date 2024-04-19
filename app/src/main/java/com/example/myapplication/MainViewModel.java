package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
        add.setOnAddUserToFirestore(()-> getProducts());
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
                                productList.add(documentSnapshot.toObject(Product.class));
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
    public LiveData<List<Product>> getFreshProduct() {
        MutableLiveData<List<Product>> filteredProductLiveData = new MutableLiveData<>();
        productListLiveData.observeForever(productList -> {
            if (productList != null && !productList.isEmpty()) {
                List<Product> filteredList = productList.stream()
                        // Добавьте ваш фильтр здесь
                        .filter(product -> product.getFreshnessId() == 1 /* условие фильтрации */)
                        .collect(Collectors.toList());
                filteredProductLiveData.postValue(filteredList);
            }
        });
        return filteredProductLiveData;
    }

    public LiveData<List<Product>> getDeadlineProduct() {
        MutableLiveData<List<Product>> filteredProductLiveData = new MutableLiveData<>();
        productListLiveData.observeForever(productList -> {
            if (productList != null && !productList.isEmpty()) {
                List<Product> filteredList = productList.stream()
                        .filter(product -> product.getFreshnessId() == 2)
                        .collect(Collectors.toList());
                filteredProductLiveData.postValue(filteredList);
            }
        });
        return filteredProductLiveData;
    }
    public LiveData<List<Product>> getFailedProduct() {
        MutableLiveData<List<Product>> filteredProductLiveData = new MutableLiveData<>();
        productListLiveData.observeForever(productList -> {
            if (productList != null && !productList.isEmpty()) {
                List<Product> filteredList = productList.stream()
                        // Добавьте ваш фильтр здесь
                        .filter(product -> product.getFreshnessId() == 3)
                        .collect(Collectors.toList());
                filteredProductLiveData.postValue(filteredList);
            }
        });
        return filteredProductLiveData;
    }
}
