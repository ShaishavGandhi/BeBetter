package shaishav.com.bebetter.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shaishav.com.bebetter.Activities.Login;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.App;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogOut extends Fragment {


    public LogOut() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());

        App.getInstance().clearApplicationData();
        Intent intent = new Intent(getActivity(),Login.class);
        startActivity(intent);
        getActivity().finish();

        return textView;
    }

}
