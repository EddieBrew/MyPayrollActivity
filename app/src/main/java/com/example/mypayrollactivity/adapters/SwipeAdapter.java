package com.example.mypayrollactivity.adapters;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mypayrollactivity.myClasses.DailyInfoModel;
import com.example.mypayrollactivity.myFragments.SwipePageFragment;

import java.util.ArrayList;
import java.util.List;

public class SwipeAdapter extends FragmentStatePagerAdapter {

	private Bundle bundle;
	private int itemCount;
	List<DailyInfoModel> hoursInfo;

	public SwipeAdapter(FragmentManager fm, List<DailyInfoModel> hoursInfo) {
		super(fm);
		this.hoursInfo =  hoursInfo;
		this.itemCount =  hoursInfo.size();
	}

	@NonNull
	@Override
	public Fragment getItem(int position) {

		bundle = new Bundle();
		bundle.putInt("itemCount", itemCount);
		bundle.putInt("count", position);
		bundle.putParcelableArrayList("myList", (ArrayList<? extends Parcelable>) hoursInfo);

		Fragment fragment = new SwipePageFragment();
		fragment.setArguments(bundle);

		return fragment;


	}

	@Override
	public int getCount() {
		return itemCount;
	}
}
