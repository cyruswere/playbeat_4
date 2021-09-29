package com.cyruswere.playbeat_4.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cyruswere.playbeat_4.Constants;
import com.cyruswere.playbeat_4.R;
import com.cyruswere.playbeat_4.models.Result;
import com.cyruswere.playbeat_4.ui.ResultDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FirebaseResultsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    View mView;
    Context mContext;
    public ImageView mResultImageView;

    public FirebaseResultsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindResult(Result result) {
        mResultImageView = (ImageView) mView.findViewById(R.id.albumImage);
        //ImageView mAlbumImageView = (ImageView) mView.findViewById(R.id.albumImage);
        TextView mAlbumNameTextView = (TextView) mView.findViewById(R.id.albumName);
        TextView mAlbumArtistTextView = (TextView) mView.findViewById(R.id.albumArtsistName);
        TextView mAlbumTrackCountTextView = (TextView) mView.findViewById(R.id.numberOfSongs);

        Picasso.get().load(result.getArtworkUrl100()).into(mResultImageView);
        mAlbumNameTextView.setText(result.getCollectionName());
        mAlbumArtistTextView.setText(result.getArtistName());
        mAlbumTrackCountTextView.setText( result.getTrackCount() + " Songs");
    }

    @Override
    public void onClick(View view) {
        final ArrayList<Result> results = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_RESULTS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    results.add(snapshot.getValue(Result.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, ResultDetailActivity.class);
                intent.putExtra("position", itemPosition + "");
                intent.putExtra("results", Parcels.wrap(results));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

