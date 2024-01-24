package com.example.mypages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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
            signInWithGoogle();
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

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            // Google Sign In failed
            // Handle failure
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        String idToken = account.getIdToken();
        firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                        // sucess
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}