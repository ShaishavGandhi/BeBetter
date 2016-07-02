package shaishav.com.bebetter.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import shaishav.com.bebetter.Data.Lesson;
import shaishav.com.bebetter.Data.LessonSource;
import shaishav.com.bebetter.Adapters.LessonRecyclerViewAdapter;
import shaishav.com.bebetter.R;

import java.util.List;


public class LessonList extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;

    LessonSource lessonSource;
    public List<Lesson> lessonList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LessonList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_list, container, false);

        //Get the lesson source
        lessonSource = new LessonSource(getActivity().getApplicationContext());

        //Get all lessons
        lessonSource.open();
        lessonList = lessonSource.getAllLessons();
        lessonSource.close();



        // Set the adapter
        if (view instanceof LinearLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            LinearLayout emptyView = (LinearLayout)view.findViewById(R.id.emptyView);
            if(lessonList.size()==0){
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);

            }
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new LessonRecyclerViewAdapter(lessonList));
        }
        return view;
    }




}
