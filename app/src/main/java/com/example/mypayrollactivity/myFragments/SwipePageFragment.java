package com.example.mypayrollactivity.myFragments;

/*
SwipePageFragment configures the layout and sets the view
attributes for displaying the SEP work shift information

*/


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.mypayrollactivity.R;
import com.example.mypayrollactivity.myClasses.DailyInfoModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import static com.example.mypayrollactivity.MainActivity.username;


/**
 * A simple {@link Fragment} subclass.
 */
public class SwipePageFragment extends Fragment {

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
	private Animation anim;//object used to bring blinking functionality to textViewNoteheader
	private ListUpdated myListUpdatedListener;

	public interface ListUpdated{
		public void updateArrayList(List<DailyInfoModel> entry);
		public void updateQueryHoursonQueryFragment(List<DailyInfoModel> entry);
		public void updateLocalDatabase(String date, String eNum);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try{

			myListUpdatedListener = (ListUpdated) context;
		}catch(ClassCastException e){
			e.printStackTrace();
		}
	}

	public SwipePageFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		view = inflater.inflate(R.layout.fragment_swipe_page, container, false);
		textViewDate =  view.findViewById(R.id.textViewDate);
		textViewEventNumber = view.findViewById(R.id.textViewEventNumber);
		textViewEventName = view.findViewById(R.id.textViewEventName);
		textViewEventTime = view.findViewById(R.id.textViewTime);
		textViewrHours = view.findViewById(R.id.textViewrHours);
		textViewoHours = view.findViewById(R.id.textViewoHours);
		imageView = view.findViewById(R.id.imageView);
		imageView1 = view.findViewById(R.id.imageView1);
		textViewNoteheader = view.findViewById(R.id.textViewNoteheader);

		 final Bundle bundle  = getArguments();
		 final List<DailyInfoModel> myArray ;
		myArray = bundle.getParcelableArrayList("myList");//receives the data list from MainACtivity


		final int index = (bundle.getInt("count"));
		textViewDate.setText("Date: " +  myArray.get(index  ).getDate());
		textViewEventNumber.setText("Event Number: " +  myArray.get(index  ).getEventNumber());
		textViewEventName.setText("Event Name: " +  myArray.get(index  ).getEventName());
		textViewEventTime.setText("Time: " +  myArray.get(index  ).getTime());
		textViewrHours.setText("Regular Hours: " + myArray.get(index  ).getRhours());
		textViewoHours.setText("OverTime Hours: " +  myArray.get(index  ).getOhours());



		switch(getYear(myArray.get(index).getDate())){
			case 2017:imageView1.setImageResource(R.drawable.year2017);
			break;
			case 2018:imageView1.setImageResource(R.drawable.year2018);
			break;
			case 2019: imageView1.setImageResource(R.drawable.year2019);
			break;
			case 2020: imageView1.setImageResource(R.drawable.year2020);
				break;
			default:
		}




