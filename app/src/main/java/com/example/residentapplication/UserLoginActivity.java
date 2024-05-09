package com.example.residentapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserLoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private Button RegisterButton;
    private EditText EmailLogin;
    private EditText PasswordLogin;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        LoginButton = (Button) findViewById(R.id.login_btn);
        RegisterButton = (Button) findViewById(R.id.register_btn);
        EmailLogin = (EditText) findViewById(R.id.email_user);
        PasswordLogin= (EditText) findViewById(R.id.password_user);
        fAuth = FirebaseAuth.getInstance();


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(UserLoginActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterActivityIntent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(RegisterActivityIntent);
            }
        });



        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = EmailLogin.getText().toString();
                String password = PasswordLogin.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    EmailLogin.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    PasswordLogin.setError("Password is required");
                    return;
                }


                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UserLoginActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeScreenActivity.class));
                        }else{
                            Toast.makeText(UserLoginActivity.this, "Wrong credentials/No such user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    @Override
    protected void onStart(){
        super.onStart();
        fAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        fAuth.removeAuthStateListener(firebaseAuthListener);
    }


}