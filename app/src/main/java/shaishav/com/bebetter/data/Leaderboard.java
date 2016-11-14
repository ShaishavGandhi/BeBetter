package shaishav.com.bebetter.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shaishavgandhi05 on 11/13/16.
 */

public class Leaderboard implements Parcelable {

    long points;
    String name;

    protected Leaderboard(Parcel in) {
        points = in.readLong();
        name = in.readString();
    }

    public Leaderboard() {}

    public static final Creator<Leaderboard> CREATOR = new Creator<Leaderboard>() {
        @Override
        public Leaderboard createFromParcel(Parcel in) {
            return new Leaderboard(in);
        }

        @Override
        public Leaderboard[] newArray(int size) {
            return new Leaderboard[size];
        }
    };

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(points);
        dest.writeString(name);
    }
}