		String myColors = " " ;
		switch(myArray.get(index).getDay()){
			case "SUN" : imageView.setImageResource(R.drawable.sunday);
				          myColors = "#FFFF0000";//red
			break;
			case "MON" : imageView.setImageResource(R.drawable.monday);
			    myColors = "#FFFFBB33";//orange
				break;
			case "TUE" : imageView.setImageResource(R.drawable.tuesday);
				myColors = "#FFFFC0CB";//pink
				break;
			case "WED" : imageView.setImageResource(R.drawable.wednesday);

				myColors = "#FF008000";//green
				break;
			case "THU" : imageView.setImageResource(R.drawable.thursday);
				myColors = "#FF0099CC";//aqua blue
				break;
			case "FRI" : imageView.setImageResource(R.drawable.friday);
				myColors = "#FF000080";//navy blue
				break;
			case "SAT" : imageView.setImageResource(R.drawable.saturday);
				myColors = "#FFAA66CC";//purple
				break;
			default:
		}



		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (bundle.getBoolean("offline")) {//creates SwipeAdapter view using AWS server data
						Toast.makeText(getActivity(), "Not Connected To Server Data", Toast.LENGTH_LONG).show();
					} else {
						int x = index;
						askDeletion(myArray, x); //provides popup box to verify entry gets deleted
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});


		textViewNoteheader.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(textViewNotes.length() > 4) { //if there is no data, textViewNotes contains the string
					// null which has a length of 4. Therefore, the dialog box
					// will not appear
					//Log.i("MyStuff", textViewNotes);
					showAddItemDialog(textViewNotes);
				}
			}
		});

		/*
		imageView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(textViewNotes.length() > 4) { //if there is no data, textViewNotes contains the string
					                             // null which has a length of 4. Therefore, the dialog box
					                             // will not appear
					Log.i("MyStuff", textViewNotes);
					showAddItemDialog(textViewNotes);
				}
			}
		});
		*/




		textViewDate.setTextColor(Color.parseColor(myColors));
		textViewEventNumber.setTextColor(Color.parseColor(myColors));
		textViewEventName.setTextColor(Color.parseColor(myColors));
		textViewEventTime.setTextColor(Color.parseColor(myColors));
		textViewrHours.setTextColor(Color.parseColor(myColors));
		textViewoHours.setTextColor(Color.parseColor(myColors));
		textViewNoteheader.setTextColor(Color.parseColor(myColors));//orange
		textViewNotes = myArray.get(index).getNotes();
		Log.i("MyStuff", textViewNotes);
		if(textViewNotes.length() > 10){
			textViewNoteheader.setText("Additional Info Available. Click On Text");
			startBlinkText();
		}
		else{
			textViewNoteheader.setText("No Additonal Information");
		}

		return view;
	}

	/***********************************************************************************************
	 * showAddItemDialog displays a dialog message box that  contains info entered from the employee's
	 * shift for the day
	 * @pre none
	 * @parameter String info: info from daily shift
	 * @post displays info in a dialog box
	 * ********************************************************************************************/
	private void showAddItemDialog(String info) {


       Log.i("MyStuff", info);
		new AlertDialog.Builder(getActivity())
				.setTitle("ADDITIONAL INFORMATION")
				.setMessage(info)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						stopBlinkText();
					}
				}).show();
				//show().getWindow().setLayout(1000,1000);
	}


	/***********************************************************************************************
	 *  startBlinkText() blinks the Additional info text to inform user that there is more
	 * information that must be read by the user. The additional info is displayed in a
	 * dialog nox when the user click the year image
	 * @pre none
	 * @parameter none
	 * @post none
	 * ********************************************************************************************/
	public void startBlinkText() {

		anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(500); //You can manage the time of the blink with this parameter
		anim.setStartOffset(200);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		textViewNoteheader.startAnimation(anim);
	}

	/***********************************************************************************************
	 *  stopBlinkText() stps blinkinkin the Additional info text upon user viewing
	 * information  displayed in the dialog box
	 * @pre none
	 * @parameter none
	 * @post none
	 * ********************************************************************************************/
	public void stopBlinkText() {
		try {
			//TextView myText = (TextView) findViewById(R.id.state);
			anim.cancel();
			anim.reset();
			textViewNoteheader.setText("Additional Information");
			// myText.startAnimation(anim);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	Integer getYear(String year){
	    String delimStr = "/";  //date format is mm/dd/yyyy
	    String[] words = year.split(delimStr);

	    return Integer.parseInt(words[2]);

    }


	/***********************************************************************************************
	 * askDeletion() displays a dialog message box and ask for confirmation when the user wants to delete a payroll
	 * entry displayed on the viewpager fragment display. If the user selects OK, the ingormation is deleted
	 * from the cloud server permanently and the gui
	 * @pre none
	 * @parameter List<DailyInfoModel> myArray: list that contains the DailyInfomodel class dates that were queried ,
	 *            int index: item number deleted from viewpager fragment display
	 * @post updates the viewpage display when deleting an item
	 * ********************************************************************************************/

	void askDeletion(final List<DailyInfoModel> myArray, final int index){


		new AlertDialog.Builder(getActivity())
				.setIcon(android.R.drawable.alert_dark_frame)
				.setTitle("DELETE FROM SERVER?" )
				.setMessage("Do You want to delete date " + myArray.get(index).getDate() + " from the server?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, final int which) {
						//code to delete row from Parse Server

						Toast.makeText(getActivity(), "Date: " + myArray.get(index).getDate() +
								"  Deleted.", Toast.LENGTH_SHORT).show();

						Log.i("OUTPUT",  username + ", " + myArray.get(index).getDate() +
								", " + myArray.get(index).getEventNumber());

						ParseQuery<ParseObject> query = ParseQuery.getQuery("TimeCardClass");
						query.whereEqualTo("username", username);
						query.whereEqualTo("date", myArray.get(index).getDate());
						query.whereEqualTo("eventNumber", myArray.get(index).getEventNumber());

						query.findInBackground(new FindCallback<ParseObject>() {
							public void done(List<ParseObject> records, ParseException e) {
								if (e == null) {
									// iterate over all messages and delete them
									for (ParseObject record : records) {
										record.deleteInBackground();
										myArray.remove(index);
										myListUpdatedListener.updateArrayList(myArray);
										myListUpdatedListener.updateQueryHoursonQueryFragment(myArray);
									}

								} else {
									//Handle condition here
									Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
								}
							}//end done(List<ParseObject> records, ParseException e)
						});// end query.findInBackground

						myListUpdatedListener.updateLocalDatabase(myArray.get(index).getDate(), myArray.get(index).getEventNumber());
					}//end onClick(DialogInterface dialog, int which)
				})//end setPositive
				.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Toast.makeText(getActivity(), "DELETION CANCELED", Toast.LENGTH_SHORT).show();
							}
						}//end setNegative
				).show();//ends AlertDialog.Builder
	}//end askDeletion

}//end class
