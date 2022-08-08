package com.example.diagnozed.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivityAssessmentBinding;
import com.example.diagnozed.utilities.Constants;
import com.example.diagnozed.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;

public class AssessmentActivity extends AppCompatActivity {

    private ActivityAssessmentBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssessmentBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {

        binding.backButton.setOnClickListener(v -> onBackPressed());

        binding.assessmentSubmitButton.setOnClickListener(v -> {
            if (isValidAnswers()) {
                loading(true);

                HashMap<String, Object> assessmentResult = new HashMap<>();
                RadioButton radioButton;

                int[] checkedRadioIds = {binding.answersNo1.getCheckedRadioButtonId(), binding.answersNo2.getCheckedRadioButtonId(),
                        binding.answersNo3.getCheckedRadioButtonId(), binding.answersNo4.getCheckedRadioButtonId(), binding.answersNo5.getCheckedRadioButtonId(),
                        binding.answersNo6.getCheckedRadioButtonId(), binding.answersNo7.getCheckedRadioButtonId(), binding.answersNo8.getCheckedRadioButtonId(),
                        binding.answersNo9.getCheckedRadioButtonId(), binding.answersNo10.getCheckedRadioButtonId()};

                String[] stateIds = {getResources().getString(R.string.state_no_1), getResources().getString(R.string.state_no_2),
                        getResources().getString(R.string.state_no_3), getResources().getString(R.string.state_no_4),
                        getResources().getString(R.string.state_no_5), getResources().getString(R.string.state_no_6),
                        getResources().getString(R.string.state_no_7), getResources().getString(R.string.state_no_8),
                        getResources().getString(R.string.state_no_9), getResources().getString(R.string.state_no_10)};

                String namaAnakRaw = binding.inputNamaAnak.getText().toString().trim();
                String namaAnak = namaAnakRaw.toLowerCase().replaceAll("\\s", "");

                assessmentResult.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
                assessmentResult.put(Constants.KEY_NAMA_ANAK, namaAnak);
                assessmentResult.put(Constants.KEY_USIA_ANAK, binding.inputUsiaAnak.getText().toString().trim());


                int autismResult = 0;
                int speechDelayResult = 0;
                int stateIter = 0;
                for (int checkedRadioId : checkedRadioIds) {
                    radioButton = findViewById(checkedRadioId);
                    assessmentResult.put(stateIds[stateIter],
                            radioButton.getText().toString().trim());
                    if (stateIter < 5 && radioButton.getText().toString().trim().equals("Ya")){
                        autismResult++;
                    } else if (radioButton.getText().toString().trim().equals("Ya")) {
                        speechDelayResult++;
                    }
                    stateIter++;
                }

                preferenceManager.putString(Constants.KEY_AUTISM_RESULT, String.valueOf(autismResult));
                preferenceManager.putString(Constants.KEY_SPEECH_DELAY_RESULT, String.valueOf(speechDelayResult));
                assessmentResult.put(Constants.KEY_AUTISM_RESULT, autismResult);
                assessmentResult.put(Constants.KEY_SPEECH_DELAY_RESULT, speechDelayResult);

                FirebaseFirestore database = FirebaseFirestore.getInstance();

//                assessmentResult.put("timestamp", FieldValue.serverTimestamp());
//                database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
//                        .add(assessmentResult)
//                        .addOnSuccessListener(documentReference -> preferenceManager.putString(Constants.KEY_ASSESSMENT_ID, documentReference.getId()));
//
//                database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
//                        .document("DB2MtP0stAFHqRwOsQVw")
//                        .update(assessmentResult);

                database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
                        .whereEqualTo(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL))
                        .whereEqualTo(Constants.KEY_NAMA_ANAK, namaAnak)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult().getDocuments().size() > 0) {
                                assessmentResult.put("timestamp", FieldValue.serverTimestamp());
                                updateResult(true, assessmentResult, task.getResult().getDocuments().get(0).getId());
//                                showToast("success");
                            } else {
//                                showToast("tidak");
                                assessmentResult.put("timestamp", FieldValue.serverTimestamp());
                                updateResult(false, assessmentResult, "");

                            }
                        });

//                loading(false);
//                Intent intent = new Intent(getApplicationContext(), AssessmentResultActivity.class);
//                startActivity(intent);
//                showToast("WOY BANGUN WOY");
            }
        });

    }

    private void updateResult(boolean exist, HashMap<String,Object> assessmentResult, String id) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        if (exist) {
            database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
                    .document(id)
                    .update(assessmentResult)
                    .addOnSuccessListener(unused -> {
                        loading(false);
                        Intent intent = new Intent(getApplicationContext(),AssessmentResultActivity.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    });
        } else {
            database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
                    .add(assessmentResult)
                    .addOnSuccessListener(documentReference -> {
                        Intent intent = new Intent(getApplicationContext(),AssessmentResultActivity.class);
                        intent.putExtra("id",documentReference.getId());
                        startActivity(intent);
                    });
        }
    }

    private boolean isValidAnswers() {
        boolean isNotValidAnswers;
        isNotValidAnswers = binding.answersNo1.getCheckedRadioButtonId() == -1 || binding.answersNo2.getCheckedRadioButtonId() == -1
                || binding.answersNo3.getCheckedRadioButtonId() == -1 || binding.answersNo4.getCheckedRadioButtonId() == -1 ||
                binding.answersNo5.getCheckedRadioButtonId() == -1 || binding.answersNo6.getCheckedRadioButtonId() == -1 ||
                binding.answersNo7.getCheckedRadioButtonId() == -1 || binding.answersNo8.getCheckedRadioButtonId() == -1 ||
                binding.answersNo8.getCheckedRadioButtonId() == -1 || binding.answersNo10.getCheckedRadioButtonId() == -1;
        if (isNotValidAnswers) {
            showToast("Pastikan semua jawaban terisi");
            return false;
        }
        return true;
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.submitProgressBar.setVisibility(View.VISIBLE);
            binding.assessmentSubmitButton.setVisibility(View.INVISIBLE);
        } else {
            binding.submitProgressBar.setVisibility(View.INVISIBLE);
            binding.assessmentSubmitButton.setVisibility(View.VISIBLE);
        }
    }

}