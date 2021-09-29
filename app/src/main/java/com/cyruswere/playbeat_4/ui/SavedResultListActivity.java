package com.cyruswere.playbeat_4.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cyruswere.playbeat_4.Constants;
import com.cyruswere.playbeat_4.R;
import com.cyruswere.playbeat_4.adapter.FirebaseResultListAdapter;
import com.cyruswere.playbeat_4.adapter.FirebaseResultsViewHolder;
import com.cyruswere.playbeat_4.models.Result;
import com.cyruswere.playbeat_4.util.OnStartDragListener;
import com.cyruswere.playbeat_4.util.SimpleItemTouchHelperCallback;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedResultListActivity extends AppCompatActivity implements OnStartDragListener {

    private DatabaseReference mResultReference;
    private ItemTouchHelper mItemTouchHelper;
    private FirebaseResultListAdapter mFirebaseAdapter;

    @BindView(R.id.postRecyclerView) RecyclerView mRecyclerView;
    //@BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);


        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mResultReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_RESULTS).child(uid);
        FirebaseRecyclerOptions<Result> options =
                new FirebaseRecyclerOptions.Builder<Result>()
                        .setQuery(mResultReference, Result.class)
                        .build();

        mFirebaseAdapter = new FirebaseResultListAdapter(options, mResultReference, this, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
        mRecyclerView.setHasFixedSize(true);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter!= null) {
            mFirebaseAdapter.stopListening();
        }
    }
    public void onStartDrag(RecyclerView.ViewHolder viewHolder){
        mItemTouchHelper.startDrag(viewHolder);
    }

}
