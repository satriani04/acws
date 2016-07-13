package com.fisipol.winarto.sistempelaporanstudi.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fisipol.winarto.sistempelaporanstudi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WarningFragment extends Fragment {


    public WarningFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.warning_fragment, container, false);
    }

}
