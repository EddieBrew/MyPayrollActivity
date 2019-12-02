package com.example.mypayrollactivity.ui.main;

import android.content.Context;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mypayrollactivity.R;
import com.example.mypayrollactivity.myFragments.DataEntryFragment;
import com.example.mypayrollactivity.myFragments.MyLayoutFragment;
import com.example.mypayrollactivity.myFragments.QueryFieldFragment;
import com.example.mypayrollactivity.myFragments.QueryFragment;
import com.example.mypayrollactivity.myFragments.StandfordAccessFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	@StringRes
	private static final int[] TAB_TITLES = new int[]{R.string.tab_dataEntry, R.string.tab_query, R.string.tab_queryFields,R.string.tab_axess};
	private final Context mContext;
	private Boolean isMenuItemsEnabled;

	public SectionsPagerAdapter(Context context, FragmentManager fm, Boolean menuItenStatus) {
		super(fm);
		mContext = context;
		isMenuItemsEnabled = menuItenStatus;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class below).

		switch(position){
			case 0:

				return DataEntryFragment.newInstance("Fragment1", "Fragment 1");
			case 1:
				return MyLayoutFragment.newInstance("Query", "Fragment 1");
			case 2:
				return MyLayoutFragment.newInstance("QueryField", "Fragment 1");
			case 3:
				return new StandfordAccessFragment();
			default: return null;
		}

	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return mContext.getResources().getString(TAB_TITLES[position]);
	}

	@Override
	public int getCount() {
		// Show 2 total pages.
		return 4;
	}

	@Override
	public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
		super.restoreState(state, loader);
	}


}