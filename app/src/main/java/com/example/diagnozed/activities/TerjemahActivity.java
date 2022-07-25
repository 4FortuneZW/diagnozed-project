package com.example.diagnozed.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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
//            startActivity(new Intent(getApplicationContext(), org.tensorflow.lite.examples.detection.DetectorActivity.class));

//            Intent intent = null;
//            try {
//                intent = new Intent(this,
//                        Class.forName("org.tensorflow.lite.examples.detection.DetectorActivity"));
//                startActivity(intent);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }

            Intent intent = null;
            try {
                intent = new Intent(this,
                        Class.forName("com.example.imagepro.CombineLettersActivity"));
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        binding.indoKeIsyarat.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), IndonesiaKeIsyaratActivity.class));
        });
    }
}