package com.example.residentapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.residentapplication.databinding.ActivityVisitorRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.residentapplication.databinding.ActivityRegisterBinding;


public class UserRegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    String name,unitNumber,phoneNumber,email,password;

    ProgressBar progressBar;

    FirebaseAuth fAuth;

    String userID;

    EditText Email;

    EditText Password;
    FirebaseDatabase db;
    DatabaseReference ref;



    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);


        fAuth = FirebaseAuth.getInstance();



        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.agree_register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = binding.name.getText().toString();
                unitNumber = binding.unitNumber.getText().toString();
                phoneNumber = binding.phoneNumber.getText().toString();
                email = binding.email.getText().toString();
                password = binding.password.getText().toString();

                // Regular expressions for email, phone number, and unit number validation
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String phonePattern = "\\d{10,11}";
                String unitNumberPattern = "\\d+";

                // Regular expression for password complexity
                String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

                // Validate email
                if (!email.matches(emailPattern)) {
                    binding.email.setError("Invalid email address");
                    return;
                }
                // Validate password complexity
                else if (!password.matches(passwordPattern)) {
                    binding.password.setError("Password must contain at least 8 characters including a-z,A-Z,@!*,0-9");
                    return;
                }
                // Validate unit number
                else if (!unitNumber.matches(unitNumberPattern)) {
                    binding.unitNumber.setError("Unit number must contain only numbers");
                    return;
                }
                // Validate phone number
                else if (!phoneNumber.matches(phonePattern)) {
                    binding.phoneNumber.setError("Invalid phone number");
                    return;
                }

                // If all validations pass, proceed to register the user
                registerUser();
            }
        });


    }

    private void registerUser() {
        final String email = binding.email.getText().toString();
        final String password = binding.password.getText().toString();

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(UserRegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Registration successful, save user data to database
                    String user_Id = fAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Residents").child(user_Id);

                    // Create a new Resident object with user data
                    Resident resident = new Resident(name, email, password, unitNumber, phoneNumber);

                    // Save the Resident object to the database
                    current_user_db.setValue(resident).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Data saved successfully
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(UserRegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), UserLoginActivity.class));
                            } else {
                                // Failed to save data
                                Toast.makeText(UserRegisterActivity.this, "Failed to save user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Registration failed
                    Toast.makeText(UserRegisterActivity.this, "Failed to register: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}