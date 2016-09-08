package shaishav.com.bebetter.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shaishav.com.bebetter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFirst extends Fragment {


    public IntroFirst() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.intro_first, container, false);
    }

}
