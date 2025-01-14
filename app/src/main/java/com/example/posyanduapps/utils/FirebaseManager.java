package com.example.posyanduapps.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    private static final String DATABASE_URL = "https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance(DATABASE_URL);

    public interface FirebaseCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    public static DatabaseReference getReference(String path) {
        return database.getReference(path);
    }

    public static void readData(String path, FirebaseCallback<DataSnapshot> callback) {
        DatabaseReference ref = getReference(path);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onSuccess(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public static void writeData(String path, Object data, FirebaseCallback<Void> callback) {
        DatabaseReference ref = getReference(path);
        ref.setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess(null);
            } else {
                callback.onError(task.getException() != null ? task.getException().getMessage() : "Unknown error");
            }
        });
    }

    public static void updateData(String path, Map<String, Object> updates, FirebaseCallback<Void> callback) {
        DatabaseReference ref = getReference(path);
        ref.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess(null);
            } else {
                callback.onError(task.getException() != null ? task.getException().getMessage() : "Unknown error");
            }
        });
    }
}
