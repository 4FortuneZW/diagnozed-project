package com.example.diagnozed.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.diagnozed.DetectorActivity;
import com.example.diagnozed.databinding.ActivityTerjemahBinding;

public class TerjemahActivity extends AppCompatActivity {

    private ActivityTerjemahBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTerjemahBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.isyaratKeIndo.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), DetectorActivity.class));
        });

        binding.indoKeIsyarat.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), IndonesiaKeIsyaratActivity.class));
        });
    }
}