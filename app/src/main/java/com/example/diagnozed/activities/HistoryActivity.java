package com.example.diagnozed.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivityHistoryBinding;
import com.example.diagnozed.utilities.Constants;
import com.example.diagnozed.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private PreferenceManager preferenceManager;
    private int[] historyTextViewIds = {R.id.firstHistory,R.id.secondHistory,R.id.thirdHistory,R.id.fourthHistory,R.id.fifthHistory};
    private String[] historyIds;
    private TextView textView;
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

    private void setListeners() {
        binding.firstHistory.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(getApplicationContext(), SearchResultActivity.class);

            intent.putExtra("historyId", historyIds[0]);
            startActivity(intent);
        });
        binding.secondHistory.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(getApplicationContext(), SearchResultActivity.class);

            intent.putExtra("historyId", historyIds[1]);
            startActivity(intent);
        });
        binding.thirdHistory.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(getApplicationContext(), SearchResultActivity.class);

            intent.putExtra("historyId", historyIds[2]);
            startActivity(intent);
        });
        binding.fourthHistory.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(getApplicationContext(), SearchResultActivity.class);

            intent.putExtra("historyId", historyIds[3]);
            startActivity(intent);
        });
        binding.fifthHistory.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(getApplicationContext(), SearchResultActivity.class);

            intent.putExtra("historyId", historyIds[4]);
            startActivity(intent);
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
                            textView = findViewById(historyTextViewIds[i]);
                            textView.setText(String.format("Nama : %1$s\nUmur : %2$s\nHasil diagnosa speech delay : %3$d/5\n" +
                                    "Hasil diagnosa autis : %4$d/5\nBaca selengkapnya...",
                                            documentSnapshot.getString(Constants.KEY_NAMA_ANAK),
                                            documentSnapshot.getString(Constants.KEY_USIA_ANAK),
                                            documentSnapshot.getLong(Constants.KEY_SPEECH_DELAY_RESULT),
                                            documentSnapshot.getLong(Constants.KEY_AUTISM_RESULT))
                                    );
                            textView = findViewById(historyTextViewIds[i]);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}