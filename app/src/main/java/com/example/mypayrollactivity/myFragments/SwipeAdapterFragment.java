package com.example.mypayrollactivity.myFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mypayrollactivity.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SwipeAdapterFragment extends Fragment {

    private Button myButton;
    private ViewPager viewPager;
	public SwipeAdapterFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_swipe_adapter, container, false);
		viewPager = view.findViewById(R.id.swipeViewpager);

		return view;
	}

	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);


	}
}
