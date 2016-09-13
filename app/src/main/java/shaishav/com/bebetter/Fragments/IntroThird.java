package shaishav.com.bebetter.Fragments;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import shaishav.com.bebetter.Data.Source.PreferenceSource;
import shaishav.com.bebetter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroThird extends Fragment {


    PreferenceSource preferenceSource;

    public IntroThird() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.intro_third, container, false);

        preferenceSource = PreferenceSource.getInstance(getActivity());
        EditText editText = (EditText)rootView.findViewById(R.id.goal);
        editText.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);


        return rootView;
    }

}
