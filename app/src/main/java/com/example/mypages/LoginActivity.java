package com.example.mypages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    ImageView googleIcon, facebookIcon;
    Button createAccount, loginButton;
    TextView forgotPassword, passwordError;
    EditText userEmail, userPassword;
    CardView googleButton, facebookButton;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (user != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        googleIcon = findViewById(R.id.login_googleIcon);
        facebookIcon = findViewById(R.id.login_facebookIcon);
        facebookIcon.setImageResource(R.drawable.facebook_icon);
        googleIcon.setImageResource(R.drawable.google_icon);

        createAccount = findViewById(R.id.login_createNewUser);
        loginButton = findViewById(R.id.login_loginButton);
        forgotPassword = findViewById(R.id.login_forgotPassword);
        passwordError = findViewById(R.id.login_textViewError);
        userEmail = findViewById(R.id.login_userEmailInput);
        userPassword = findViewById(R.id.login_userpasswordInput);
        facebookButton = findViewById(R.id.login_facebookButton);
        googleButton = findViewById(R.id.login_googleButton);
        
        createAccount.setOnClickListener(view -> {
            String userMail = userEmail.getText().toString();
            String password = userPassword.getText().toString();
            
            if (password.equals("") || userMail.equals("")) {
                Toast.makeText(this, "Please enter username and password for the new account to be created", Toast.LENGTH_LONG).show();
            } else if (password.length() < 6) {
                Toast.makeText(this, "Too short password", Toast.LENGTH_SHORT).show();
            } else {
                signUpFirebase(userMail, password);
            }
        });
        

        loginButton.setOnClickListener(view -> {
            String userMail, password;
            userMail = userEmail.getText().toString();
            password = userPassword.getText().toString();
            
            if (password.equals("") || userMail.equals("")) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_LONG).show();
            } else if (password.length() < 6) {
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
            } else {
                signInFirebase(userMail, password);
            }
        });


        forgotPassword.setOnClickListener(view -> {
            String mail = userEmail.getText().toString();
            if (!mail.equals("")) {
                auth.sendPasswordResetEmail(mail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "We sent an email to you", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Unable to change password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Please enter your email first", Toast.LENGTH_SHORT).show();
            }
        });


        googleButton.setOnClickListener(view -> {

        });
    }


    public void signUpFirebase(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Your account has bee created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Unable to create account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void signInFirebase(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}