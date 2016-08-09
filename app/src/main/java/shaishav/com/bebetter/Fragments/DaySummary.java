package shaishav.com.bebetter.Fragments;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.w3c.dom.Text;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import im.dacer.androidcharts.LineView;
import shaishav.com.bebetter.Data.PreferenceSource;
import shaishav.com.bebetter.Data.Usage;
import shaishav.com.bebetter.Data.UsageSource;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.Constants;
import shaishav.com.bebetter.Utils.Notification;


public class DaySummary extends Fragment {

    PreferenceSource preferenceSource;
    private UsageSource usageSource;
    private TextView current_session_tv,daily_session_tv,average_daily_usage_tv,total_usage_tv;
    private View rootView;
    private String daily_session,current_session,average_daily_usage,daily_goal,total_usage;
    LineView lineView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public DaySummary() {
        // Required empty public constructor
    }

    public static DaySummary newInstance(String param1, String param2) {
        DaySummary fragment = new DaySummary();

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
        initialize();
        getData();
        setData();

        return rootView;
    }

    private void initialize(){
        preferenceSource = PreferenceSource.getInstance(getActivity());

        current_session_tv = (TextView)rootView.findViewById(R.id.current_session);
        daily_session_tv = (TextView)rootView.findViewById(R.id.daily_usage);
        average_daily_usage_tv = (TextView)rootView.findViewById(R.id.average_daily_usage);
        total_usage_tv = (TextView)rootView.findViewById(R.id.total_usage);
        lineView = (LineView)rootView.findViewById(R.id.usage_chart_2);
        lineView.setDrawDotLine(true); //optional
        lineView.setColorArray(new String[]{"#674172","#F25268","#F25268"});
        lineView.setShowPopup(LineView.SHOW_POPUPS_All); //optional



        usageSource = new UsageSource(getActivity().getApplicationContext());


        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                setData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void getData(){

        if(preferenceSource.getLastUnlockedTime()!=0) {
            current_session = String.valueOf((new Date().getTime() - preferenceSource.getLastUnlockedTime()) / (1000 * 60));
            daily_session = String.valueOf((preferenceSource.getSessionTime()) / (preferenceSource.getUsageUnit()));
            if(preferenceSource.getUsageUnit()==1000*60)
                daily_session = String.valueOf(Long.parseLong(daily_session) + Long.parseLong(current_session));
            else
                daily_session = String.valueOf(Long.parseLong(daily_session) + Long.parseLong(current_session)/60);
            average_daily_usage = String.valueOf(getAverageUsage()/(preferenceSource.getUsageUnit()));
            daily_goal = String.valueOf(preferenceSource.getGoal());

        }
        else{
            current_session = String.valueOf(0);
            daily_session = String.valueOf(0);
            average_daily_usage = String.valueOf(0);
            daily_goal = String.valueOf(200);
        }

        usageSource.open();
        total_usage = String.valueOf(usageSource.getTotalUsage()/(1000*60*60));
        usageSource.close();

        total_usage = String.valueOf((Long.valueOf(daily_session) + Long.valueOf(total_usage)*60)/60);


        ArrayList<String> xAxes = new ArrayList<>();
        ArrayList<Integer> yAxes = new ArrayList<>();
        ArrayList<Integer> threshold = new ArrayList<>();

        List<Usage> weeklyData = getWeeklyData();



        for(int i=0;i<weeklyData.size();i++){
            Date date = new Date(weeklyData.get(i).getDate());
            xAxes.add(Constants.getFormattedDate(date));
            threshold.add((int)(preferenceSource.getGoal()/(preferenceSource.getUsageUnit())));
            yAxes.add((int)(weeklyData.get(i).getUsage()/(preferenceSource.getUsageUnit())));
        }


        yAxes.add((int)Long.parseLong(daily_session));
        threshold.add((int)(preferenceSource.getGoal()/(preferenceSource.getUsageUnit())));
        xAxes.add(Constants.getFormattedDate(new Date()));
        ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
        data.add(yAxes);
        data.add(threshold);
        lineView.setBottomTextList(xAxes);
        lineView.setDataList(data);

    }

    private long getAverageUsage(){
        usageSource.open();
        List<Usage> usages = usageSource.getAllUsages();
        long sum = 0;

        if(usages.size()==0)
            return 0;

        for(Usage usage : usages){
            sum += usage.getUsage();
        }

        return sum/(usages.size());

    }

    private void setData(){
        animateCounter(current_session_tv,(int)Long.parseLong(current_session));
        if(Long.parseLong(daily_session) > Long.parseLong(daily_goal)){
            daily_session_tv.setTextColor(Color.RED);
        }
        animateCounter(daily_session_tv, (int)Long.parseLong(daily_session));
        animateCounter(average_daily_usage_tv, (int)Long.parseLong(average_daily_usage));
        animateCounter(total_usage_tv, Integer.parseInt(total_usage));

        Notification notif = new Notification();
        notif.updateNotification(getActivity().getApplicationContext(),
                notif.createNotification(getActivity().getApplicationContext(),daily_session,daily_goal));
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

        usageSource.open();
        List<Usage> usages = usageSource.getData(lower_threshold,higher_threshold);
        usageSource.close();

        return usages;
    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
        setData();
    }

}
