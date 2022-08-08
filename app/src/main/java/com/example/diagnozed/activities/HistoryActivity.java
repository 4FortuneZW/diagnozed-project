package com.example.diagnozed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivityHistoryBinding;
import com.example.diagnozed.utilities.Constants;
import com.example.diagnozed.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private PreferenceManager preferenceManager;
    private int[] historyTextViewIds = {R.id.firstHistory,R.id.secondHistory,R.id.thirdHistory,R.id.fourthHistory,R.id.fifthHistory
    ,R.id.sixthHistory,R.id.seventhHistory,R.id.eighthHistory};
    private int[] historyNames = {R.id.firstHistoryName,R.id.secondHistoryName,R.id.thirdHistoryName
            ,R.id.fourthHistoryName,R.id.fifthHistoryName,R.id.sixthHistoryName,R.id.seventhHistoryName,R.id.eighthHistoryName};
    private int[] historyDates = {R.id.firstHistoryDate,R.id.secondHistoryDate,R.id.thirdHistoryDate,R.id.fourthHistoryDate
            ,R.id.fifthHistoryDate,R.id.sixthHistoryDate,R.id.seventhHistoryDate,R.id.eighthHistoryDate};
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

        String[] statements =
                {getResources().getString(R.string.state_no_1), getResources().getString(R.string.state_no_2),
                        getResources().getString(R.string.state_no_3), getResources().getString(R.string.state_no_4),
                        getResources().getString(R.string.state_no_5), getResources().getString(R.string.state_no_6),
                        getResources().getString(R.string.state_no_7), getResources().getString(R.string.state_no_8),
                        getResources().getString(R.string.state_no_9), getResources().getString(R.string.state_no_10)};

        database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        binding.namaAnak.setText("Nama anak : " + documentSnapshot.getString(Constants.KEY_NAMA_ANAK));
                        binding.usiaAnak.setText("Usia anak : " + documentSnapshot.getString(Constants.KEY_USIA_ANAK));
                        binding.autismResultInfo.setText(String.format("Hasil diagnosa autis : %d/5"
                                , documentSnapshot.getLong(Constants.KEY_AUTISM_RESULT))
                        );
                        binding.speechDelayResultInfo.setText(String.format("Hasil diagnosa speech delay : %d/5"
                                , documentSnapshot.getLong(Constants.KEY_SPEECH_DELAY_RESULT))
                        );
                        binding.q1Info.setText(statements[0] + " : " + documentSnapshot.getString(statements[0]));
                        binding.q2Info.setText(statements[1] + " : " + documentSnapshot.getString(statements[1]));
                        binding.q3Info.setText(statements[2] + " : " + documentSnapshot.getString(statements[2]));
                        binding.q4Info.setText(statements[3] + " : " + documentSnapshot.getString(statements[3]));
                        binding.q5Info.setText(statements[4] + " : " + documentSnapshot.getString(statements[4]));
                        binding.q6Info.setText(statements[5] + " : " + documentSnapshot.getString(statements[5]));
                        binding.q7Info.setText(statements[6] + " : " + documentSnapshot.getString(statements[6]));
                        binding.q8Info.setText(statements[7] + " : " + documentSnapshot.getString(statements[7]));
                        binding.q9Info.setText(statements[8] + " : " + documentSnapshot.getString(statements[8]));
                        binding.q10Info.setText(statements[9] + " : " + documentSnapshot.getString(statements[9]));

                        binding.results.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void setListeners() {
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

                        binding.allHistory.setVisibility(View.VISIBLE);

                    } else {
                        binding.noHistory.setVisibility(View.VISIBLE);
                    }
                });
    }
}