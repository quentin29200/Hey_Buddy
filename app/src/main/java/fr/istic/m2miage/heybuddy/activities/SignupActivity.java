package fr.istic.m2miage.heybuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.istic.m2miage.heybuddy.R;
import fr.istic.m2miage.heybuddy.firebase.FirebaseUtil;
import fr.istic.m2miage.heybuddy.firebase.User;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = SignupActivity.class.getName();

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @BindView(R.id.signup_input_layout_email) TextInputLayout signupInputLayoutEmail;
    @BindView(R.id.signup_input_layout_password) TextInputLayout signupInputLayoutPassword;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.signup_input_password) EditText signupInputPassword;
    @BindView(R.id.signup_input_email) EditText signupInputEmail;
    @BindView(R.id.btn_link_login) Button btnLinkToLogin;
    @BindView(R.id.btn_signup) Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "authListener");
                FirebaseUser user = auth.getCurrentUser();
                if(user != null){
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
//                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @OnClick(R.id.btn_signup)
    public void submitForm() {
        final String email = signupInputEmail.getText().toString().trim();
        final String password = signupInputPassword.getText().toString().trim();

        /**
         * Vérification des champs
         */
        if(!checkEmail() || !checkPassword()) {
            return;
        }
        signupInputLayoutEmail.setErrorEnabled(false);
        signupInputLayoutPassword.setErrorEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, Log the message to the LogCat. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if(!task.isSuccessful()){
                            Log.d(SignupActivity.class.getName(), "Authentication failed: " + task.getException());
                        } else {
                            User user = new User(email, email);
                            FirebaseUtil.addUser(user);
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                            Toast.makeText(getApplicationContext(), "You are successfully Registered !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        progressBar.setVisibility(View.GONE);
    }

    private boolean checkUsername() {
//        String username = signupInputUsername.getText().toString().trim();
//        if(username.isEmpty() || !isUsernameValid(username)){
//            signupInputLayoutUsername.setErrorEnabled(true);
//            signupInputLayoutUsername.setError("Username non valide");
//            signupInputUsername.setError("Ce champ est requis");
//            requestFocus(signupInputUsername);
//            return false;
//        }
//        else if(isUsernameExisting(username)){
//            signupInputLayoutUsername.setErrorEnabled(true);
//            signupInputLayoutUsername.setError("Cet username existe déjà");
//            requestFocus(signupInputUsername);
//            return false;
//        }
//        signupInputLayoutUsername.setErrorEnabled(false);
        return true;
    }

    private boolean isUsernameValid(String username) {
        return username.length() >= 4;
    }

    private boolean checkEmail() {
        String email = signupInputEmail.getText().toString().trim();
        if (email.isEmpty() || !isEmailValid(email)) {
            signupInputLayoutEmail.setErrorEnabled(true);
            signupInputLayoutEmail.setError("email non valide");
            signupInputEmail.setError("Ce champ est requis");
            requestFocus(signupInputEmail);
            return false;
        }
        signupInputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkPassword() {
        String password = signupInputPassword.getText().toString().trim();
        if (password.isEmpty() || !isPasswordValid(password)) {

            signupInputLayoutPassword.setError("Mot de passe non valide");
            signupInputPassword.setError("Ce champ est requis");
            requestFocus(signupInputPassword);
            return false;
        }
        signupInputLayoutPassword.setErrorEnabled(false);
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authListener != null){
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
