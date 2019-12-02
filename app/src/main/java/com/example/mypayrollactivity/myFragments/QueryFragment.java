package com.example.mypayrollactivity.myFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypayrollactivity.R;
import com.example.mypayrollactivity.myClasses.DailyInfoModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.mypayrollactivity.MainActivity.convertDateStringToInt;
import static com.example.mypayrollactivity.MainActivity.dbInfo;
import static com.example.mypayrollactivity.MainActivity.fillText;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QueryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QueryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryFragment extends Fragment {

	static Calendar date = Calendar.getInstance();//displays the current date when date object appears on screen
	EditText editTextBDate;//PASS THIS TO ListViewFragment
	EditText editTextEDate;//PASS THIS TO ListViewFragment
	CheckBox chxBoxError;
	TextView editTextPayrollMessage;//
    Button btnDisplayPayPeriod;
	boolean isErrorCheckVisible = false;
	int DateSelect;



	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;



	public QueryFragment() {
		// Required empty public constructor
	}


	//////////////////////////////////Fragment Interface Code
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
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void displayViewPagerView(List<DailyInfoModel> list);
		public void displayRecyclerView(List<DailyInfoModel> list);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(List<DailyInfoModel> list) {
		if (mListener != null) {
			mListener.displayRecyclerView(list);

		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}


/////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment QueryFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static QueryFragment newInstance(String param1, String param2) {
		QueryFragment fragment = new QueryFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);


		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_query, container, false);
		Button btnBDate = view.findViewById(R.id.btnBDate);
		Button btnEDate = view.findViewById(R.id.btnEDate);
		btnDisplayPayPeriod = view.findViewById(R.id.btnDisplayPayPeriod);
		TextView editTextPayrollMessage = view.findViewById(R.id.editTextPayrollMessage);
		editTextPayrollMessage.setSelected(true);//allows the TextView to scroll horizontally across screen


		final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				date.set(Calendar.YEAR, year);
				date.set(Calendar.MONTH, monthOfYear);
				date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				String data = ((date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/"
						+ date.get(Calendar.YEAR));

				editTextBDate = getView().findViewById(R.id.editTextBDate);//Payroll Hours Layout:
				editTextEDate = getView().findViewById(R.id.editTextEDate);//Payroll Hours

				switch (DateSelect) {//updates the GUIs date textviews
					case 1:
						editTextBDate.setText(data);
						Toast.makeText(getActivity(), "Your Starting Date is " + data, LENGTH_SHORT).show();
						break;
					case 2:
						editTextEDate.setText(data);
						Toast.makeText(getActivity(), "Your Ending Date is " + data, LENGTH_SHORT).show();
						break;
					default:
				}
			}
		};


		btnBDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DateSelect = 1;
				new DatePickerDialog(getActivity(), d, date.get(Calendar.YEAR), Integer.parseInt(fillText(date.get(Calendar.MONTH))),
						Integer.parseInt(fillText(date.get(Calendar.DAY_OF_MONTH)))).show();
			}
		});


		btnEDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DateSelect = 2;
				new DatePickerDialog(getActivity(), d, date.get(Calendar.YEAR), Integer.parseInt(fillText(date.get(Calendar.MONTH))),
						Integer.parseInt(fillText(date.get(Calendar.DAY_OF_MONTH)))).show();


			}
		});

		editTextPayrollMessage.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

				if(isErrorCheckVisible) {
					chxBoxError.setVisibility(View.INVISIBLE);
					isErrorCheckVisible = false;
				}
				else {
					chxBoxError.setVisibility(View.VISIBLE);
					isErrorCheckVisible = true;
				}
				return false;
			}
		});
		return view;
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		chxBoxError = getActivity().findViewById(R.id.chxBoxError);
		btnDisplayPayPeriod =  getActivity().findViewById(R.id.btnDisplayPayPeriod);


		btnDisplayPayPeriod.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				editTextBDate = getActivity().findViewById(R.id.editTextBDate);//Payroll Hours Layout:
				editTextEDate = getActivity().findViewById(R.id.editTextEDate);//Payroll Hours Layout
				try {
					List<DailyInfoModel> hoursInfo ;
					List<DailyInfoModel> tempArray = dbInfo.getAllInfo();
					int date1 = convertDateStringToInt(String.valueOf(editTextBDate.getText()));
					int date2 = convertDateStringToInt(String.valueOf(editTextEDate.getText()));
					hoursInfo = getPayrollHours(tempArray, date1, date2);// list corresponding to date1 and date2 range
					//onButtonPressed(hoursInfo); //---method used to implement a recycler view to display queryFragment query
					mListener.displayViewPagerView(hoursInfo);
				}catch (Exception e){
					Toast.makeText(getActivity(),  e.toString(), Toast.LENGTH_LONG).show();
					System.out.println(e.toString());
				}
			}
		});



	}

	/*********************************************************************************
	 * getPayrollHours() creates a List object containing DailyInfoModel objects
	 * ( between user defined dates )and computes the total regular and overtime hours. The List
	 * is used to populate a Listview display
	 * @pre none
	 * @parameter List<>: Sqlite database entries, int: Beginning date parameter ,
	 *            int: Endind date parameter
	 * @post List<>: List containg DailyInfoModel variables found within defined date parameters
	 **********************************************************************************/
	List<DailyInfoModel> getPayrollHours(final List<DailyInfoModel> dataArray, int bDay, int eDay) {
		double rHours = 0;
		double otHours = 0;



		editTextBDate = getView().findViewById(R.id.editTextBDate);
		editTextEDate = getView().findViewById(R.id.editTextEDate);//Payroll Hours Layout:
		editTextPayrollMessage = getView().findViewById(R.id.editTextPayrollMessage);
		List<DailyInfoModel> timeCardPayPeriod = new ArrayList<>();

		for (DailyInfoModel element : dataArray) {
			int num = 0;

			if ((convertDateStringToInt(element.getDate()) >= bDay) &&
					(convertDateStringToInt(element.getDate()) <= eDay)) {

				timeCardPayPeriod.add(element);
				Log.i("MyStuff4", element.getNotes());
				rHours += element.getRhours();
				otHours += element.getOhours();
				num++;
			}
		}


		String outMessage = "Payroll Date: " + editTextBDate.getText() + " to " + editTextEDate.getText() + "\n" +
				"Total Number of Shifts = " + timeCardPayPeriod.size() + "\n" +
				"Reg Hours = " + String.format("%.2f", (rHours)) + ", OT Hours = " + String.format("%.2f", (otHours));

		editTextPayrollMessage.setText(outMessage);
		return timeCardPayPeriod;
	}














}
