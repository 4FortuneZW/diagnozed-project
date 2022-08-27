package com.example.diagnozed.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivityAssessmentResultBinding;
import com.example.diagnozed.utilities.Constants;
import com.example.diagnozed.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;

import java.util.HashMap;

public class AssessmentResultActivity extends AppCompatActivity {

    private ActivityAssessmentResultBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssessmentResultBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {

        binding.homeButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));

        switch (preferenceManager.getString(Constants.KEY_AUTISM_RESULT)) {
            case "Risiko rendah" :
                binding.rendah.setVisibility(View.VISIBLE);
                break;
            case "Risiko sedang" :
                binding.sedang.setVisibility(View.VISIBLE);
                break;
            case "Risiko tinggi" :
                binding.tinggi.setVisibility(View.VISIBLE);
                break;
        }

        switch (preferenceManager.getString(Constants.KEY_SPEECH_DELAY_RESULT)) {
            case "Suspect" :
                binding.suspek.setVisibility(View.VISIBLE);
                break;
            case "Normal" :
                binding.normal.setVisibility(View.VISIBLE);
                break;
        }

        binding.backButton.setOnClickListener(v -> onBackPressed());
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String id = String.valueOf(getIntent().getStringExtra("id"));
//        database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
//                .document(id)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if(task.isSuccessful() && task.getResult() != null) {
//                        DocumentSnapshot documentSnapshot = task.getResult();
//                        binding.assementId.setText("Id : " + id);
//                        binding.assessmentResultAutism.setText("Autism : "
//                                + documentSnapshot.get(Constants.KEY_AUTISM_RESULT) + "/5");
//                        binding.assessmentResultSpeechDelay.setText("Speech Delay : "
//                                + documentSnapshot.get(Constants.KEY_SPEECH_DELAY_RESULT) + "/5");
//                    }
//                });

        binding.autismAssessmentButton.setOnClickListener(v -> {
            binding.autismAssessmentButton.setBackgroundColor(getColor(R.color.secondary_text));
            binding.speechDelayAssessmentButton.setBackgroundColor(getColor(R.color.primary));
            binding.keteranganAutis.setVisibility(View.VISIBLE);
            binding.keteranganSpeda.setVisibility(View.GONE);
        });

        binding.speechDelayAssessmentButton.setOnClickListener(v -> {
            binding.autismAssessmentButton.setBackgroundColor(getColor(R.color.primary));
            binding.speechDelayAssessmentButton.setBackgroundColor(getColor(R.color.secondary_text));
            binding.keteranganAutis.setVisibility(View.GONE);
            binding.keteranganSpeda.setVisibility(View.VISIBLE);
        });

        binding.assessmentResultAutism.setText("Autism : "
                + preferenceManager.getString(Constants.KEY_AUTISM_RESULT));
        binding.assessmentResultSpeechDelay.setText("Speech Delay : "
                + preferenceManager.getString(Constants.KEY_SPEECH_DELAY_RESULT));

        binding.resultLayout.setVisibility(View.VISIBLE);

        binding.buttonResultToConsultPage.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(getApplicationContext(), ChatMainActivity.class);
            startActivity(intent);
        });
    }



}