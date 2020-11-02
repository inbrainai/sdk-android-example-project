package com.inbrain.example;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inbrain.sdk.model.Survey;

import java.util.List;

public class NativeSurveysAdapter extends RecyclerView.Adapter<NativeSurveysAdapter.ViewHolder> {

    private final NativeSurveysClickListener listener;
    private List<Survey> surveys;

    public NativeSurveysAdapter(NativeSurveysClickListener listener, List<Survey> surveys) {
        this.listener = listener;
        this.surveys = surveys;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setupSurvey(surveys.get(position));
    }

    @Override
    public NativeSurveysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_survey, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return surveys.size();
    }

    interface NativeSurveysClickListener {
        void surveyClicked(String surveyId);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        private TextView rankTextView;
        private TextView valueTextView;
        private TextView timeTextView;

        ViewHolder(View v) {
            super(v);
            rootView = v;
            rankTextView = v.findViewById(R.id.rank_text_view);
            valueTextView = v.findViewById(R.id.value_text_view);
            timeTextView = v.findViewById(R.id.time_text_view);
        }

        public void setupSurvey(final Survey survey) {
            rankTextView.setText("Rank:" + survey.rank);
            valueTextView.setText("Value:" + survey.value);
            timeTextView.setText("Time:" + survey.time);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.surveyClicked(survey.id);
                }
            });
        }
    }
}