package com.example.diagnozed.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivitySearchResultBinding;
import com.example.diagnozed.utilities.Constants;
import com.example.diagnozed.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private ActivitySearchResultBinding binding;
    private FirebaseFirestore database;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        database = FirebaseFirestore.getInstance();
        intent = getIntent();
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {

//        String[] statements =
//                        {getResources().getString(R.string.state_no_1), getResources().getString(R.string.state_no_2),
//                        getResources().getString(R.string.state_no_3), getResources().getString(R.string.state_no_4),
//                        getResources().getString(R.string.state_no_5), getResources().getString(R.string.state_no_6),
//                        getResources().getString(R.string.state_no_7), getResources().getString(R.string.state_no_8),
//                        getResources().getString(R.string.state_no_9), getResources().getString(R.string.state_no_10)};

        binding.autismAssessmentButton.setOnClickListener(v -> {
            binding.autismAssessmentButton.setBackgroundColor(getColor(R.color.secondary_text));
            binding.speechDelayAssessmentButton.setBackgroundColor(getColor(R.color.primary));
            binding.asesmenAutis.setVisibility(View.VISIBLE);
            binding.asesmenSpeechDelay.setVisibility(View.GONE);
        });

        binding.speechDelayAssessmentButton.setOnClickListener(v -> {
            binding.speechDelayAssessmentButton.setBackgroundColor(getColor(R.color.secondary_text));
            binding.autismAssessmentButton.setBackgroundColor(getColor(R.color.primary));
            binding.asesmenSpeechDelay.setVisibility(View.VISIBLE);
            binding.asesmenAutis.setVisibility(View.GONE);
        });

        binding.searchVector.setOnClickListener(v -> {
            searchResult();
        });

        binding.backButton.setOnClickListener(v -> onBackPressed());

//        if (preferenceManager.getString(Constants.KEY_ROLE).equals("user")) {
//            database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
//                    .document(intent.getStringExtra("historyId"))
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                            binding.namaAnak.setText("Nama anak : " + documentSnapshot.getString(Constants.KEY_NAMA_ANAK));
//                            binding.usiaAnak.setText("Usia anak : " + documentSnapshot.getString(Constants.KEY_USIA_ANAK));
//                            binding.autismResultInfo.setText(String.format("Hasil diagnosa autis : %d/5"
//                                    , documentSnapshot.getLong(Constants.KEY_AUTISM_RESULT))
//                            );
//                            binding.speechDelayResultInfo.setText(String.format("Hasil diagnosa speech delay : %d/5"
//                                    , documentSnapshot.getLong(Constants.KEY_SPEECH_DELAY_RESULT))
//                            );
//                            binding.q1Info.setText(statements[0] + " : " + documentSnapshot.getString(statements[0]));
//                            binding.q2Info.setText(statements[1] + " : " + documentSnapshot.getString(statements[1]));
//                            binding.q3Info.setText(statements[2] + " : " + documentSnapshot.getString(statements[2]));
//                            binding.q4Info.setText(statements[3] + " : " + documentSnapshot.getString(statements[3]));
//                            binding.q5Info.setText(statements[4] + " : " + documentSnapshot.getString(statements[4]));
//                            binding.q6Info.setText(statements[5] + " : " + documentSnapshot.getString(statements[5]));
//                            binding.q7Info.setText(statements[6] + " : " + documentSnapshot.getString(statements[6]));
//                            binding.q8Info.setText(statements[7] + " : " + documentSnapshot.getString(statements[7]));
//                            binding.q9Info.setText(statements[8] + " : " + documentSnapshot.getString(statements[8]));
//                            binding.q10Info.setText(statements[9] + " : " + documentSnapshot.getString(statements[9]));
//
//                            binding.results.setVisibility(View.VISIBLE);
//                        }
//                    });
//        }

    }

    private void searchResult() {
//        String[] statements = {getResources().getString(R.string.state_no_1), getResources().getString(R.string.state_no_2),
//                getResources().getString(R.string.state_no_3), getResources().getString(R.string.state_no_4),
//                getResources().getString(R.string.state_no_5), getResources().getString(R.string.state_no_6),
//                getResources().getString(R.string.state_no_7), getResources().getString(R.string.state_no_8),
//                getResources().getString(R.string.state_no_9), getResources().getString(R.string.state_no_10)};


        database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString().trim())
                .whereEqualTo(Constants.KEY_NAMA_ANAK, binding.inputNamaAnak.getText().toString().trim())
                .whereEqualTo(Constants.KEY_USIA_ANAK, binding.inputUsiaAnak.getText().toString().trim())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        List<Integer> autismCheckedboxesId = (List<Integer>) documentSnapshot.get(Constants.KEY_AUTISM_CHECKEDBOXES);
                        List<Integer> spedaCheckedboxesId = (List<Integer>) documentSnapshot.get(Constants.KEY_SPEDA_CHECKEDBOXES);
                        for (Integer checkboxId : autismCheckedboxesId) {
                            CheckBox checkBox;
                            checkBox = findViewById(checkboxId);
                            checkBox.setChecked(true);
                        }

                        for (Integer checkboxId : spedaCheckedboxesId) {
                            CheckBox checkBox;
                            checkBox = findViewById(checkboxId);
                            checkBox.setChecked(true);
                        }

                        switch (documentSnapshot.getString(Constants.KEY_USIA_ANAK)) {
                            case "1" :
                                binding.satuTahun.setVisibility(View.VISIBLE);
                                break;
                            case "2" :
                                binding.duaTahun.setVisibility(View.VISIBLE);
                                break;
                            case "3" :
                                binding.tigaTahun.setVisibility(View.VISIBLE);
                                break;
                            case "4" :
                                binding.empatTahun.setVisibility(View.VISIBLE);
                                break;
                            case "5" :
                                binding.limaTahun.setVisibility(View.VISIBLE);
                                break;
                            case "6" :
                                binding.enamTahun.setVisibility(View.VISIBLE);
                                break;
                        }

                        binding.result.setVisibility(View.VISIBLE);
                        binding.autismResultInfo.setText("Hasil asesmen autis : " +
                                documentSnapshot.getString(Constants.KEY_AUTISM_RESULT));
                        binding.speechDelayResultInfo.setText("Hasil asesmen speech delay : " +
                                documentSnapshot.getString(Constants.KEY_SPEECH_DELAY_RESULT));


                    }
                })
                .addOnFailureListener(exception -> Toast.makeText(getApplicationContext(), "Data yang Anda masukkan salah", Toast.LENGTH_SHORT).show());

