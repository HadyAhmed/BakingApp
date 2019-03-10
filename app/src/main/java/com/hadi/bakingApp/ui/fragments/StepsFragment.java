package com.hadi.bakingApp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hadi.bakingApp.R;
import com.hadi.bakingApp.adapters.StepsAdapter;
import com.hadi.bakingApp.databinding.FragmentStepsBinding;
import com.hadi.bakingApp.models.Recipe;
import com.hadi.bakingApp.models.Step;
import com.hadi.bakingApp.ui.activities.StepsActivity;

import java.util.List;

import static com.hadi.bakingApp.ui.activities.StepsActivity.RECIPE;


public class StepsFragment extends Fragment implements StepsAdapter.OnStepClickListener {
    private static final String TAG = "StepsFragment";
    private Recipe recipe;

    private OnFragmentInteractionListener mListener;

    public StepsFragment() {
        // Required empty public constructor
    }

    public static StepsFragment newInstance(Recipe recipe1) {

        StepsFragment stepsFragment = new StepsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RECIPE, recipe1);
        stepsFragment.setArguments(bundle);
        return stepsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentStepsBinding stepsBinding = FragmentStepsBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(RECIPE)) {
            recipe = (Recipe) bundle.getSerializable(RECIPE);
            List<Step> stepList = null;
            if (recipe != null) {
                stepList = recipe.getSteps();
            }
            StepsAdapter stepsAdapter = new StepsAdapter(stepList, getActivity(), this);

            stepsBinding.listSteps.setAdapter(stepsAdapter);
        }

        return stepsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (StepsActivity.masterDetailFlowMode) {
            DetailsFragment fragment = DetailsFragment.newInstance(0, recipe);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.details_fragment, fragment).commit();
            }
        }
    }

    @Override
    public void onStepClick(int position) {

        if (mListener != null) {
            mListener.fragmentOnItemClick(position);
        }
    }

    public interface OnFragmentInteractionListener {

        void fragmentOnItemClick(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: " + e.getMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
