package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.DropDownProductBinding;

import org.checkerframework.checker.units.qual.C;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    DropDownProductBinding binding;
    ItemClickListener itemClickListener;
    public RecyclerViewHolder(DropDownProductBinding binding, ItemClickListener itemClickListener) {
        super(binding.getRoot());
        this.binding=binding;
        this.itemClickListener=itemClickListener;
    }
    public void bind(Product product, Context context){
        binding.nameProduct.setText(product.getName());
        binding.freshnessDateProduct.setText(product.getData());
        binding.getRoot().setOnClickListener(v -> itemClickListener.onItemClick(product));
        setFreshness(product, context);
    }
    public void setFreshness(Product product, Context context){
        float cornerRadius = 20f;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(cornerRadius);
        switch (product.getFreshnessId()){
            case 1:
                drawable.setColor(ContextCompat.getColor(context, R.color.fresh));
                binding.freshness.setBackgroundColor(ContextCompat.getColor(context, R.color.fresh));
                binding.freshness.setBackground(drawable);
                binding.freshness.setText("Свежий");
                break;
            case 2:
                drawable.setColor(ContextCompat.getColor(context, R.color.expiration));
                binding.freshness.setBackgroundColor(ContextCompat.getColor(context, R.color.expiration));
                binding.freshness.setBackground(drawable);
                binding.freshness.setText("Истекает срок");
                break;
            case 3:
                drawable.setColor(ContextCompat.getColor(context, R.color.ruined));
                binding.freshness.setBackgroundColor(ContextCompat.getColor(context, R.color.ruined));
                binding.freshness.setBackground(drawable);
                binding.freshness.setText("Испорчен");
                break;
        }
    }
}
