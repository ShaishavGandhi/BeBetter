package shaishav.com.bebetter.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by shaishavgandhi05 on 10/29/16.
 */

public class ImageSwitch extends ImageButton {

    private boolean isChecked = false;

    public ImageSwitch(Context context) {
        super(context);
    }

    public ImageSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isChecked(){
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;

        if (isChecked) {
            setColorFilter(Color.parseColor("#674172"));
        } else {
            setColorFilter(Color.BLACK);
        }
    }



    public void toggleCheck() {
        if (isChecked) {
            setIsChecked(false);
        } else {
            setIsChecked(true);
        }
    }


}
