package com.example.myapplication;

import android.Manifest;
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
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.security.Permission;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private MainViewModel viewModel;
    List<Product> productList = new ArrayList<>();
    final int NOTIFICATION_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
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
        scheduleNotifications(productList);
        adapter.setItems(productList);
        adapter.setItemClickListener(diaryEntry -> adapter.openFragment(this, diaryEntry, getLayoutInflater()));
        binding.recycler.setAdapter(adapter);
        binding.recycler.setClipToPadding(false);
    }
    public LocalDate convertStringToDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yy");
        return LocalDate.parse(dateStr, formatter);
    }
    public void scheduleNotifications(List<Product> productList) {
        for (Product product : productList) {
            if (product.getFreshnessId() == 2) {
                long delay = calculateDelay(product);
                scheduleNotification(product, delay);
            }
        }
    }

    private long calculateDelay(Product product) {
        LocalDate expirationDate = convertStringToDate(product.getData());
        LocalDate today = LocalDate.now();
        long daysUntilExpiration = today.until(expirationDate, ChronoUnit.DAYS);
        return TimeUnit.DAYS.toMillis(daysUntilExpiration - 2); // Notify 2 days before expiration
    }

    private void scheduleNotification(Product product, long delay) {
        Data inputData = new Data.Builder()
                .putString("productName", product.getName())
                .putString("productData", product.getData())
                .putInt("freshnessId", product.getFreshnessId())
                .build();
        OneTimeWorkRequest notificationWorkRequest = new OneTimeWorkRequest.Builder(uploadWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance().enqueue(notificationWorkRequest);

    }
}
