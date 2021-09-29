package com.cyruswere.playbeat_4.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cyruswere.playbeat_4.Constants;
import com.cyruswere.playbeat_4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity  implements  View.OnClickListener{
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private DatabaseReference mSearchedKeyWordReference;
    private ValueEventListener mSearchedKeyWordReferenceListener;


    //Using BIndView from ButterKnife.
    @BindView(R.id.goToResultsFromSearch) Button searchButton;
    @BindView(R.id.keyWordInputSearchView) EditText searchKeyWOrd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSearchedKeyWordReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_KEY_WORD);

        mSearchedKeyWordReferenceListener = mSearchedKeyWordReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot searchKeyWordSnapshot : dataSnapshot.getChildren()) {
                    String searchKeyWord = searchKeyWordSnapshot.getValue().toString();
                    Log.d("Search Key word ", "Search Key Word: " + searchKeyWord);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //BindViews
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();



        //Implemented because of the onclick interface
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == searchButton){
            if( searchKeyWOrd.getText().toString().length() == 0 ){
                searchKeyWOrd.setError( "Please Enter a search key word to proceed" );
                searchKeyWOrd.requestFocus();
            }else {
                String TypedSearchKeyWOrd = searchKeyWOrd.getText().toString();
                Intent intent = new Intent(SearchActivity.this, ResultsActivity.class);
                intent.putExtra("TypedSearchKeyWOrd", TypedSearchKeyWOrd);
                saveKeyWordToFirebase(TypedSearchKeyWOrd);
                if(!(TypedSearchKeyWOrd).equals("")) {
                    addToSharedPreferences(TypedSearchKeyWOrd);
                }
                startActivity(intent);
            }
        }
    }
    public void saveKeyWordToFirebase(String SearchKeyWOrd) {
        mSearchedKeyWordReference.push().setValue(SearchKeyWOrd);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchedKeyWordReference.removeEventListener(mSearchedKeyWordReferenceListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SearchActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void addToSharedPreferences(String searchKeyWord) {
        mEditor.putString(Constants.PREFERENCES_RESULT_KEY, searchKeyWord).apply();
    }


}