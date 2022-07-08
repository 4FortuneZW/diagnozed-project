package com.example.diagnozed.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivityIndonesiaKeIsyaratBinding;

public class IndonesiaKeIsyaratActivity extends AppCompatActivity {

    private ActivityIndonesiaKeIsyaratBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIndonesiaKeIsyaratBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.translateButton.setOnClickListener(v -> {
            String masukan = binding.translateInput.getText().toString().trim().toLowerCase();
            switch (masukan) {
                case "a" :
                    binding.handSign.setImageResource(R.drawable.ic_hand_picture_for_testing);
                    break;
                case "b" :
                    binding.handSign.setImageResource(R.drawable.ic_person);
                    break;
            }
        });
//        binding.aInSibi.setVisibility(View.VISIBLE);


//    private void translate(String masukan) {
//        Toast.makeText(getApplicationContext(), masukan, Toast.LENGTH_SHORT).show();
//        if (masukan.equals("a")) {
//            binding.aInSibi.setImageResource(R.drawable.ic_hand_picture_for_testing);
//        }

    }
}