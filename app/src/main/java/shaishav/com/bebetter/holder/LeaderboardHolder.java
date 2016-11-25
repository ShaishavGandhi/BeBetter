package shaishav.com.bebetter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import shaishav.com.bebetter.R;

/**
 * Created by shaishavgandhi05 on 11/24/16.
 */

public class LeaderboardHolder extends RecyclerView.ViewHolder {

    public TextView mName;
    public TextView mPoints;
    public TextView mPosition;

    public LeaderboardHolder(View itemView) {
        super(itemView);

        mName = (TextView) itemView.findViewById(R.id.name);
        mPoints = (TextView) itemView.findViewById(R.id.points);
        mPosition = (TextView) itemView.findViewById(R.id.position);
    }
}
