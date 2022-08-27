package com.example.diagnozed.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.stream.Collectors;

public class AssessmentActivity extends AppCompatActivity {

    private ActivityAssessmentBinding binding;
    private PreferenceManager preferenceManager;
    private Integer usiaAnak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssessmentBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());

        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {

        binding.autismAssessmentButton.setOnClickListener(v -> {
            binding.autismAssessmentButton.setBackgroundColor(getColor(R.color.secondary_text));
            binding.speechDelayAssessmentButton.setBackgroundColor(getColor(R.color.primary));
            binding.asesmenAutis.setVisibility(View.VISIBLE);
            binding.asesmenSpeechDelay.setVisibility(View.GONE);
        });

        binding.speechDelayAssessmentButton.setOnClickListener(v -> {
            if(binding.inputUsiaAnak.getText().toString().equals("")) {
                showToast("Masukkan usia anak terlebih dahulu");
            } else {

                usiaAnak = Integer.valueOf(binding.inputUsiaAnak.getText().toString());
                switch (usiaAnak) {
                    case 1 :
                        binding.satuTahun.setVisibility(View.VISIBLE);
                        break;
                    case 2 :
                        binding.duaTahun.setVisibility(View.VISIBLE);
                        break;
                    case 3 :
                        binding.tigaTahun.setVisibility(View.VISIBLE);
                        break;
                    case 4 :
                        binding.empatTahun.setVisibility(View.VISIBLE);
                        break;
                    case 5 :
                        binding.limaTahun.setVisibility(View.VISIBLE);
                        break;
                    case 6 :
                        binding.enamTahun.setVisibility(View.VISIBLE);
                        break;
                    case 7 :
                        showToast("Tes ini hanya bisa dilakukan oleh anak maksimal 6 tahun");
                        break;
                }

                binding.speechDelayAssessmentButton.setBackgroundColor(getColor(R.color.secondary_text));
                binding.autismAssessmentButton.setBackgroundColor(getColor(R.color.primary));
                binding.asesmenSpeechDelay.setVisibility(View.VISIBLE);
                binding.asesmenAutis.setVisibility(View.GONE);
            }
        });

        binding.backButton.setOnClickListener(v -> onBackPressed());

        binding.petunjukButton.setOnClickListener(v -> {
            binding.petunjuk.setVisibility(View.VISIBLE);
        });

        binding.tutupPetunjukButton.setOnClickListener(v -> {
            binding.petunjuk.setVisibility(View.GONE);
        });

        binding.assessmentSubmitButton.setOnClickListener(v -> {
            if (!binding.inputNamaAnak.getText().toString().equals("") && !binding.inputUsiaAnak.getText().toString().equals("")) {

                loading(true);

                usiaAnak = Integer.valueOf(binding.inputUsiaAnak.getText().toString());

                HashMap<String, Object> assessmentResult = new HashMap<>();
                CheckBox checkBox;

                int autismCheckBoxesId[] = {R.id.autis_no_1,R.id.autis_no_2,R.id.autis_no_3,R.id.autis_no_4,
                        R.id.autis_no_5,R.id.autis_no_6,R.id.autis_no_7,R.id.autis_no_8,
                        R.id.autis_no_9,R.id.autis_no_10,R.id.autis_no_11,R.id.autis_no_12,
                        R.id.autis_no_13,R.id.autis_no_14,R.id.autis_no_15,R.id.autis_no_16,
                        R.id.autis_no_17,R.id.autis_no_18,R.id.autis_no_19,R.id.autis_no_20};

                List<Integer> autismCheckedboxesId;
                autismCheckedboxesId = new ArrayList<Integer>();

//                String[] autismStateIds = {getResources().getString(R.string.autis_no_1), getResources().getString(R.string.autis_no_2),
//                        getResources().getString(R.string.autis_no_3), getResources().getString(R.string.autis_no_4),
//                        getResources().getString(R.string.autis_no_5), getResources().getString(R.string.autis_no_6),
//                        getResources().getString(R.string.autis_no_7), getResources().getString(R.string.autis_no_8),
//                        getResources().getString(R.string.autis_no_9), getResources().getString(R.string.autis_no_10),
//                        getResources().getString(R.string.autis_no_11), getResources().getString(R.string.autis_no_12),
//                        getResources().getString(R.string.autis_no_13), getResources().getString(R.string.autis_no_14),
//                        getResources().getString(R.string.autis_no_15), getResources().getString(R.string.autis_no_16),
//                        getResources().getString(R.string.autis_no_17), getResources().getString(R.string.autis_no_18),
//                        getResources().getString(R.string.autis_no_19), getResources().getString(R.string.autis_no_20)};

                String namaAnakRaw = binding.inputNamaAnak.getText().toString().trim();
                String namaAnak = namaAnakRaw.toLowerCase().replaceAll("\\s", "");

                assessmentResult.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
                assessmentResult.put(Constants.KEY_NAMA_ANAK, namaAnak);
                assessmentResult.put(Constants.KEY_USIA_ANAK, binding.inputUsiaAnak.getText().toString().trim());

                int autismScore = 0;
                int checkedBoxesIter = 0;
                int stateIter = 0;
                for (int checkBoxId : autismCheckBoxesId) {
                    checkBox = findViewById(checkBoxId);

                    if (checkBox.isChecked()) {
                        autismCheckedboxesId.add(autismCheckBoxesId[stateIter]);
                        checkedBoxesIter++;
                    }

                    if ((stateIter == 1 || stateIter == 4 || stateIter == 11) && checkBox.isChecked()) {
                        autismScore++;
                    }

                    if (stateIter != 1 && stateIter != 4 && stateIter != 11 && !checkBox.isChecked()) {
                        autismScore++;
                    }

                    stateIter++;
                }

                String autismResult;
                if (autismScore >= 8) {
                    autismResult = "Risiko tinggi";
                    assessmentResult.put("keteranganAutis", getResources().getString(R.string.risiko_tinggi));
                } else if (autismScore >= 3) {
                    autismResult = "Risiko sedang";
                    assessmentResult.put("keteranganAutis", getResources().getString(R.string.risiko_sedang));
                } else {
                    autismResult = "Risiko rendah";
                    assessmentResult.put("keteranganAutis", getResources().getString(R.string.risiko_rendah));
                }

                preferenceManager.putString(Constants.KEY_AUTISM_RESULT, autismResult);
                assessmentResult.put(Constants.KEY_AUTISM_RESULT, autismResult);

                int spedaCheckBoxesId[] = {R.id.speda1_no_1,R.id.speda1_no_2,R.id.speda1_no_3,R.id.speda2_no_1,
                        R.id.speda2_no_2,R.id.speda2_no_3,R.id.speda2_no_4,R.id.speda2_no_5,
                        R.id.speda3_no_1,R.id.speda3_no_2,R.id.speda3_no_3,R.id.speda3_no_4,
                        R.id.speda3_no_5,R.id.speda4_no_1,R.id.speda4_no_2,R.id.speda4_no_3,
                        R.id.speda4_no_4,R.id.speda4_no_5,R.id.speda4_no_6,R.id.speda4_no_7,
                        R.id.speda4_no_8,R.id.speda5_no_1,R.id.speda5_no_2,R.id.speda5_no_3,
                        R.id.speda5_no_4,R.id.speda5_no_5,R.id.speda5_no_6,R.id.speda6_no_1,
                        R.id.speda6_no_2,R.id.speda6_no_3
                };

                List<Integer> spedaCheckedboxesId;
                spedaCheckedboxesId = new ArrayList<>();

                stateIter = 0;
                for (int checkBoxId : spedaCheckBoxesId) {
                    checkBox = findViewById(checkBoxId);

                    if (checkBox.isChecked()) {
                        spedaCheckedboxesId.add(spedaCheckBoxesId[stateIter]);
                    }

                    stateIter++;
                }

                int cautionScore = 0;
                int delayScore = 0;

                switch (usiaAnak) {
                    case 1 :

                        if(!binding.speda1No1.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda1No2.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda1No3.isChecked()) {
                            cautionScore++;
                        }

                        break;

                    case 2 :

                        if(!binding.speda2No1.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda2No2.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda2No3.isChecked()) {
                            cautionScore++;
                        }
                        if(!binding.speda2No4.isChecked()) {
                            cautionScore++;
                        }
                        if(!binding.speda2No5.isChecked()) {
                            cautionScore++;
                        }

                        break;

                    case 3 :

                        if(!binding.speda3No1.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda3No2.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda3No3.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda3No4.isChecked()) {
                            cautionScore++;
                        }
                        if(!binding.speda3No5.isChecked()) {
                            cautionScore++;
                        }

                        break;

                    case 4 :

                        if(!binding.speda4No1.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda4No2.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda4No3.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda4No4.isChecked()) {
                            cautionScore++;
                        }
                        if(!binding.speda4No5.isChecked()) {
                            cautionScore++;
                        }
                        if(!binding.speda4No6.isChecked()) {
                            cautionScore++;
                        }
                        if(!binding.speda4No7.isChecked()) {
                            cautionScore++;
                        }
                        if(!binding.speda4No8.isChecked()) {
                            cautionScore++;
                        }

                        break;

                    case 5 :

                        if(!binding.speda5No1.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda5No2.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda5No3.isChecked()) {
                            cautionScore++;
                        }
                        if(!binding.speda5No4.isChecked()) {
                            cautionScore++;
                        }
                        if(!binding.speda5No5.isChecked()) {
                            cautionScore++;
                        }
                        if(!binding.speda5No6.isChecked()) {
                            cautionScore++;
                        }

                        break;

                    case 6 :

                        if(!binding.speda6No1.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda6No2.isChecked()) {
                            delayScore++;
                        }
                        if(!binding.speda6No3.isChecked()) {
                            delayScore++;
                        }

                        break;

                    case 7 :
                        showToast("Tes ini hanya bisa dilakukan oleh anak maksimal 6 tahun");
                        break;
                }

                String speechDelayResult;

                if (delayScore >= 1 || cautionScore >= 2) {
                    speechDelayResult = "Suspect";
                    assessmentResult.put("keteranganSpeda", getResources().getString(R.string.suspect));
                } else {
                    speechDelayResult = "Normal";
                    assessmentResult.put("keteranganSpeda", getResources().getString(R.string.normal));
                }

                preferenceManager.putString(Constants.KEY_SPEECH_DELAY_RESULT, speechDelayResult);
                assessmentResult.put(Constants.KEY_SPEECH_DELAY_RESULT, speechDelayResult);
                assessmentResult.put(Constants.KEY_AUTISM_CHECKEDBOXES, autismCheckedboxesId);
                assessmentResult.put(Constants.KEY_SPEDA_CHECKEDBOXES, spedaCheckedboxesId);

                FirebaseFirestore database = FirebaseFirestore.getInstance();

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

                loading(false);
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

//    private boolean isValidAnswers() {
//        boolean isNotValidAnswers;
//        isNotValidAnswers = binding.stateNo1.isChecked() && binding.stateNo2.isChecked()
//                && binding.stateNo3.isChecked() && binding.stateNo4.isChecked()
//                && binding.stateNo5.isChecked() && binding.stateNo6.isChecked()
//                && binding.stateNo7.isChecked() && binding.stateNo8.isChecked()
//                && binding.stateNo9.isChecked() && binding.stateNo10.isChecked()
//                && binding.stateNo11.isChecked() && binding.stateNo12.isChecked()
//                && binding.stateNo13.isChecked() && binding.stateNo14.isChecked()
//                && binding.stateNo15.isChecked() && binding.stateNo16.isChecked()
//                && binding.stateNo17.isChecked() && binding.stateNo18.isChecked()
//                && binding.stateNo19.isChecked() && binding.stateNo20.isChecked();
//
//        if (isNotValidAnswers) {
//            showToast("Pastikan semua jawaban terisi");
//            return false;
//        }
//        return true;
//    }


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