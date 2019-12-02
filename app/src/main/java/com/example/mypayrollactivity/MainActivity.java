package com.example.mypayrollactivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.mypayrollactivity.adapters.HoursArrayAdapter;
import com.example.mypayrollactivity.adapters.RecyclerViewAdapter;
import com.example.mypayrollactivity.adapters.SwipeAdapter;
import com.example.mypayrollactivity.myClasses.DailyInfoModel;
import com.example.mypayrollactivity.myDatabases.HoursDataBase;
import com.example.mypayrollactivity.myFragments.DataEntryFragment;
import com.example.mypayrollactivity.myFragments.ListViewFragment;
import com.example.mypayrollactivity.myFragments.MyLayoutFragment;
import com.example.mypayrollactivity.myFragments.SwipePageFragment;
import com.example.mypayrollactivity.myFragments.QueryFieldFragment;
import com.example.mypayrollactivity.myFragments.QueryFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypayrollactivity.ui.main.SectionsPagerAdapter;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataEntryFragment.OnFragmentInteractionListener,
        QueryFieldFragment.OnFragmentInteractionListener,  QueryFragment.OnFragmentInteractionListener,
		MyLayoutFragment.OnFragmentInteractionListener, ListViewFragment.OnFragmentInteractionListener, SwipePageFragment.ListUpdated {

    private static String TAG = "MainAcivitySTUFF";
	Boolean makeMenuItemsEnabled = false;//make certain menu items enabled/disabled
	public static String username; //set to static so that the fragments can have access
	public static HoursDataBase dbInfo; //local android SQLite database
	public TabLayout tabs;
	private SectionsPagerAdapter sectionsPagerAdapter;
	public static ViewPager viewPagerMainUI, viewPagerInQueryHourTab;
	private ListView payrollListQueryFieldFragment, payrollListQueryFragment;
	private HoursArrayAdapter arrayAdapterQueryFieldFragment, arrayAdapterQueryFragment ;
	public  static Boolean isSignOnSuccessful;
	private RecyclerView recyclerView;
	private RecyclerViewAdapter adapter;
	public static ViewPager viewPager;
	static final String errorMessage = "ERROR: No Connection to Server";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		Intent uName = getIntent();
		username = uName.getStringExtra("username");
		isSignOnSuccessful = uName.getExtras().getBoolean("onlineStatus");
		setTitle("TimeCard for " + username);







		sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), makeMenuItemsEnabled);
		viewPagerMainUI = findViewById(R.id.view_pager);
		viewPagerMainUI.setAdapter(sectionsPagerAdapter);
		viewPagerMainUI.setOffscreenPageLimit(3); //sets the number of off page tabs ( not visible) to 3. Maintains the state of the tab
		tabs = findViewById(R.id.tabLayout);
		tabs.setupWithViewPager(viewPagerMainUI);

		if(dbInfo == null) {
			dbInfo = new HoursDataBase(this);
		}

		if(dbInfo.getDatabaseCount() == 0){
			try {
				updateDatabase();
			} catch (Exception e) {
				e.printStackTrace();
				//Log.i( "ERROR", e.toString());
			}
		}


		dbInfo.close();

	}



	/////////////////////////////////////////////////////////////////////////////////////////////
	/// APP'S MENU BAR METHODS
	//////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mMenuInflater = getMenuInflater();
		mMenuInflater.inflate(R.menu.menu_activity, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		if (makeMenuItemsEnabled) {
			menu.findItem(R.id.menu_deleteDatabase).setEnabled(true);
			menu.findItem(R.id.menu_UpdateLocalDatabase).setEnabled(true);
			menu.findItem(R.id.menu_UploadToServerDatabase).setEnabled(true);
		}else {
			menu.findItem(R.id.menu_deleteDatabase).setEnabled(false);
			menu.findItem(R.id.menu_UpdateLocalDatabase).setEnabled(false);
			menu.findItem(R.id.menu_UploadToServerDatabase).setEnabled(false);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Boolean menuHomeDisplay = false ;
		int fragmentSelector;
		//final String errorMessage = "ERROR: No Connection to Server";

		switch (item.getItemId()) {

			case (R.id.menu_deleteDatabase)://When the Delete Database menu item is clicked, the event below is performed:

				deleteDataBase();
				return true;

			case (R.id.menu_UpdateLocalDatabase)://Pulls data from AWS server and updates SQLite database

				new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("Update Local Database?")
						.setMessage("Do You Want to Update the Database? Previous Content will be destroyed")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {

								if(isSignOnSuccessful) {
									startDialogProcessDisplay();//initiate a DialogProgress display
									dbInfo.deleteAll();
									TextView textViewYTD = findViewById(R.id.textViewYTD);
									textViewYTD.setText("0");
									updateDatabase();
								}else {
									Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
								}

							}
						})
						.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								//allows entered data to be edited
								Toast.makeText(MainActivity.this, "Database Update Cancelled", Toast.LENGTH_SHORT).show();
							}
						}).show();
				return true;


			case(R.id.menu_UploadToServerDatabase)	:
				new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("Upload Database to Server?")
						.setMessage("Do You Want to Update the Parse Server's Database? Previous data will be destroyed")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								if(isSignOnSuccessful) {
									deleteParseServerDatabase();
									uploadLocalDatabseToServer();
								}else{
									Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
								}

							}
						})
						.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								//allows entered data to be edited
								Toast.makeText(MainActivity.this, "Database Update Cancelled", Toast.LENGTH_SHORT).show();
							}
						}).show();
				return true;
			case (R.id.menu_logout):

				new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("Logout?")
						.setMessage("Do You Want to Log Out?")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								Log.i(TAG, String.valueOf(R.string.name));
								ParseUser.logOut();


								Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
								startActivity(intent2);
								Toast.makeText(MainActivity.this, username + " logged out"
										, Toast.LENGTH_SHORT).show();
							}
						})
						.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								//allows entered data to be edited
								Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
							}
						}).show();
			default:
		}//end switch
		return true;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	/// END APP'S MENU BAR METHODS
	//////////////////////////////////

