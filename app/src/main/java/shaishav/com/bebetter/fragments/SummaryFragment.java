package shaishav.com.bebetter.fragments;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import im.dacer.androidcharts.LineView;
import shaishav.com.bebetter.activities.MainActivity;
import shaishav.com.bebetter.contracts.SummaryContract;
import shaishav.com.bebetter.data.models.Goal;
import shaishav.com.bebetter.data.models.Point;
import shaishav.com.bebetter.data.models.Time;
import shaishav.com.bebetter.data.source.GoalSource;
import shaishav.com.bebetter.data.source.PointSource;
import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.data.models.Usage;
import shaishav.com.bebetter.data.source.UsageSource;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.utils.Constants;
import shaishav.com.bebetter.utils.Notification;


public class SummaryFragment extends Fragment {

    PreferenceSource preferenceSource;
    private EpoxyRecyclerView recyclerView;
    private View rootView;
    private long daily_session,current_session,average_daily_usage,daily_goal,total_usage;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SummaryFragment() {
        // Required empty public constructor
    }

    public static SummaryFragment newInstance(String param1, String param2) {
        SummaryFragment fragment = new SummaryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_day_summary, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("BeBetter");
        initialize();
//        getData();
//        setData();

        return rootView;
    }

    private void initialize(){
        preferenceSource = PreferenceSource.getInstance(getActivity());
        recyclerView = rootView.findViewById(R.id.recyclerView);

//        usageChart = (LineView)rootView.findViewById(R.id.usage_chart_2);
//        usageChart.setDrawDotLine(true); //optional
//        usageChart.setColorArray(new String[]{"#674172","#F25268","#F25268"});
//        usageChart.setShowPopup(LineView.SHOW_POPUPS_All); //optional
//
//        pointsChart = (LineView) rootView.findViewById(R.id.pointsChart);
//        pointsChart.setDrawDotLine(true); //optional
//        pointsChart.setColorArray(new String[]{"#674172","#F25268","#F25268"});
//        pointsChart.setShowPopup(LineView.SHOW_POPUPS_All); //optional


        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);

    }

    private void getData(){

        if(preferenceSource.getLastUnlockedTime()!=0) {
            current_session = (new Date().getTime() - preferenceSource.getLastUnlockedTime()) / (1000 * 60);
            daily_session = (preferenceSource.getSessionTime()) / (preferenceSource.getUsageUnit());
            if(preferenceSource.getUsageUnit()==1000*60)
                daily_session = daily_session + current_session;
            else
                daily_session = daily_session + current_session/60;
            average_daily_usage = getAverageUsage()/(preferenceSource.getUsageUnit());
            daily_goal = preferenceSource.getGoal()/preferenceSource.getUsageUnit();

        }
        else{
            current_session = 0;
            daily_session = 0;
            average_daily_usage = 0;
            daily_goal = 200;
        }

        total_usage = UsageSource.getTotalUsage(getActivity());


        total_usage = (daily_session + total_usage);

//        totalPoints = PointSource.getTotalPoints(getActivity());

        ArrayList<String> xAxes = new ArrayList<>();
        ArrayList<Integer> yAxes = new ArrayList<>();
        ArrayList<Integer> threshold = new ArrayList<>();

        List<Usage> weeklyData = getWeeklyData();
        List<Goal> weeklyGoal = getWeeklyGoal();


        for (int i = 0; i < weeklyData.size(); i++) {
            Date date = new Date(weeklyData.get(i).getDate());
            xAxes.add(Constants.getFormattedDate(date));
            threshold.add((int)weeklyGoal.get(i).getGoal()/(1000*60));
            yAxes.add((int)(weeklyData.get(i).getUsage()/(preferenceSource.getUsageUnit())));
        }


        yAxes.add((int)daily_session);
        threshold.add((int)(preferenceSource.getGoal()/(preferenceSource.getUsageUnit())));
        xAxes.add(Constants.getFormattedDate(new Date()));
        ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
        data.add(yAxes);
        data.add(threshold);
//        usageChart.setBottomTextList(xAxes);
//        usageChart.setDataList(data);

        List<Point> points = PointSource.getAllPoints(getActivity());
        ArrayList<String> pointsXAxes = new ArrayList<>();
        ArrayList<Integer> pointsYAxes = new ArrayList<>();

        for (int i = 0 ; i < points.size(); i++) {
            pointsXAxes.add(Constants.getFormattedDate(new Date(points.get(i).getDate())));
            pointsYAxes.add(points.get(i).getPoints());

        }

        if (pointsXAxes.size() > 0 && pointsYAxes.size() > 0) {
            ArrayList<ArrayList<Integer>> pointsData = new ArrayList<>();
            pointsData.add(pointsYAxes);
//            pointsChart.setBottomTextList(pointsXAxes);
//            pointsChart.setDataList(pointsData);
        }

    }

    private long getAverageUsage(){
        return UsageSource.getAverageUsage(getActivity());
    }

    private void setData(){
//        animateCounter(current_session_tv,(int)current_session);
//        if(daily_session > daily_goal){
//            daily_session_tv.setTextColor(Color.RED);
//        }
//
//        Time time = Constants.getTimeUnit(total_usage);
//
//        animateCounter(daily_session_tv, (int)daily_session);
//        animateCounter(average_daily_usage_tv, (int)average_daily_usage);
//        animateCounter(total_usage_tv, time.getValue());
//
//        timeUnit.setText(time.getUnit());
//
//        animateCounter(totalPointsTextView, totalPoints);
//
//        Notification notif = new Notification();
//        notif.updateNotification(getActivity().getApplicationContext(),
//                notif.createNotification(getActivity().getApplicationContext(), String.valueOf(daily_session),
//                        String.valueOf(daily_goal)));
    }

    private void animateCounter(final TextView view,int count){
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, count);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        animator.setDuration(700);
        animator.start();
    }

    private List<Usage> getWeeklyData(){
        Date date = new Date();
        date.setDate(date.getDate()-70);
        long lower_threshold = date.getTime();
        date.setDate(date.getDate()+140);
        long higher_threshold = date.getTime();


        List<Usage> usages = UsageSource.getData(getActivity(), lower_threshold, higher_threshold);


        return usages;
    }

    private List<Goal> getWeeklyGoal(){
        Date date = new Date();
        date.setDate(date.getDate()-70);
        long lower_threshold = date.getTime();
        date.setDate(date.getDate()+140);
        long higher_threshold = date.getTime();

        List<Goal> goals = GoalSource.getData(getActivity(), lower_threshold, higher_threshold);

        return goals;
    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
        setData();
    }

}
