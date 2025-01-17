package com.example.diagnozed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivityMainBinding;
import com.example.diagnozed.utilities.Constants;
import com.example.diagnozed.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.ref.PhantomReference;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_PERMISSION_CODE = 112;
    public static final int STORAGE_PERMISSION_CODE = 113;
    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

        if (preferenceManager.getString(Constants.KEY_ROLE).equals("doctor")) {
            binding.buttonCariPasien.setVisibility(View.VISIBLE);
            binding.riwayat.setVisibility(View.INVISIBLE);
        }
        loadUserDetails();
//        getToken();

        setListeners();
    }

    private void setListeners() {
        binding.buttonKonsul.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
            startActivity(intent);
        });
        binding.buttonAsesmen.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AssessmentActivity.class);
            startActivity(intent);
        });
        binding.buttonCariPasien.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
            startActivity(intent);
        });
        binding.signOutButton.setOnClickListener(v -> signOut());
        binding.translateButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), TerjemahActivity.class)));
        binding.riwayat.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), HistoryActivity.class)));
    }

    private void loadUserDetails() {
        binding.nameText.setText("Selamat datang,\n" + preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

//    private void getToken() {
//        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
//    }

//    private void updateToken(String token){
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference documentReference =
//                database.collection(Constants.KEY_COLLECTION_USERS)
//                        .document(preferenceManager.getString(Constants.KEY_USER_ID));
//        documentReference.update(Constants.KEY_FCM_TOKEN, token)
//                .addOnSuccessListener(unused -> showToast("Token update successfully"))
//                .addOnFailureListener(e -> showToast("Unable to update token"));
//
//    }

    private void signOut() {
        showToast("Signing Out");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .document(preferenceManager.getString(Constants.KEY_USER_ID));

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->showToast("Unable to sign out"));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void checkPermission(String permission, int requestCode) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Camera permission is granted");
            } else {
                showToast("Camera permission is denied");
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Storage permission is granted");
            } else {
                showToast("Storage permission is denied");
            }
        }
    }
}