/////////////////////////////////////////////////////////////////////////////
	//MAINACTIVITY METHODS
	/////////////////////////////////////////////////////////////////////////////////

	/*********************************************************************************
	 * deleteDatabase() deletes the content of the dBInfo SQLITE database
	 * @pre none
	 * @parameter int
	 * @post  dbInfo database has no entries
	 **********************************************************************************/
	void deleteDataBase(){
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Delete Database?")
				.setMessage("Do You Want to Delete the Database? Data Can not be Retrieved")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dbInfo.deleteAll();
						TextView textViewYTD = findViewById(R.id.textViewYTD);
						textViewYTD.setText(Double.toString(0.));
						Toast.makeText(MainActivity.this, "Database Deleted", Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						//allows entered data to be edited
						Toast.makeText(MainActivity.this, "Deletion Cancelled", Toast.LENGTH_SHORT).show();

					}
				}).show();
	}



	/*********************************************************************************
	 * fillText() converts an integer to a string and places a leading zero in
	 * the date's month and day, if they consist of a single digit, for proper string
	 * representation ( i.e Jan = 1-> 01)
	 * @pre none
	 * @parameter int
	 * @post returns to 2 character String for the month and day
	 **********************************************************************************/
	public static String fillText(int i) {
		int num = 9;
		return i > num ? i + "" : "0" + i;
	}

	/*********************************************************************************
	 *  convertDateStringToInt() converts a date in String format to an integer value t
	 * @pre none
	 * @parameter String
	 * @post returns int reprentation of the date
	 **********************************************************************************/
	public static int convertDateStringToInt(String date) {
		String delimStr = "/";  //date format is mm/dd/yyyy
		String[] words = date.split(delimStr);

		return ((Integer.parseInt(words[0]) * 100) + (Integer.parseInt(words[1])) +
				Integer.parseInt(words[2]) * 10000);
	} //end method


	/*******************************************************************************************
	 * updateDatabase() retrieves the employee's time card arrayAdapterQueryFieldFragment from Parse Server and stores the content
	 *  in the SQLite database .
	 * @pre none
	 * @parameter
	 * @post none
	 ********************************************************************************************/
	public void updateDatabase() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TimeCardClass");
		query.whereEqualTo("username", username);
		query.setLimit(1000);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					if (objects.size() > 0) {
						List<DailyInfoModel> workerHoursListTemp =new ArrayList<>();
						double cumHours = 0;
						String colons = ":: ";
						for (ParseObject object : objects) {

							String data;
							data = (
									object.getString("day") + colons +
											object.getString("date") + colons +
											object.getString("eventNumber") + colons +
											object.getString("eventName") + colons +
											object.getString("time") + colons +
											Double.toString(object.getDouble("RegHours")) + colons +
											Double.toString(object.getDouble("OTHours")) + colons) +
									object.getString("Notes");

							DailyInfoModel dailyHours = new DailyInfoModel(data);
							cumHours += dailyHours.getRhours() +dailyHours.getOhours();
							workerHoursListTemp.add(dailyHours);
							TextView textViewYTD = findViewById(R.id.textViewYTD);
							textViewYTD.setText(String.format("%.2f", (cumHours)));
						} // end (ParseObject object : objects)
						sortListByAscendingDate(workerHoursListTemp);
						for (int i = 0; i < workerHoursListTemp.size(); i++) {
							dbInfo.addNewEntry(MainActivity.this, workerHoursListTemp.get(i));
						}
						workerHoursListTemp.clear();
					}  // end (objects.size() > 0)
					else {
						Toast.makeText(MainActivity.this, "No Data on Server/Database Empty", Toast.LENGTH_SHORT).show();
					}
				}//end if*e == null)
				else {
					Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});// end findInBackground

		ParseAnalytics.trackAppOpenedInBackground(null);
		//Toast.makeText(MainActivity.this, "Database Update Completed", Toast.LENGTH_SHORT).show();
	}

	/*******************************************************************************************
	 *  startDialogProcessDisplay() display a progress bar showing the download progress when updating
	 *  the SQLite database
	 * @pre none
	 * @parameter
	 * @post none
	 ********************************************************************************************/
	public void startDialogProcessDisplay(){

		final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this); ;
		progressDialog.setMax(dbInfo.getDatabaseCount()); // Progress Dialog Max Value
		progressDialog.setMessage("DownLoading in Progress..."); // Setting Message
		progressDialog.setTitle("Data Retrieveal From AWS Server"); // Setting Title
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Progress Dialog Style Horizontal
		progressDialog.show(); // Display Progress Dialog
		progressDialog.setCancelable(false);

		final Handler handle;
		handle = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				progressDialog.incrementProgressBy(2); // Incremented By Value 2
			}
		};
		//progressDialog = new ProgressDialog(MainActivity.this);



		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (progressDialog.getProgress() <= progressDialog.getMax()) {
						Thread.sleep(20);
						handle.sendMessage(handle.obtainMessage());
						if (progressDialog.getProgress() == progressDialog.getMax()) {
							progressDialog.dismiss();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


	/*******************************************************************************************
	 *  deleteParseServerDatabase() deletes all data located in the Parse Serve TimeCardClass
	 * @pre none
	 * @parameter
	 * @post none
	 ********************************************************************************************/
	public void deleteParseServerDatabase(){

		ParseQuery<ParseObject> query = ParseQuery.getQuery("TimeCardClass");
		query.whereEqualTo("username", username);

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> records, ParseException e) {
				if (e == null) {
					// iterate over all messages and delete them
					for (ParseObject record : records) {
						record.deleteInBackground();
					}
					Toast.makeText(MainActivity.this, "Parse Server Data Deleted", Toast.LENGTH_SHORT).show();
				} else {
					//Handle condition here
					Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
				}
			}//end done(List<ParseObject> records, ParseException e)
		});// end query.findInBackground
	}

	/*******************************************************************************************
	 *  uploadLocalDatabseToServer() loads the local SQLite Database to the Parse Server TimeCardClass
	 * @pre none
	 * @parameter
	 * @post none
	 ********************************************************************************************/
	public void uploadLocalDatabseToServer(){
		List<DailyInfoModel> databaseEntry;
		databaseEntry = dbInfo.getAllInfo();

		for(int i = 0; i < databaseEntry.size(); i++){
			//entry writtent to Parse Server
			String callNumber = "H36";
			ParseObject object = new ParseObject("TimeCardClass_JAVA");
			object.put("username", username);
			object.put("DateInt", convertDateStringToInt(databaseEntry.get(i).getDate()));
			object.put("date",databaseEntry.get(i).getDate() );
			object.put("day", databaseEntry.get(i).getDay());
			object.put("eventName",databaseEntry.get(i).getEventName() );
			object.put("eventNumber",databaseEntry.get(i).getEventNumber());
			object.put("time", databaseEntry.get(i).getTime());
			object.put("RegHours", databaseEntry.get(i).getRhours());
			object.put("OTHours", databaseEntry.get(i).getOhours());
			object.put("Notes", databaseEntry.get(i).getNotes());
			object.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if( e == null){
						Toast.makeText(MainActivity.this, "Data Sent To Server", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(MainActivity.this, "ERROR:" + e.toString(), Toast.LENGTH_SHORT).show();
						Log.i("ERROR: ",  e.toString());
					}
				}
			});
			ParseAnalytics.trackAppOpenedInBackground(null);
		}
	}



	/***********************************************************************************************
	 * sortListByAscendingDate() sorts a List, by their dates, from lowest to highest
	 * @pre none
	 * @parameter List<> : a list of DailyInfoModel objects
	 * @post none
	 * ********************************************************************************************/
	private void sortListByAscendingDate(List<DailyInfoModel> dataArray) {
		if (dataArray == null) {
			return;
		}

		boolean swap = true;
		int j = 0;
		while (swap) {
			swap = false;
			j++;
			for (int i = 0; i < dataArray.size() - j; i++) {
				String item1 = dataArray.get(i).getDate();
				String item2 = dataArray.get(i + 1).getDate();
				int item1Int = convertDateStringToInt(item1);
				int item2Int = convertDateStringToInt(item2);
				if (item1Int > item2Int) {//swap list item
					DailyInfoModel s = dataArray.get(i);
					dataArray.set(i, dataArray.get(i + 1));
					dataArray.set(i + 1, s);
					swap = true;
				}
			}
		}

	}


	/***********************************************************************************************
	 * sortListByDescendingDate() sorts a List, by their dates, from lowest to highest
	 * @pre none
	 * @parameter List<> : a list of DailyInfoModel objects
	 * @post none
	 * ********************************************************************************************/
	private void sortListByDescendingDate(List<DailyInfoModel> dataArray) {
		if (dataArray == null) {
			return;
		}

		boolean swap = true;
		int j = 0;
		while (swap) {
			swap = false;
			j++;
			for (int i = 0; i < dataArray.size() - j; i++) {
				String item1 = dataArray.get(i).getDate();
				String item2 = dataArray.get(i + 1).getDate();
				int item1Int = convertDateStringToInt(item1);
				int item2Int = convertDateStringToInt(item2);
				if (item1Int < item2Int) {//swap list item
					DailyInfoModel s = dataArray.get(i);
					dataArray.set(i, dataArray.get(i + 1));
					dataArray.set(i + 1, s);
					swap = true;
				}
			}
		}

	}

	///////////////////////////////////////DataEntry Fragment Interface Methods

	@Override
	public void updateSQLiteDatabase(DailyInfoModel data) {
		dbInfo.addNewEntry(MainActivity.this, data);
		List<DailyInfoModel> temp;
		temp = dbInfo.getAllInfo();//copies current database content to a list so that it can be sorted chronologically by the date
		sortListByAscendingDate(temp);//sorts list in the event that data is not added to the database chronologically
		dbInfo.deleteAll(); //clears the current database
		for( int i = 0; i < temp.size(); i++){//repopulates database with new list
			dbInfo.addNewEntry(this, temp.get(i));
		}
	}

	@Override
	public void sendUpdatedMenuItems(Boolean status) {//DataEntryFrgamnet Interface Methods
		makeMenuItemsEnabled = status;
	}



    ////////////////////////////////////////////////////////////////////////////////////////////////////



	////////////////Page Fragment Interface Methods/////////////////////////////////////////////////


	@Override
	public void displayViewPagerView(List<DailyInfoModel> list) {
		viewPager = findViewById(R.id.swipeViewpager);
		SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), list);
		viewPager.setAdapter(swipeAdapter);
		swipeAdapter.notifyDataSetChanged();

	}

	/*********************************************************************************
	 *  updateArrayList() is an interface method in Page Fragment, used to update
	 *  the EditTextPayroll Message in the fragment_query fragment layout
	 *
	 * @pre none
	 * @parameter List<DailyInfoModel></DailyInfoModel> date
	 * @post
	 **********************************************************************************/


	///////////////////////////////////////Query Fragment Interface Methods////////////////////////

	@Override
	public void displayRecyclerView(List<DailyInfoModel> list) {

		sortListByAscendingDate(list);
		if(list.size() > 0){
			Integer[] dayImages = {
					R.drawable.sunday,
					R.drawable.monday,
					R.drawable.tuesday,
					R.drawable.wednesday,
					R.drawable.thursday,
					R.drawable.friday,
					R.drawable.saturday,
			};


			LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
			//layoutManager.setMeasuredDimension(100, 100);
			recyclerView = findViewById(R.id.recyclerView);
			recyclerView.setLayoutManager(layoutManager);
			adapter = new RecyclerViewAdapter(this, list, dayImages);
			recyclerView.setAdapter(adapter);
		}

		else{
			Toast.makeText(this, "No Queries were found", Toast.LENGTH_SHORT).show();
		}

	}

