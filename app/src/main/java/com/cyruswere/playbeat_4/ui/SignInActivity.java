package com.cyruswere.playbeat_4.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cyruswere.playbeat_4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private String mName;
    @BindView(R.id.firebaseProgressBar) ProgressBar mSignInProgressBar;
    @BindView(R.id.loadingTextView) TextView mLoadingSignUp;
    @BindView(R.id.goToSeeMusciFRomRegister) Button seeMusic;
    @BindView(R.id.emailInputRegisterView) EditText mRegisterUserEmail;
    @BindView(R.id.passwordInputRegisterView) EditText mRegisterUserPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //BindViews
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        seeMusic.setOnClickListener(view ->{
            createUser();
        });

    }
    private void showProgressBar() {
        mSignInProgressBar.setVisibility(View.VISIBLE);
        mLoadingSignUp.setVisibility(View.VISIBLE);
        mLoadingSignUp.setText("Sign Up process in Progress");
    }

    private void hideProgressBar() {
        mSignInProgressBar.setVisibility(View.GONE);
        mLoadingSignUp.setVisibility(View.GONE);
    }

    private void createUser(){
        String email = mRegisterUserEmail.getText().toString();
        String password = mRegisterUserPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            mRegisterUserEmail.setError("Email is required");
            mRegisterUserEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            mRegisterUserPassword.setError("Password is required");
            mRegisterUserPassword.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull  Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignInActivity.this,"Registration successful ",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignInActivity.this,LogInActivity.class));
                    }else{
                        Toast.makeText(SignInActivity.this,"Error occurred : " + task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mRegisterUserEmail.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            mRegisterUserEmail.setError("Please enter your Email");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mRegisterUserPassword.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mRegisterUserPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

}