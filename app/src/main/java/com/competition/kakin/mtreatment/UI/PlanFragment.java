package com.competition.kakin.mtreatment.UI;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.competition.kakin.mtreatment.R;
import com.competition.kakin.mtreatment.UI.Plan.PlanMineFragment;
import com.competition.kakin.mtreatment.UI.Plan.PlanOtherFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends Fragment {


    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.plan_viewPager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.plan_tablayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }



    public class SectionPagerAdapter extends FragmentPagerAdapter{

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new PlanOtherFragment();
                case 1:
                    return new PlanMineFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "推荐计划";
                case 1:
                    return "我的计划";
            }
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
