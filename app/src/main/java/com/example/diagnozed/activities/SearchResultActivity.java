package com.example.diagnozed.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivitySearchResultBinding;
import com.example.diagnozed.utilities.Constants;
import com.example.diagnozed.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SearchResultActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private ActivitySearchResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.searchVector.setOnClickListener(v -> {
            searchResult();
        });
    }

    private void searchResult() {
        String[] statements = {getResources().getString(R.string.state_no_1), getResources().getString(R.string.state_no_2),
                getResources().getString(R.string.state_no_3), getResources().getString(R.string.state_no_4),
                getResources().getString(R.string.state_no_5), getResources().getString(R.string.state_no_6),
                getResources().getString(R.string.state_no_7), getResources().getString(R.string.state_no_8),
                getResources().getString(R.string.state_no_9), getResources().getString(R.string.state_no_10)};

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_ASSESSMENTS)
                .document(binding.searchResult.getText().toString().trim())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        binding.namaAnak.setText("Nama anak : " + documentSnapshot.getString(Constants.KEY_NAMA_ANAK));
                        binding.usiaAnak.setText("Usia anak : " + documentSnapshot.getString(Constants.KEY_USIA_ANAK));
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

                        binding.namaAnak.setVisibility(View.VISIBLE);
                        binding.usiaAnak.setVisibility(View.VISIBLE);
                        binding.q1Info.setVisibility(View.VISIBLE);
                        binding.q2Info.setVisibility(View.VISIBLE);
                        binding.q3Info.setVisibility(View.VISIBLE);
                        binding.q4Info.setVisibility(View.VISIBLE);
                        binding.q5Info.setVisibility(View.VISIBLE);
                        binding.q6Info.setVisibility(View.VISIBLE);
                        binding.q7Info.setVisibility(View.VISIBLE);
                        binding.q8Info.setVisibility(View.VISIBLE);
                        binding.q9Info.setVisibility(View.VISIBLE);
                        binding.q1Info.setVisibility(View.VISIBLE);



                    };
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), "Id salah", Toast.LENGTH_SHORT).show();
                });
    }
}