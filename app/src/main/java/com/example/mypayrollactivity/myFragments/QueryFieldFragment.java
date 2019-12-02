package com.example.mypayrollactivity.myFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mypayrollactivity.R;
import com.example.mypayrollactivity.myClasses.DailyInfoModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.mypayrollactivity.MainActivity.dbInfo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QueryFieldFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QueryFieldFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryFieldFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private Spinner field_spinner, itemEntries_spinner;
	private Button btnQuery;
	private List<String> itemSpinnerList;
	private ArrayAdapter<String> itemDataAdapter;
	private String fieldName;//query parameter
	private String itemName;//query parameter
	private TextView listViewMessage;
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	public QueryFieldFragment() {
		// Required empty public constructor
	}


	///////////////////////////BEGIN INTERFACE CODE//////////////////////////////////
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
		public void passQueryFieldFragmentList(List<DailyInfoModel> list);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(List<DailyInfoModel> myList) {
		if (mListener != null) {
			mListener.passQueryFieldFragmentList(myList);
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

	/////////////////////////// END INTERFACE CODE//////////////////////////////////

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment QueryFieldFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static QueryFieldFragment newInstance(String param1, String param2) {
		QueryFieldFragment fragment = new QueryFieldFragment();
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
		View view =  inflater.inflate(R.layout.fragment_query_field, container, false);



		field_spinner = view.findViewById(R.id.field_spinner);
		itemEntries_spinner = view.findViewById(R.id.itemEntries_spinner);

		listViewMessage = view.findViewById(R.id.listViewMessage);
		btnQuery = view.findViewById(R.id.btnQuery);

		//sets up field spinner
		List<String> fieldSpinnerList = Arrays.asList(getResources().getStringArray(R.array.queryfields_array));
		ArrayAdapter<String> fieldDataAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_items, fieldSpinnerList);
		fieldDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		field_spinner.setAdapter(fieldDataAdapter);

		field_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				fieldName = (String) field_spinner.getItemAtPosition(position);
				fillSelectItemSpinner(position);//sets up itemSpinnerList
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});


		btnQuery = view.findViewById(R.id.btnQuery);
		btnQuery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					List<DailyInfoModel> tempArray ;
					tempArray = dbInfo.getQueryResultsInAList(getContext(), fieldName, itemName);
					listViewMessage.setText("Results = " + tempArray.size());
					onButtonPressed(tempArray);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		field_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				fieldName = (String) field_spinner.getItemAtPosition(position);
				fillSelectItemSpinner(position);//sets up itemSpinnerList
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}

	private void fillSelectItemSpinner (int position) {

		itemSpinnerList = addItemsOnItemSpinner(position);
		ArrayAdapter<String> itemDataAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_items, itemSpinnerList);
		itemEntries_spinner.setAdapter(itemDataAdapter);
		itemEntries_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				itemName = (String) parent.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}


	public List<String> addItemsOnItemSpinner (int selector){
		List<String> itemSpinnerListA = new ArrayList<>();

		switch (selector) {
			case 0:
				itemSpinnerListA = Arrays.asList(getResources().getStringArray(R.array.eventdays_array));
				break;
			case 1:
				itemSpinnerListA = Arrays.asList(getResources().getStringArray(R.array.eventnumbers_array));
				break;
			case 2:
				itemSpinnerListA = Arrays.asList(getResources().getStringArray(R.array.eventnames_array));
			default:
				break;
		}
		return itemSpinnerListA;
	}




}