//        if (preferenceManager.getString(Constants.KEY_ROLE).equals("doctor")) {
//            database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
//                    .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString().trim())
//                    .whereEqualTo(Constants.KEY_NAMA_ANAK, binding.inputNamaAnak.getText().toString().trim())
//                    .whereEqualTo(Constants.KEY_USIA_ANAK, binding.inputUsiaAnak.getText().toString().trim())
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
//                            binding.namaAnak.setText("Nama anak : " + documentSnapshot.getString(Constants.KEY_NAMA_ANAK));
//                            binding.usiaAnak.setText("Usia anak : " + documentSnapshot.getString(Constants.KEY_USIA_ANAK));
//                            binding.autismResultInfo.setText(String.format("Hasil diagnosa autis : %d/5"
//                                    , documentSnapshot.getLong(Constants.KEY_AUTISM_RESULT))
//                            );
//                            binding.speechDelayResultInfo.setText(String.format("Hasil diagnosa speech delay : %d/5"
//                                    , documentSnapshot.getLong(Constants.KEY_SPEECH_DELAY_RESULT))
//                            );
//                            binding.q1Info.setText(statements[0] + " : " + documentSnapshot.getString(statements[0]));
//                            binding.q2Info.setText(statements[1] + " : " + documentSnapshot.getString(statements[1]));
//                            binding.q3Info.setText(statements[2] + " : " + documentSnapshot.getString(statements[2]));
//                            binding.q4Info.setText(statements[3] + " : " + documentSnapshot.getString(statements[3]));
//                            binding.q5Info.setText(statements[4] + " : " + documentSnapshot.getString(statements[4]));
//                            binding.q6Info.setText(statements[5] + " : " + documentSnapshot.getString(statements[5]));
//                            binding.q7Info.setText(statements[6] + " : " + documentSnapshot.getString(statements[6]));
//                            binding.q8Info.setText(statements[7] + " : " + documentSnapshot.getString(statements[7]));
//                            binding.q9Info.setText(statements[8] + " : " + documentSnapshot.getString(statements[8]));
//                            binding.q10Info.setText(statements[9] + " : " + documentSnapshot.getString(statements[9]));
//
//                            binding.results.setVisibility(View.VISIBLE);
//
//                        };
//                    })
//                    .addOnFailureListener(exception -> {
//                        Toast.makeText(getApplicationContext(), "Data yang Anda masukkan salah", Toast.LENGTH_SHORT).show();
//                    });
//        } else {
//            database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
//                    .document(intent.getStringExtra("historyId"))
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                            binding.namaAnak.setText("Nama anak : " + documentSnapshot.getString(Constants.KEY_NAMA_ANAK));
//                            binding.usiaAnak.setText("Usia anak : " + documentSnapshot.getString(Constants.KEY_USIA_ANAK));
//                            binding.autismResultInfo.setText(String.format("Hasil diagnosa autis : %d/5"
//                                            , documentSnapshot.getLong(Constants.KEY_AUTISM_RESULT))
//                                    );
//                            binding.speechDelayResultInfo.setText(String.format("Hasil diagnosa speech delay : %d/5"
//                                    , documentSnapshot.getLong(Constants.KEY_SPEECH_DELAY_RESULT))
//                            );
//                            binding.q1Info.setText(statements[0] + " : " + documentSnapshot.getString(statements[0]));
//                            binding.q2Info.setText(statements[1] + " : " + documentSnapshot.getString(statements[1]));
//                            binding.q3Info.setText(statements[2] + " : " + documentSnapshot.getString(statements[2]));
//                            binding.q4Info.setText(statements[3] + " : " + documentSnapshot.getString(statements[3]));
//                            binding.q5Info.setText(statements[4] + " : " + documentSnapshot.getString(statements[4]));
//                            binding.q6Info.setText(statements[5] + " : " + documentSnapshot.getString(statements[5]));
//                            binding.q7Info.setText(statements[6] + " : " + documentSnapshot.getString(statements[6]));
//                            binding.q8Info.setText(statements[7] + " : " + documentSnapshot.getString(statements[7]));
//                            binding.q9Info.setText(statements[8] + " : " + documentSnapshot.getString(statements[8]));
//                            binding.q10Info.setText(statements[9] + " : " + documentSnapshot.getString(statements[9]));
//
//                            binding.results.setVisibility(View.VISIBLE);
//
//                        };
//                    })
//                    .addOnFailureListener(exception -> {
//                        Toast.makeText(getApplicationContext(), "Id salah", Toast.LENGTH_SHORT).show();
//                    });
//        }


    }
}