package com.example.diagnozed.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivityIndonesiaKeIsyaratBinding;

public class IndonesiaKeIsyaratActivity extends AppCompatActivity {

    private ActivityIndonesiaKeIsyaratBinding binding;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIndonesiaKeIsyaratBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        linearLayout = findViewById(R.id.oneWord);

        setListeners();
    }

    private void setListeners() {
        binding.backButton.setOnClickListener(v -> onBackPressed());
        binding.translateButton.setOnClickListener(v -> {
            loading(true);

            int i;
            String masukan = binding.translateInput.getText().toString().replaceAll("\\s", "").toLowerCase();
            int[] oneWord = {R.id.firstWord240};
            int[] twoWords = {R.id.firstWord120,R.id.secondWord120};
            int[] threeWords = {R.id.firstWord80,R.id.secondWord80,R.id.thirdWord80};
            int[] fourWords = {R.id.firstWord60,R.id.secondWord60,R.id.thirdWord60,R.id.fourthWord60};
            int[] fiveWords = {R.id.firstWord48,R.id.secondWord48,R.id.thirdWord48,R.id.fourthWord48,R.id.fifthWord48};
            int[] moreWords = {R.id.firstWord40,R.id.secondWord40,R.id.thirdWord40,R.id.fourthWord40,R.id.fifthWord40,R.id.sixthWord40
            ,R.id.seventhWord40,R.id.eighthWord40,R.id.ninthWord40,R.id.tenthWord40,R.id.eleventhWord40,R.id.twelvethWord40,
            R.id.thirdteenthWord40,R.id.fourteenthWord40,R.id.fifteenthWord40,R.id.sixteenthWord40,R.id.seventeenthWord40,R.id.eighteenthWord40};
            int[] wordCategories = {R.id.oneWord,R.id.twoWords,R.id.threeWords,R.id.fourWords,R.id.fiveWords,R.id.moreWords};
            int[][] lettersPerCategory = {oneWord,twoWords,threeWords,fourWords,fiveWords,moreWords};

            if (masukan.length() >= 6) {
                linearLayout = findViewById(wordCategories[5]);
            } else {
                linearLayout = findViewById(wordCategories[masukan.length()-1]);
            }

            int n;
            if (masukan.length() >= 6) {
                n = 18;
            } else {
                n = masukan.length();
            }
            ImageView imageView;

            for (i = 0; i < n; i++) {

                if (masukan.length() >= 6) {
                    imageView = findViewById(lettersPerCategory[5][i]);
                } else {
                    imageView = findViewById(lettersPerCategory[masukan.length()-1][i]);
                }

                if (i >= masukan.length()) {
                    imageView.setVisibility(View.INVISIBLE);
                    continue;
                }

                switch (masukan.charAt(i)) {
                    case 'a' :
                        imageView.setImageResource(R.drawable.ic_hand_picture_for_testing);
                        break;
                    case 'b' :
                        imageView.setImageResource(R.drawable.ic_person);
                        break;
                    case 'c' :
                        imageView.setImageResource(R.drawable.ic_add);
                        break;
                    case 'd' :
                        imageView.setImageResource(R.drawable.ic_back);
                        break;
                    case 'e' :
                        imageView.setImageResource(R.drawable.ic_history);
                        break;
                    default:
                        imageView.setImageResource(R.drawable.ic_baseline_clear_24);
                }

                imageView.setVisibility(View.VISIBLE);
            }

            loading(false);


//            switch (masukan) {
//                case "a" :
//                    binding.handSign.setImageResource(R.drawable.ic_hand_picture_for_testing);
//                    break;
//                case "b" :
//                    binding.handSign.setImageResource(R.drawable.ic_person);
//                    break;
//            }

        });
//        binding.aInSibi.setVisibility(View.VISIBLE);


//    private void translate(String masukan) {
//        Toast.makeText(getApplicationContext(), masukan, Toast.LENGTH_SHORT).show();
//        if (masukan.equals("a")) {
//            binding.aInSibi.setImageResource(R.drawable.ic_hand_picture_for_testing);
//        }

    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}