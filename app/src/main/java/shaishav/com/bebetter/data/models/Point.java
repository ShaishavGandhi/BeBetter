package shaishav.com.bebetter.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shaishavgandhi05 on 11/6/16.
 */

public class Point implements Parcelable{

    int points;
    long date;
    long id;

    protected Point(Parcel in) {
        points = in.readInt();
        date = in.readLong();
        id = in.readLong();
    }

    public Point(){

    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(points);
        dest.writeLong(date);
        dest.writeLong(id);
    }
}
