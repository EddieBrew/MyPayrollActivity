package com.example.mypayrollactivity.myFragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mypayrollactivity.MainActivity;
import com.example.mypayrollactivity.R;
import com.example.mypayrollactivity.myClasses.DailyInfoModel;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.mypayrollactivity.MainActivity.convertDateStringToInt;
import static com.example.mypayrollactivity.MainActivity.dbInfo;
import static com.example.mypayrollactivity.MainActivity.fillText;
import static com.example.mypayrollactivity.MainActivity.username;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DataEntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DataEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataEntryFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

	Calendar date = Calendar.getInstance();//displays the current date when date object appears on screen
	//static String username; //set to static so that the fragments can have access

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	double cumulativeHours;
	TextView textViewYTD;
	TextView textViewRegHours;
	TextView textViewRegHourslabel;
	TextView textViewOTHours;
	String dailyTimeIn;
	String dailyTimeOut;
	EditText txtEventNum;
	EditText txtEventName;
	CheckBox checkboxPAID;
	CheckBox checkBoxPaidHour;
	Button buttonDate;
	Button buttonTimeIn;
	Button buttonTimeOut;
	Button btnSetData;
	EditText editTextInfo;
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	Context mContext;
	boolean isMenuItemEnabled = false;
	private DailyInfoModel duplicateDate;
	private OnFragmentInteractionListener mListener;


	//Interface methods to interact with MainActivity
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		//void onFragmentInteraction(Uri uri);

		public void updateSQLiteDatabase(DailyInfoModel data);
		public void sendUpdatedMenuItems(Boolean status);
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	public void onButtonPressed() {
		if (mListener != null) {

		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	public DataEntryFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment DataEntryFragment.
	 */






	// TODO: Rename and change types and number of parameters
	public static DataEntryFragment newInstance(String param1, String param2) {
		DataEntryFragment fragment = new DataEntryFragment();
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
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_dataentry, container, false);
		textViewYTD = view.findViewById(R.id.textViewYTD);
		textViewRegHours = view.findViewById(R.id.textViewRegHours);
		textViewOTHours = view.findViewById(R.id.textViewOTHours);
		buttonDate = view.findViewById(R.id.buttonDate);
		buttonTimeIn = view.findViewById(R.id.buttonTimeIn);
		buttonTimeOut = view.findViewById(R.id.buttonTimeOut);
		btnSetData = view.findViewById(R.id.btnSetData);
		checkboxPAID = view.findViewById(R.id.checkBoxPaid);
		checkBoxPaidHour = view.findViewById(R.id.checkBoxPaidHour);
		txtEventName = view.findViewById(R.id.editTextEventName);
		txtEventNum = view.findViewById(R.id.editTextEventNum);
		textViewRegHourslabel = view.findViewById(R.id.textViewRegHourslabel);
		editTextInfo = view.findViewById(R.id.editTextInfo);
		mContext = getActivity();



		buttonDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//DateSelect = 0;
				setWorkDate();
			}
		});

		buttonTimeIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int timeSelect = 1;
				setTime(timeSelect);
			}
		});

		buttonTimeOut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int timeSelect = 2;
				setTime(timeSelect);
			}
		});

		btnSetData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					//playCashRegisterSound();
					processHoursInfo();
				}
				catch(Exception e){
					Toast.makeText(getActivity(),  e.toString(), Toast.LENGTH_LONG).show();
					//Log.i( "MYERROR", e.toString());

				}
			}
		});

		textViewRegHourslabel.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if(isMenuItemEnabled) {
					isMenuItemEnabled =false;
					Toast.makeText(getActivity(),  "Menu Items Disabled", Toast.LENGTH_LONG).show();
					mListener.sendUpdatedMenuItems(isMenuItemEnabled);

				}
				else{
					isMenuItemEnabled = true;
					Toast.makeText(getActivity(),  "Menu Items Enabled", Toast.LENGTH_LONG).show();
					mListener.sendUpdatedMenuItems(isMenuItemEnabled);

				}
				return isMenuItemEnabled;
			}
		});


		checkBoxPaidHour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkBoxPaidHour.isChecked()){
					checkboxPAID.setChecked(false); ;
				}
			}
		});


		checkboxPAID.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkboxPAID.isChecked()){
					checkBoxPaidHour.setChecked(false); ;
				}
			}
		});

		return view;

		//return inflater.inflate(R.layout.fragment_dataentry, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getTotalHours();
	}


