package com.cyruswere.playbeat_4.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.cyruswere.playbeat_4.Constants;
import com.cyruswere.playbeat_4.R;
import com.cyruswere.playbeat_4.adapter.FirebaseResultListAdapter;
import com.cyruswere.playbeat_4.adapter.TrackListAdapter;
import com.cyruswere.playbeat_4.models.Result;
import com.cyruswere.playbeat_4.models.TrackResponse;
import com.cyruswere.playbeat_4.network.TracksClient;
import com.cyruswere.playbeat_4.util.OnStartDragListener;
import com.cyruswere.playbeat_4.util.SimpleItemTouchHelperCallback;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentKeyWords;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    TrackListAdapter adapter;
    List<Result> resultList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        recyclerView = findViewById(R.id.postRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
        Log.e("TAG", "We got here");
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        String TypedSearchKeyWOrd = intent.getStringExtra("TypedSearchKeyWOrd");

        if(mRecentKeyWords != null){
            fetchPosts(mRecentKeyWords);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();


        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchKeyWord) {
                addToSharedPreferences(searchKeyWord);
                fetchPosts(searchKeyWord);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String searchKeyWord) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ResultsActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void fetchPosts(String term){
        progressBar.setVisibility(View.VISIBLE);
        TracksClient.getRetrofitClient().getTracks(term).enqueue(new Callback<TrackResponse>() {
            @Override
            public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    resultList = response.body().getResults();
                    progressBar.setVisibility(View.GONE);
                    adapter = new TrackListAdapter(ResultsActivity.this,resultList);
                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }else{
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<TrackResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ResultsActivity.this,"Error "+ t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void showUnsuccessfulMessage() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(ResultsActivity.this,"Something went wrong, please make another search, or try later",Toast.LENGTH_SHORT).show();

    }

    private void addToSharedPreferences(String keyWord) {
        mEditor.putString(Constants.PREFERENCES_RESULT_KEY, keyWord).apply();
    }
}