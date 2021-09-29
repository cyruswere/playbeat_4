package com.cyruswere.playbeat_4.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cyruswere.playbeat_4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener  {
    FirebaseAuth mAuth;

    //Using BIndView from ButterKnife.
    @BindView(R.id.goToLoginScreen) Button login;
    @BindView(R.id.goToSavedSearch) Button savedSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();
        //BindViews
        ButterKnife.bind(this);

        //Implemented because of the onclick interface
        login.setOnClickListener(this);
        savedSearch.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        FirebaseUser user = mAuth.getCurrentUser();
        //Helps us navigate between the log in and the register button.
        if(v == login){
            if(user == null){
                startActivity(new Intent(WelcomeActivity.this, LogInActivity.class));
            }else{
                startActivity(new Intent(WelcomeActivity.this, SearchActivity.class));
            }
        }else if( v==savedSearch){
            if(user == null){
                startActivity(new Intent(WelcomeActivity.this, LogInActivity.class));
            }else{
                Intent intent = new Intent(WelcomeActivity.this,SavedResultListActivity.class);
                startActivity(intent);
            }
        }

    }
}