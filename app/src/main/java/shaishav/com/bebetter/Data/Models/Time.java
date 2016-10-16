package shaishav.com.bebetter.Data.Models;

/**
 * Created by Shaishav on 10/8/2016.
 */
public class Time {

    int value;
    String unit;

    public Time(int value, String unit){
        this.value = value;
        this.unit = unit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
