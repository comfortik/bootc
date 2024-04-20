package com.example.myapplication;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.security.Permission;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private MainViewModel viewModel;
    List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS},101);
            }
            else {

            }
        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getProductList().observe(this, productList -> {
            if (productList != null && !productList.isEmpty()) {
                setupRecyclerView(productList);
            }
        });
        binding.all.setOnClickListener(v -> viewModel.getProductList().observe(MainActivity.this, productList -> {
            if (productList != null && !productList.isEmpty()) {
                setupRecyclerView(productList);
            }
        }));
        binding.fresh.setOnClickListener(v -> viewModel.getFreshnessProduct(1).observe(MainActivity.this, products -> {
            if(products!=null&& !products.isEmpty()){
                setupRecyclerView(products);
            }
        }));
        binding.deadline.setOnClickListener(v -> viewModel.getFreshnessProduct(2).observe(MainActivity.this, products -> {
            if(products!=null&& !products.isEmpty()){
                setupRecyclerView(products);
            }
        }));
        binding.failed.setOnClickListener(v -> viewModel.getFreshnessProduct(3).observe(MainActivity.this, products -> {
            if(products!=null&& !products.isEmpty()){
                setupRecyclerView(products);
            }
        }));
    }

    private void setupRecyclerView(List<Product> productList) {
        RecyclerAdapter adapter = new RecyclerAdapter(this);

        adapter.setItems(productList);
        adapter.setItemClickListener(diaryEntry -> adapter.openFragment(this, diaryEntry, getLayoutInflater()));
        binding.recycler.setAdapter(adapter);
        binding.recycler.setClipToPadding(false);
    }
    public LocalDate convertStringToDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yy");
        return LocalDate.parse(dateStr, formatter);
    }
}
