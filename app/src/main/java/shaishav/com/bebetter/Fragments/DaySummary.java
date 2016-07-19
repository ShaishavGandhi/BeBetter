package shaishav.com.bebetter.Fragments;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import shaishav.com.bebetter.Data.Usage;
import shaishav.com.bebetter.Data.UsageSource;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.Constants;
import shaishav.com.bebetter.Utils.Notification;


public class DaySummary extends Fragment {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private UsageSource usageSource;
    private TextView current_session_tv,daily_session_tv,average_daily_usage_tv;
    private View rootView;
    private String daily_session,current_session,average_daily_usage;
    private LineChart lineChart;
    private SwipeRefreshLayout swipeRefreshLayout;

    public DaySummary() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
        preferences = getActivity().getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        editor = preferences.edit();

        current_session_tv = (TextView)rootView.findViewById(R.id.current_session);
        daily_session_tv = (TextView)rootView.findViewById(R.id.daily_usage);
        average_daily_usage_tv = (TextView)rootView.findViewById(R.id.average_daily_usage);
        lineChart = (LineChart)rootView.findViewById(R.id.usage_chart);

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

        if(preferences.getLong(Constants.UNLOCKED,0)!=0) {
            current_session = String.valueOf((new Date().getTime() - preferences.getLong(Constants.UNLOCKED, 0)) / (1000 * 60));
            daily_session = String.valueOf((preferences.getLong(Constants.SESSION, 0)) / (1000 * 60));
            daily_session = String.valueOf(Long.parseLong(daily_session) + Long.parseLong(current_session));
            average_daily_usage = String.valueOf(getAverageUsage()/(1000*60));

        }
        else{
            current_session = String.valueOf(0);
            daily_session = String.valueOf(0);
            average_daily_usage = String.valueOf(0);
        }

        List<Usage> weeklyData = getWeeklyData();
        ArrayList<String> xValues = new ArrayList<String>();
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        ArrayList<Entry> entries = new ArrayList<>();

        for(int i=0;i<weeklyData.size();i++){
            Date date = new Date(weeklyData.get(i).getDate());
            xValues.add(Constants.getFormattedDate(date));

            Entry data = new Entry((weeklyData.get(i).getUsage()/(1000*60)),i);

            entries.add(data);
        }

        Entry todaysEntry = new Entry(Long.parseLong(daily_session),weeklyData.size());
        xValues.add(Constants.getFormattedDate(new Date()));
        entries.add(todaysEntry);

        LineDataSet dataset = new LineDataSet(entries,"Weekly Usage In Minutes");
        dataset.setDrawCubic(true);
        dataset.setColor(getResources().getColor(R.color.colorPrimary));
        dataset.setDrawFilled(true);
        dataset.setFillColor(getResources().getColor(R.color.colorPrimaryDark));
        dataset.setFillAlpha(200);
        dataset.setCircleRadius(3);
        dataset.disableDashedLine();
        // TODO: Check this later
        dataset.setCubicIntensity(100);
        dataset.setCircleColorHole(getResources().getColor(R.color.colorPrimaryDark));
        dataset.setCircleColor(getResources().getColor(R.color.colorPrimaryDark));
        dataset.setValueTextSize(12);

        lineDataSets.add(dataset);

        LineData lineData = new LineData(xValues,lineDataSets);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.setDescription("");
        lineChart.setData(lineData);
        lineChart.invalidate();

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
        animateCounter(daily_session_tv,(int)Long.parseLong(daily_session));
        animateCounter(average_daily_usage_tv,(int)Long.parseLong(average_daily_usage));

        Notification notif = new Notification();
        notif.updateNotification(getActivity().getApplicationContext(),
                notif.createNotification(getActivity().getApplicationContext(),daily_session));
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
        date.setDate(date.getDate()-7);
        long lower_threshold = date.getTime();
        date.setDate(date.getDate()+14);
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
