package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.AlertProductBinding;
import com.example.myapplication.databinding.DropDownProductBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    AlertProductBinding binding;
    List<Product> productList = new ArrayList<>();
    ItemClickListener itemClickListener;
    Context context;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DropDownProductBinding binding= DropDownProductBinding.inflate(layoutInflater, parent, false);
        return new RecyclerViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, context);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener =itemClickListener;
    }
    public void setItems(List<Product> productList) {
        int itemCount = getItemCount();
        this.productList = new ArrayList<>(productList);
        notifyItemRangeChanged(0, Math.max(itemCount, getItemCount()));
    }
    public  void openFragment(Context context, Product product, LayoutInflater layoutInflater){
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        binding = AlertProductBinding.inflate(layoutInflater, null, false);
        builder.setView(binding.getRoot());
        binding.nameProduct.setText(product.getName());
        binding.freshnessDateProduct.setText(product.getData());
        setFreshness(product, context);
        alertDialog = builder.setView(binding.getRoot()).show();
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }
    public void setFreshness(Product product, Context context){
        float cornerRadius = 20f;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(cornerRadius);

        switch (product.getFreshnessId()){
            case 1:
                drawable.setColor(ContextCompat.getColor(context, R.color.fresh));
                binding.freshness.setBackground(drawable);
                binding.freshness.setText("Свежий");
                break;
            case 2:
                drawable.setColor(ContextCompat.getColor(context, R.color.expiration));
                binding.freshness.setBackground(drawable);
                binding.freshness.setText("Истекает срок");
                break;
            case 3:
                drawable.setColor(ContextCompat.getColor(context, R.color.ruined));
                binding.freshness.setBackground(drawable);
                binding.freshness.setText("Испорчен");
                break;
        }
    }
    void deleteItem(int index) {
        productList.remove(index);
        notifyItemRemoved(index);
    }
}
