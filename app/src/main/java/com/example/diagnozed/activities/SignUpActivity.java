package com.example.diagnozed.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.diagnozed.R;
import com.example.diagnozed.databinding.ActivitySignUpBinding;
import com.example.diagnozed.utilities.Constants;
import com.example.diagnozed.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {

    public static final int STORAGE_PERMISSION_CODE = 113;
    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;
    private String role;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private void auth(String email, String password, String docId) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext()
                                                        ,"Periksa email anda untuk melakukan verifikasi akun"
                                                        , Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                                FirebaseFirestore database;
                                                database = FirebaseFirestore.getInstance();
                                                database.collection(Constants.KEY_COLLECTION_USERS)
                                                        .document(docId)
                                                        .delete();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Email tidak valid",
                                                    Toast.LENGTH_LONG).show();
                                            FirebaseFirestore database;
                                            database = FirebaseFirestore.getInstance();
                                            database.collection(Constants.KEY_COLLECTION_USERS)
                                                    .document(docId)
                                                    .delete();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setListeners() {
        binding.backToLogin.setOnClickListener(v ->
                onBackPressed());
        binding.buttonSignUpUser.setOnClickListener(v -> {
            role = "user";
            if (isValidSignUpDetails()) {
                signUp(false);
            }
        });
        binding.buttonSignUpDoctor.setOnClickListener(v -> {
            role = "doctor";
            if (isValidSignUpDetails()) {
                signUp(true);
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signUp(Boolean isDoctor) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        if (!isDoctor) {
            loading(true, false);
            HashMap<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_NAME, binding.inputName.getText().toString());
            user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
            user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
            user.put(Constants.KEY_IMAGE, encodedImage);
            user.put(Constants.KEY_ROLE, Constants.KEY_VALUE_USER);
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        loading(false, isDoctor);
                        auth(binding.inputEmail.getText().toString(),binding.inputPassword.getText().toString(),
                                documentReference.getId());
                    })
                    .addOnFailureListener(exception -> {
                        loading(false, isDoctor);
                        showToast(exception.getMessage());
                    });
        } else {
            loading(true, true);
            HashMap<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_NAME, binding.inputName.getText().toString());
            user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
            user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
            user.put(Constants.KEY_IMAGE, encodedImage);
            user.put(Constants.KEY_ROLE, Constants.KEY_VALUE_DOCTOR);
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        loading(false, isDoctor);
                        auth(binding.inputEmail.getText().toString(),binding.inputPassword.getText().toString(),
                                documentReference.getId());

                    })
                    .addOnFailureListener(exception -> {
                        loading(false, isDoctor);
                        showToast(exception.getMessage());
                    });
        }
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.teksTambahGambar.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails() {
//        if (encodedImage == null) {
//            showToast("Pilih foto profil Anda");
//            return false;
//        }

        if (encodedImage == null) {
            Drawable drawable;

            if (role == "doctor") {
                drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_profile_doctor);
            } else {
                drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_profile_user);
            }

            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            encodedImage = encodeImage(bitmap);
        }

        if (binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("Masukkan nama Anda");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Masukkan email Anda");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Masukkan email yang valid");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Masukkan password");
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Konfirmasi password Anda");
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Password harus sama");
            return false;
        }

        return true;
    }

    private void loading(Boolean isLoading, Boolean isDoctor) {
        if (isLoading && !isDoctor) {
            binding.buttonSignUpUser.setVisibility(View.INVISIBLE);
            binding.progressBarUser.setVisibility(View.VISIBLE);
        } else if (isLoading) {
            binding.buttonSignUpDoctor.setVisibility(View.INVISIBLE);
            binding.progressBarDoctor.setVisibility(View.VISIBLE);
        } else {
            binding.buttonSignUpUser.setVisibility(View.VISIBLE);
            binding.progressBarUser.setVisibility(View.INVISIBLE);
            binding.buttonSignUpDoctor.setVisibility(View.VISIBLE);
            binding.progressBarDoctor.setVisibility(View.INVISIBLE);
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {permission}, requestCode);
        } else {
            showToast("Permission is already granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Storage permission is granted");
            } else {
                showToast("Storage permission is denied");
            }
        }
    }
}
