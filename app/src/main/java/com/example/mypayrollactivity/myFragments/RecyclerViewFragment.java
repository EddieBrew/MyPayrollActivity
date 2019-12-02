package com.example.mypayrollactivity.myFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypayrollactivity.R;
import com.example.mypayrollactivity.myClasses.DailyInfoModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static com.example.mypayrollactivity.MainActivity.dbInfo;
import static com.example.mypayrollactivity.MainActivity.username;


public class RecyclerViewFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	RecyclerView recyclerView;
	// TODO: Rename and change types of parameters
	ImageView imageView;
	ImageView imageView1;
	TextView textViewDate;
	TextView textViewEventNumber;
	TextView textViewEventName;
	TextView textViewEventTime;
	TextView textViewrHours;
	TextView textViewoHours;
	TextView textViewNoteheader;
	String textViewNotes;
	View view;

	public RecyclerViewFragment() {
		// Required empty public constructor
	}
	//////////////////////////////////////BEGIN INTERFACE CODE
	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.recycler_view, container, false);
		recyclerView = view.findViewById(R.id.recyclerView);


		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
}//end class



