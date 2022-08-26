package com.example.diagnozed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivityHistoryBinding;
import com.example.diagnozed.utilities.Constants;
import com.example.diagnozed.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private PreferenceManager preferenceManager;
    private int[] historyTextViewIds = {R.id.firstHistory,R.id.secondHistory,R.id.thirdHistory,R.id.fourthHistory,R.id.fifthHistory
    ,R.id.sixthHistory,R.id.seventhHistory,R.id.eighthHistory};
    private int[] historyNames = {R.id.firstHistoryName,R.id.secondHistoryName,R.id.thirdHistoryName
            ,R.id.fourthHistoryName,R.id.fifthHistoryName,R.id.sixthHistoryName,R.id.seventhHistoryName,R.id.eighthHistoryName};
    private int[] historyDates = {R.id.firstHistoryDate,R.id.secondHistoryDate,R.id.thirdHistoryDate,R.id.fourthHistoryDate
            ,R.id.fifthHistoryDate,R.id.sixthHistoryDate,R.id.seventhHistoryDate,R.id.eighthHistoryDate};
    private List<Integer> autismCheckedboxesId;
    private List<Integer> spedaCheckedboxesId;
    private String[] historyIds;
    private TextView nameView, dateView;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        historyIds = new String[6];
        database = FirebaseFirestore.getInstance();

        setContentView(binding.getRoot());

        setHistory();
        setListeners();
    }

    private void showResult(String id) {

        database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot documentSnapshot = task.getResult();

                        binding.autismResultInfo.setText("Hasil asesmen autis : " +
                                documentSnapshot.getString(Constants.KEY_AUTISM_RESULT));
                        binding.speechDelayResultInfo.setText("Hasil asesmen speech delay : " +
                                documentSnapshot.getString(Constants.KEY_SPEECH_DELAY_RESULT));

                        autismCheckedboxesId = (List<Integer>) documentSnapshot.get(Constants.KEY_AUTISM_CHECKEDBOXES);
                        spedaCheckedboxesId = (List<Integer>) documentSnapshot.get(Constants.KEY_SPEDA_CHECKEDBOXES);

                        for (int i = 0; i < autismCheckedboxesId.size(); i++) {
                            CheckBox checkBox;
                            checkBox = findViewById(autismCheckedboxesId.get(i).intValue());
                            checkBox.setChecked(true);
                            checkBox.setClickable(false);
                        }

                        for (int i = 0; i < spedaCheckedboxesId.size(); i++) {
                            CheckBox checkBox;
                            checkBox = findViewById(spedaCheckedboxesId.get(i).intValue());
                            checkBox.setChecked(true);
                            checkBox.setClickable(false);
                        }

//                        for (Long checkboxId : autismCheckedboxesId) {
//                            CheckBox checkBox;
//                            checkBox = findViewById((Integer) checkboxId);
//                            checkBox.setChecked(true);
//                            checkBox.setClickable(false);
//                        }
//
//                        for (Long checkboxId : spedaCheckedboxesId) {
//                            CheckBox checkBox;
//                            checkBox = findViewById((Integer) checkboxId);
//                            checkBox.setChecked(true);
//                            checkBox.setClickable(false);
//                        }

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
                    }
                });
    }

    private void setListeners() {

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

        binding.buttonAsesmen.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AssessmentActivity.class);
            startActivity(intent);
        });
        binding.firstHistory.setOnClickListener(v -> {
//            Intent intent;
//            intent = new Intent(getApplicationContext(), SearchResultActivity.class);
//
//            intent.putExtra("historyId", historyIds[0]);
//            startActivity(intent);
            showResult(historyIds[0]);
        });
        binding.secondHistory.setOnClickListener(v -> {
            showResult(historyIds[1]);
        });
        binding.thirdHistory.setOnClickListener(v -> {
            showResult(historyIds[2]);
        });
        binding.fourthHistory.setOnClickListener(v -> {
            showResult(historyIds[3]);
        });
        binding.fifthHistory.setOnClickListener(v -> {
            showResult(historyIds[4]);
        });
        binding.sixthHistory.setOnClickListener(v -> {
            showResult(historyIds[5]);
        });
        binding.seventhHistory.setOnClickListener(v -> {
            showResult(historyIds[6]);
        });
        binding.eighthHistory.setOnClickListener(v -> {
            showResult(historyIds[7]);
        });
    }


    private void setHistory() {
        database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
                .whereEqualTo(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null && task.isSuccessful()) {
                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                            DocumentSnapshot documentSnapshot;
                            documentSnapshot = task.getResult().getDocuments().get(i);
                            historyIds[i] = documentSnapshot.getId();
                            nameView = findViewById(historyNames[i]);
                            dateView = findViewById(historyDates[i]);
                            nameView.setText(documentSnapshot.getString(Constants.KEY_NAMA_ANAK));
                            dateView.setText(documentSnapshot.getString(Constants.KEY_USIA_ANAK) + " tahun");

                            CardView history = findViewById(historyTextViewIds[i]);
                            history.setVisibility(View.VISIBLE);
                        }

                        if (task.getResult().getDocuments().size() > 0) {
                            binding.allHistory.setVisibility(View.VISIBLE);
                        } else {
                            binding.noHistory.setVisibility(View.VISIBLE);
                        }


                    }
//                    else {
//                        Toast.makeText(getApplicationContext(),"In",Toast.LENGTH_SHORT).show();
//                        binding.noHistory.setVisibility(View.VISIBLE);
//                    }
                });
    }
}