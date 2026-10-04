package com.example.symptalk;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    private EditText emailText, passwordText;
    private Button signInButton;
    private ProgressBar signupProgress;
    private FirebaseAuth Auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        Auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Optional: Enable Firestore offline persistence
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);

        // Initialize views
        emailText = findViewById(R.id.emailEditText);
        passwordText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        signupProgress = findViewById(R.id.progressbar);

        signInButton.setOnClickListener(view -> registerNewUser());
    }

    private void registerNewUser() {
        signupProgress.setVisibility(View.VISIBLE);

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            signupProgress.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            signupProgress.setVisibility(View.GONE);
            return;
        }

        Log.d("SIGNUP", "Creating user with: " + email);
        Auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        signupProgress.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = Auth.getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("email", email);
                                userMap.put("uid", userId);
                                userMap.put("created_at", System.currentTimeMillis());

                                Log.d("SIGNUP", "Saving user to Firestore...");
                                firestore.collection("signup").document(userId)
                                        .set(userMap)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                            Log.d("SIGNUP", "User saved successfully");
                                            startActivity(new Intent(signup.this, Loadingpage.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getApplicationContext(), "Firestore error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("SIGNUP", "Firestore write failed", e);
                                        });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed Registration: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("SIGNUP", "Firebase auth failed", task.getException());
                        }
                    }
                });
    }
}
