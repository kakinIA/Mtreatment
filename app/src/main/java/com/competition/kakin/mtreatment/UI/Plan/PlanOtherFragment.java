package com.competition.kakin.mtreatment.UI.Plan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.competition.kakin.mtreatment.Adapter.PlanAdapter;
import com.competition.kakin.mtreatment.PlanContent;
import com.competition.kakin.mtreatment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kakin on 2016/8/8.
 */
public class PlanOtherFragment extends Fragment{

    private ListView listView;
    private List<PlanContent> planContents = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_other, container, false);
        initPlanContents();
        PlanAdapter planAdapter = new PlanAdapter(getContext(), R.layout.plan_cell, planContents);
        listView = (ListView) view.findViewById(R.id.plan_other_list);
        listView.setAdapter(planAdapter);
        return view;
    }

    private void initPlanContents(){

        int check = getResources().getIdentifier("plan_run", "drawable", getContext().getPackageName());
        System.out.println("注意！---注意--" + check);
        PlanContent planContent1= new PlanContent(getResources().getIdentifier("plan_run", "drawable", getContext().getPackageName()),
                "慢跑有氧运动计划",
                "周期：56周",
                "本计划非常好，慢跑是目前所知道的运动当中最有益的运动，能强身健体，收心养性！");
        planContents.add(planContent1);
    }
}
