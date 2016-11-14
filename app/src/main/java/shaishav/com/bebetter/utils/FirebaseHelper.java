package shaishav.com.bebetter.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import shaishav.com.bebetter.data.Leaderboard;

/**
 * Created by shaishavgandhi05 on 11/13/16.
 */

public class FirebaseHelper {

    public static void saveGoalInDb(long points) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("points");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setName(user.getDisplayName());
        leaderboard.setPoints(points);

        reference.child(user.getUid()).setValue(leaderboard);
    }

}
