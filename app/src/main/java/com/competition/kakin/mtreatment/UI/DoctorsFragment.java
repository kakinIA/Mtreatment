package com.competition.kakin.mtreatment.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.competition.kakin.mtreatment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);
        return view;
    }

}
