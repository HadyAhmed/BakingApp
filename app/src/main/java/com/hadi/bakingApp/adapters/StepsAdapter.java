package com.hadi.bakingApp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hadi.bakingApp.R;
import com.hadi.bakingApp.models.Step;

import java.util.List;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {
    private List<Step> steps;
    private Context context;
    private OnStepClickListener onStepClickListener;

    public StepsAdapter(List<Step> steps, Context context, OnStepClickListener onStepClickListener) {
        this.steps = steps;
        this.context = context;
        this.onStepClickListener = onStepClickListener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StepViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.steps_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder stepViewHolder, int position) {
        Step step = steps.get(position);
        stepViewHolder.stepNumberTv.setText(String.valueOf(step.getId()));
        stepViewHolder.stepDescTv.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        } else {
            return steps.size();
        }
    }

    public interface OnStepClickListener {

        void onStepClick(int position);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView stepNumberTv;
        private TextView stepDescTv;

        StepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepNumberTv = itemView.findViewById(R.id.step_number);
            stepDescTv = itemView.findViewById(R.id.step_short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onStepClickListener.onStepClick(getAdapterPosition());
        }
    }
}
