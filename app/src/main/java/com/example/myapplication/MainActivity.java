package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.ActivityMainBinding;

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
        binding.fresh.setOnClickListener(v -> viewModel.getFreshProduct().observe(MainActivity.this, products -> {
            if(products!=null&& !products.isEmpty()){
                setupRecyclerView(products);
            }
        }));
        binding.deadline.setOnClickListener(v -> viewModel.getDeadlineProduct().observe(MainActivity.this, products -> {
            if(products!=null&& !products.isEmpty()){
                setupRecyclerView(products);
            }
        }));
        binding.failed.setOnClickListener(v -> viewModel.getFailedProduct().observe(MainActivity.this, products -> {
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
}