/////////////////////////////////////////////Methods//////////////////////////////////

	public void setWorkDate() {

		DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				date.set(Calendar.YEAR, year);
				date.set(Calendar.MONTH, monthOfYear);
				date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				String data = (  (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/"
						+ date.get(Calendar.YEAR));
				Toast.makeText(getActivity(), "Your Work Date is " + data, LENGTH_SHORT).show();

			}
		};

		new DatePickerDialog(mContext, d,  date.get(Calendar.YEAR), Integer.parseInt(fillText(date.get(Calendar.MONTH))) ,
				Integer.parseInt(fillText(date.get(Calendar.DAY_OF_MONTH)))).show();

	}

	/*********************************************************************************
	 *  getTotalHours() reads the SQLite database and computes the total number of
	 *  worked hours
	 * @pre none
	 * @parameter none
	 * @post Updates the DataEntryFragment layout textView "Year To Date Hours"
	 **********************************************************************************/
	public void  getTotalHours() {
		cumulativeHours = 0;
		cumulativeHours = dbInfo.getTotalRegHours() + dbInfo.getTotalOTHours();
		textViewYTD.setText(String.format("%.2f", (cumulativeHours)));//main_layout widget

		Log.i("STUFF", String.valueOf(cumulativeHours));
	}// end getTotalHours()


	public void setTime(final int timeNum) {

		TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				date.set(Calendar.HOUR_OF_DAY, hourOfDay);
				date.set(Calendar.MINUTE, minute);

				switch (timeNum) {
					case 1:
						dailyTimeIn = fillText(date.get(Calendar.HOUR_OF_DAY)) + fillText(date.get(Calendar.MINUTE));
						Toast.makeText(getActivity(), "Your Beginning Time is " + dailyTimeIn, Toast.LENGTH_SHORT).show();
						break;
					case 2:
						dailyTimeOut = fillText(date.get(Calendar.HOUR_OF_DAY)) + fillText(date.get(Calendar.MINUTE));
						Toast.makeText(getActivity(), "Your Ending Time is " + dailyTimeOut, Toast.LENGTH_SHORT).show();
						break;
				}
			}
		};
		new TimePickerDialog(mContext, t, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true).show();
	}

	/*********************************************************************************
	 * String processHoursInfo() collects all DataEntryFragment layout UI widgets ,
	 * process the data, places the  data into the sqlite database and Parse Server
	 * and computes the total working hours stored in the data base
	 *
	 * @pre none
	 * @parameter none
	 * @post Updates the DataEntryFragment layouts textViews( Regular Hours,
	 *       Over Time Hours and Year To Date Hours)
	 **********************************************************************************/
	private void processHoursInfo() {
		double otHours = 0;//overtime hours
		double regHours = 0;//regular hours
		boolean isDuplicateDate = false;
		StringBuilder tempString;
		//String theNotes;
		cumulativeHours = Double.parseDouble((String) textViewYTD.getText());

		int timeIN = Integer.parseInt((String) dailyTimeIn);
		int timeOUT = Integer.parseInt((String) dailyTimeOut);
		double totHours = 0;

		if (timeOUT > timeIN) {//hours worked within a 24Hr day
			totHours = computeHoursWorkedA(timeIN, timeOUT);
		} else {
			totHours = computeHoursWorkedB(timeIN, timeOUT);//hours worked within two days
		}

		if (totHours > 8.0) {
			regHours = 8;
			otHours = totHours - 8.;
			otHours = Math.round(otHours * 100) / 100.0d;
		} else {
			regHours = Math.round(totHours *100)/100.0d;
			otHours = 0;
		}
		cumulativeHours += totHours;

		String workDay = (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/"
				+ date.get(Calendar.YEAR);



		if(dbInfo.checkForDuplicateDate(workDay) != null){
			isDuplicateDate = true;

			//need to check database using query
			double dateRegHours = dbInfo.checkForDuplicateDate(workDay).getRhours(); //hours for date record found
			double maxDailyHours = 8.;
			//recalculates the total work hours for the multiple shifts entered for that day

			//        6.5     <  8                    3    >      8 - 6.5
			if((dateRegHours < maxDailyHours) && (regHours > ( maxDailyHours - dateRegHours )) ){
				double oldRegHours = regHours;//saved pevious regHours calculated
				regHours = maxDailyHours - dateRegHours; // new reghours for shift
				otHours = otHours + (oldRegHours - regHours)  ; //updated otHours
			}
			if (dateRegHours >= maxDailyHours){
				otHours = otHours + regHours ;
				regHours = 0;
			}


		}


		final String message = "Multiple shifts enter for day " + workDay + "\n" + "Hours updated to reflect the total hours for that day. ";

		//String that will display the input for verification by the user
		tempString = new StringBuilder();

		tempString.append("Date: " + (getDayOfWeek(date.get(Calendar.DAY_OF_WEEK)))+ ". " + workDay);
		tempString.append("\n");
		tempString.append("Event Number: " + String.valueOf(txtEventNum.getText()));
		tempString.append("\n");
		tempString.append("Event Name: " + String.valueOf(txtEventName.getText()));
		tempString.append("\n");
		tempString.append("Hours: " +  dailyTimeIn+ " - " + dailyTimeOut) ;
		tempString.append("\n");
		tempString.append("Reg Hours: " + Double.toString(regHours));
		tempString.append("\n");
		tempString.append("OT  Hours: " + Double.toString(otHours));
		tempString.append("\n\n");

		if(isDuplicateDate){
			tempString.append(message);
		}

		String theNotes = String.valueOf(editTextInfo.getText());
		checkDataBeforeInsertion( workDay, regHours,  otHours, getDayOfWeek(date.get(Calendar.DAY_OF_WEEK)), tempString, theNotes);
		tempString.setLength(0);

	}

	/***********************************************************************************************
	 * checkDataBeforeInsertion() displays all data and requiress the user to approve data before
	 * inserting the data ibto the server
	 * @pre none
	 * @parameter String: date, double: daily regular hours, double: daily overtime hours, String: day of the week
	 * @post inserts the data into the database, if approved or discards the data, forcing the user to renter the data
	 * ********************************************************************************************/
	public void checkDataBeforeInsertion(final String workDay, final double regHours, final double otHours, final String dayOfWeek,
	                                     final StringBuilder tempString, final String theNotes ){


		new AlertDialog.Builder(getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Time Card Daily Info")
				.setMessage(tempString)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

						textViewRegHours.setText(String.format("%.2f", regHours));//main_layout widget
						textViewOTHours.setText(String.format("%.2f", otHours));//main_layout widget

						if(MainActivity.isSignOnSuccessful) {
							placeInDatabaseNServer(workDay, regHours, otHours, dayOfWeek, theNotes);
							playCashRegisterSound();
							cumulativeHours = Double.parseDouble(textViewYTD.getText().toString()) + regHours + otHours;
							textViewYTD.setText(String.format("%.2f", (cumulativeHours)));//main_layout widget
							txtEventNum.setText(""); //clears edittext boxes
							txtEventName.setText("");
							editTextInfo.setText("");
						}
						else {
							Toast.makeText(getActivity(), "No Server Available. No Data Sent", Toast.LENGTH_LONG).show();
						}

					}

				})
				.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						//allows entered data to be edited
						Toast.makeText(getActivity(), "MAKE CORRECTION TO INPUT DATA", Toast.LENGTH_SHORT).show();

					}
				})
				.setNeutralButton("CANCEL/ClEAR", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

						Toast.makeText(getActivity(), "DATA CLEARED. RE-ENTER ALL DATA", Toast.LENGTH_SHORT).show();
						txtEventNum.setText(""); //clears edittext boxes
						txtEventName.setText("");
						textViewRegHours.setText("0");//main_layout widget
						textViewOTHours.setText("0");//main_layout widget
						editTextInfo.setText("");


					}
				})
				.show();
	}


	/*********************************************************************************
	 *  playCashRegisterSound() plays an audio cash register sound when the info
	 *  entered in the DataEntry tab has been sent to the server
	 *
	 * @pre none
	 * @parameter String  date
	 * @post DailyInfoModel : all data pertainng to the workers payoll hours for that day.
	 **********************************************************************************/
	 private void playCashRegisterSound(){
		 MediaPlayer mPlayer = MediaPlayer.create(getActivity(), R.raw.cash_register);
		 mPlayer .start();;

	 }


	/*********************************************************************************
	 * getDuplicateDate()checks to see if a newly date entry is present in the current server database.
	 * If the date is present, an DailymodelInfo object containing the object info is returned
	 *
	 * @pre none
	 * @parameter String  date
	 * @post DailyInfoModel : all data pertainng to the workers payoll hours for that day.
	 **********************************************************************************/
	public DailyInfoModel getDuplicateDate(List<DailyInfoModel> data, String date){


		duplicateDate = new DailyInfoModel();
		for (int i = 0; i < data.size(); i++){
			if(convertDateStringToInt(date) == convertDateStringToInt(data.get(i).getDate())){

				Log.i("dates: ", date + " : " + data.get(i).getDate() );
				duplicateDate = data.get(i);
				return duplicateDate;
			}
		}
		return null;
	}

	/*********************************************************************************
	 * computeHoursWorkedA()computes the daily working hours when the shift is within
	 * the same calendar day
	 * @pre none
	 * @parameter int tin: start time, int tout: end time
	 * @post double : total hours worked for the shift
	 **********************************************************************************/
	private double computeHoursWorkedA(int tin, int tout) {
		// double hours;
		final double lunchBaseline = 10.0;
		double regHours;
		double partialHour = 0;
		int timeInMins = tin % 100;
		int timeOutMins = tout % 100;

		regHours = (tout - tin) / 100;
		if ((timeInMins < timeOutMins) || (timeInMins == timeOutMins)) { //Timeout mins > timein mins ( timrout = --45, timeIn = --15
			partialHour = ((tout - tin) % 100) / 60.;
		} else {
			partialHour = (60 + timeOutMins - timeInMins) / 60.;
		}


		//main_layout widget
		if ((!checkboxPAID.isChecked()) && (!checkBoxPaidHour.isChecked()) && (regHours >= lunchBaseline)) {
			return (regHours + partialHour - 1.0);
		} else if ((!checkboxPAID.isChecked()) && (!checkBoxPaidHour.isChecked())&& (regHours < lunchBaseline)) {
			return (regHours + partialHour - 0.5);
		} else if(checkBoxPaidHour.isChecked() && !checkboxPAID.isChecked()  ){
			return (regHours + partialHour - 1.0);
		} else{
			return regHours + partialHour;
		}
	} //end method


	/*********************************************************************************
	 * computeHoursWorkedB()computes the daily working hours when the shift ends in the
	 * next calendar day
	 * @pre none
	 * @parameter int tin: start time, int tout: end time
	 * @post double : total hours worked for the shift
	 **********************************************************************************/
	private double computeHoursWorkedB(int tin, int tout) {
		final double lunchBaseline = 10.0;
		int newday = 2360;
		int timeInMins = tin % 100;
		int timeOutMins = tout % 100;
		double partialHour;
		double regHours;

		regHours = (((2400 - tin) + tout) / 100);
		if (timeOutMins < timeInMins) {//
			partialHour = ((newday - tin + tout) % 100) / 60.;//45/60 = .75
			if (partialHour == 0.) {
				regHours++;
			}

		}//end if
		else {
			partialHour = ((timeOutMins - timeInMins) % 100) / 60.;
		}//end else


		//main_layout widget
		if ((!checkboxPAID.isChecked()) && (!checkBoxPaidHour.isChecked()) && (regHours >= lunchBaseline)) {
			return (regHours + partialHour - 1.0);
		} else if ((!checkboxPAID.isChecked()) && (!checkBoxPaidHour.isChecked())&& (regHours < lunchBaseline)) {
			return (regHours + partialHour - 0.5);
		} else if(checkBoxPaidHour.isChecked() && !checkboxPAID.isChecked()  ){
			return (regHours + partialHour - 1.0);
		} else{
			return regHours + partialHour;
		}


	} //end method

	/*********************************************************************************
	 * getDayOfWeek() takes the integer represntation of the day,from the Date class
	 * and converts it to a String, representing the day of the week
	 * @pre none
	 * @parameter int intDay: integer representation of the day of the week
	 * @post String : string representation of the day of the week
	 **********************************************************************************/
	public String getDayOfWeek(int intDay){
		String dayString = "";

		switch(intDay){
			case 1: dayString = "SUN";
				break;
			case 2: dayString = "MON";
				break;
			case 3: dayString = "TUE";
				break;
			case 4: dayString = "WED";
				break;
			case 5: dayString = "THU";
				break;
			case 6: dayString = "FRI";
				break;
			case 7: dayString = "SAT";
				break;
			default:
		}
		return dayString;
	}



	/*********************************************************************************
	 * placeDataInServer() populates the DailyInfoMode class variables and inserts
	 *  the class time card info into  the Parse Server
	 * @pre none
	 * @parameter String: date, double: regular hours , double: overtime hours
	 *            String: day of the week
	 * @post none: Inserts DailyInfoModel class variables into server
	 **********************************************************************************/
	public void placeInDatabaseNServer(String workDay, Double regHours, Double otHours, String day, String theNotes) {
		//updates the gui and place the daily entries into the Parse Server


		DailyInfoModel databaseEntry = new DailyInfoModel();
		databaseEntry.setDay(day);
		databaseEntry.setDate(workDay);
		databaseEntry.setEventNumber(String.valueOf(txtEventNum.getText()));
		databaseEntry.setEventName(String.valueOf(txtEventName.getText()));
		databaseEntry.setTime(dailyTimeIn + " - " + dailyTimeOut);
		databaseEntry.setRhours(regHours);
		databaseEntry.setOhours(otHours);
		databaseEntry.setNotes(theNotes);



		//entry writtent to Parse Server
		String callNumber = "H36";
		ParseObject object = new ParseObject("TimeCardClass");
		object.put("username", username);
		object.put("DateInt", convertDateStringToInt(databaseEntry.getDate()));
		object.put("date",databaseEntry.getDate() );
		object.put("day", databaseEntry.getDay());
		object.put("eventName",databaseEntry.getEventName() );
		object.put("eventNumber",databaseEntry.getEventNumber());
		object.put("time", databaseEntry.getTime());
		object.put("RegHours", databaseEntry.getRhours());
		object.put("OTHours", databaseEntry.getOhours());
		object.put("Notes", databaseEntry.getNotes());
		object.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if( e == null){
					Toast.makeText(getActivity(), "Data Sent To Server", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), "ERROR1:" + e.toString(), Toast.LENGTH_SHORT).show();
					Log.i("ERROR1: ",  e.toString());
				}
			}
		});
		ParseAnalytics.trackAppOpenedInBackground(null);
		mListener.updateSQLiteDatabase(databaseEntry);

	}




}
