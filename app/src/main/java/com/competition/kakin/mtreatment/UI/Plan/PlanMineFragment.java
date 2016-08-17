package com.competition.kakin.mtreatment.UI.Plan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.competition.kakin.mtreatment.R;

/**
 * Created by kakin on 2016/8/8.
 */
public class PlanMineFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_myplan, container, false);
        return view;
    }
}
