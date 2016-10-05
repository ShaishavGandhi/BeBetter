package shaishav.com.bebetter.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shaishav.com.bebetter.Activities.Details;
import shaishav.com.bebetter.Data.Models.Experience;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.Constants;

import java.util.Date;
import java.util.List;


public class ExperienceRecyclerViewAdapter extends RecyclerView.Adapter<ExperienceRecyclerViewAdapter.ViewHolder> {

    private final List<Experience> mValues;
    private View view;
    private Context context;

    public ExperienceRecyclerViewAdapter(List<Experience> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_lesson, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        Date date = new Date(mValues.get(position).getCreated_at());
        holder.mContentView.setText(Constants.getFormattedDate(date));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Details.class);
                Experience temp = mValues.get(position);
                intent.putExtra("id",temp.getId());
                intent.putExtra("title",temp.getTitle());
                intent.putExtra("category",temp.getCategory());
                intent.putExtra("created_at",temp.getCreated_at());
                intent.putExtra("content",temp.getLesson());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Experience mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
