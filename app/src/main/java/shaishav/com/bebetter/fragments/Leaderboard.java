package shaishav.com.bebetter.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import shaishav.com.bebetter.R;
import shaishav.com.bebetter.holder.LeaderboardHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Leaderboard extends Fragment {


    FirebaseRecyclerAdapter<shaishav.com.bebetter.data.Leaderboard, LeaderboardHolder> mAdapter;
    RecyclerView mRecyclerView;


    public Leaderboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);


        Query fiftyHighest = FirebaseDatabase.getInstance().getReference("points").orderByValue().limitToLast(50);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FirebaseRecyclerAdapter<shaishav.com.bebetter.data.Leaderboard, LeaderboardHolder>(shaishav.com.bebetter.data.Leaderboard.class,
                R.layout.list_item_leaderboard, LeaderboardHolder.class, fiftyHighest) {
            @Override
            protected void populateViewHolder(LeaderboardHolder viewHolder, shaishav.com.bebetter.data.Leaderboard model, int position) {
                viewHolder.mName.setText(model.getName());
                viewHolder.mPoints.setText(model.getPoints() + " points");
                viewHolder.mPosition.setText((position + 1) + "");
            }
        };

        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

}