////////////////////////////////////////////////////////////////////////////////////////////////////


	///////////////////////////////////////QueryField Fragment Interface Methods////////////////////////

	@Override
	public void passQueryFieldFragmentList(List<DailyInfoModel> list) {
		sortListByDescendingDate(list);
		if (list.size() > 0) {
			Integer[] dayImages = {
					R.drawable.sunday,
					R.drawable.monday,
					R.drawable.tuesday,
					R.drawable.wednesday,
					R.drawable.thursday,
					R.drawable.friday,
					R.drawable.saturday,
			};


			payrollListQueryFieldFragment = this.findViewById(R.id.listview_queryField);
			arrayAdapterQueryFieldFragment = new HoursArrayAdapter(MainActivity.this, list, dayImages);
			payrollListQueryFieldFragment.setAdapter(arrayAdapterQueryFieldFragment);

		}

		else{
			Toast.makeText(this, "No Queries were found", Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	public void onFragmentInteraction(Uri uri) {//MyLayoutFragment

	}


	@Override
	public void updateArrayList(List<DailyInfoModel> entry) {
		displayViewPagerView(entry); //updates the viewpager gui
	}

	@Override
	public void updateQueryHoursonQueryFragment(List<DailyInfoModel> entry) {
		TextView editTextBDate = findViewById(R.id.editTextBDate);
		TextView editTextEDate = findViewById(R.id.editTextEDate);
		TextView editTextPayrollMessage = findViewById(R.id.editTextPayrollMessage);
		double rh = 0;
		double oth = 0;
		for (int i = 0; i < entry.size(); i++) {
			rh = rh + entry.get(i).getRhours();
			oth = oth + entry.get(i).getOhours();
		}
		String outMessage = "Total Number of Shifts = " + entry.size() + "\n"+
				"Payroll Date: " + editTextBDate.getText() + " to " + editTextEDate.getText() + "\n" +
				"Reg Hours = " + String.format("%.2f", (rh)) + ", OT Hours = " + String.format("%.2f", (oth));

		editTextPayrollMessage.setText(outMessage);
	}

	@Override
	public void updateLocalDatabase(String date, String eName) {
		dbInfo.deleteEntry(date, eName);
	}


	/*
	public void displayViewPagerView(List<DailyInfoModel> list) {
		viewPager = findViewById(R.id.viewpager);
		SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), list);
		viewPager.setAdapter(swipeAdapter);
		swipeAdapter.notifyDataSetChanged();

	}
	*/


}