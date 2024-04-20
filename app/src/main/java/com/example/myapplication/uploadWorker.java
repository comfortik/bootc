package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class uploadWorker extends Worker {
    Product product;

    public uploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams,   Product product) {
        super(context, workerParams);
        this. product=product;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            NotificationHelper.sendNotification(getApplicationContext(), product);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}
