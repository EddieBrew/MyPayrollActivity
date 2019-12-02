package com.example.mypayrollactivity.myDatabases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mypayrollactivity.myClasses.DailyInfoModel;


import java.util.ArrayList;
import java.util.List;

import static com.example.mypayrollactivity.myDatabases.PayrollContract.ContractEntry.TABLE_NAME;


public class HoursDataBase extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "MyPayrolldb";
	private static final int DATABASE_VERSION = 3;
	private final int DailyInfoModelCountrolInt = 1;
	final String COLONS_DELIMITER = ":: ";

	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
			"("+ PayrollContract.ContractEntry._COLUMN_ID  + " INTEGER PRIMARY KEY," +
			PayrollContract.ContractEntry._COLUMN_DAY+" text," +
			PayrollContract.ContractEntry._COLUMN_DATE+" text," +
			PayrollContract.ContractEntry._COLUMN_EVENTNUM+" text," +
			PayrollContract.ContractEntry._COLUMN_EVENTNAME+" text," +
			PayrollContract.ContractEntry._COLUMN_TIME +" text," +
			PayrollContract.ContractEntry._COLUMN_RHOURS+" number," +
			PayrollContract.ContractEntry._COLUMN_OTHOURS+" number, " +
			PayrollContract.ContractEntry._COLUMN_NOTES+ " text);";

	public static final String DROP_TABLE = "drop table if exist" + TABLE_NAME;

	public HoursDataBase(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i("Database Operation", "Database created.....");
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
		Log.i("Database Operation", "Table created.....");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion)
			db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
		onCreate(db);

	}

	public  void addNewEntry(Context context, DailyInfoModel payrollEntry ) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(PayrollContract.ContractEntry._COLUMN_DAY, payrollEntry.getDay() );
		values.put(PayrollContract.ContractEntry._COLUMN_DATE, payrollEntry.getDate() );
		values.put(PayrollContract.ContractEntry._COLUMN_EVENTNUM,  payrollEntry.getEventNumber() );
		values.put(PayrollContract.ContractEntry._COLUMN_EVENTNAME, payrollEntry.getEventName() );
		values.put(PayrollContract.ContractEntry._COLUMN_TIME, payrollEntry.getTime());
		values.put(PayrollContract.ContractEntry._COLUMN_RHOURS, payrollEntry.getRhours());
		values.put(PayrollContract.ContractEntry._COLUMN_OTHOURS, payrollEntry.getOhours() );
		values.put(PayrollContract.ContractEntry._COLUMN_NOTES, payrollEntry.getNotes());

		// Inserting Row
		db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection
	}


	public Double getTotalRegHours(){
		int indexSelect = 6;
		Double totalRegHours = 0.;
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				//String info ;
				//index = 0 is the tables ID and is not used as a varaibale in the DailyInfoModel class
				for(int index = 1; index < 8; index++  ) {
					if(index == indexSelect){
						totalRegHours +=cursor.getDouble(indexSelect);
					}
				}
			} while (cursor.moveToNext());
		}
		return totalRegHours;
	}


	public Double getTotalOTHours(){
		int indexSelect = 7;
		Double totalOTHours = 0.;
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				for(int index = 1; index < 8; index++  ) {
					if(index == indexSelect){
						totalOTHours +=cursor.getDouble(indexSelect);
					}
				}
			} while (cursor.moveToNext());
		}
		return totalOTHours;
	}

	public DailyInfoModel checkForDuplicateDate(String queryValue){

		SQLiteDatabase db = this.getWritableDatabase();
		DailyInfoModel entry;
		// Select All Query
		int indexSelect = 2;//selects the date field
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				String info ;
				//index = 0 is the tables ID and is not used as a varaibale in the DailyInfoModel class
				for(int index = 1; index < 8; index++  ) {
					if(index == indexSelect) {
						info = cursor.getString(index); //retrieves the date string
						entry = new DailyInfoModel();
						if (info.equals(queryValue)) {
							entry.setDay(cursor.getString(2));// index 2 = date
							entry.setRhours(cursor.getDouble(6));// index 6 = reghours
							entry.setOhours(cursor.getDouble(7)); // index 7 = 0thours
							return entry;
						}
					}
				}
			} while (cursor.moveToNext());
		}
		db.close();
		return null;
	}

	// Getting All WorkedHours
	public List<DailyInfoModel> getAllInfo() {

		SQLiteDatabase db = this.getWritableDatabase();
		List<DailyInfoModel> workersHoursList = new ArrayList<>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		Cursor cursor = db.rawQuery(selectQuery, null);

		//final String COMMA = ", ";
		if (cursor.moveToFirst()) {
			do {
				String info  = "";
				//index = 0 is the tables ID and is not used as a varaibale in the DailyInfoModel class
				for(int index = 1; index < 9; index++  ){
					info = info  + cursor.getString(index) + COLONS_DELIMITER;
				}
				DailyInfoModel dailyHours = new DailyInfoModel(info);
				workersHoursList.add(dailyHours);//adds daily employees record to list
			} while (cursor.moveToNext());
		}
		db.close();
		return workersHoursList;
	}



	public int getDatabaseCount(){
		int itemCount = 0;

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				itemCount++;
			} while (cursor.moveToNext());
		}
		db.close();
		return itemCount;
	}

	public void deleteEntry(String date, String eNum){



		SQLiteDatabase db = this.getWritableDatabase();
		String queryItems= PayrollContract.ContractEntry._COLUMN_DATE + "=? AND " +
				PayrollContract.ContractEntry._COLUMN_EVENTNUM + "=?";
		Log.i("ERROR", date);
		Log.i("ERROR", eNum);
		db.delete(TABLE_NAME,  queryItems  , new String[]{date, eNum});
	}

	public void deleteAll(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_NAME); //delete all rows in a table
		db.close();
	}


	public List<DailyInfoModel> getQueryResultsInAList(Context context, String column, String columnItem) {
		SQLiteDatabase db = this.getWritableDatabase();
		List<DailyInfoModel> workersHoursList = new ArrayList<>();

		// Does a query search defined by the column and the column items
		String selectQuery = "";

		if(column.equals("Day")){
			selectQuery = "SELECT  * FROM " + TABLE_NAME  + " WHERE " +   PayrollContract.ContractEntry._COLUMN_DAY + " = " + "'" + columnItem + "'";
		} else if (column.equals("Event Name")){
			//The wildcard symbol "%" is used for the WomenBB and MenBB column item queries
			selectQuery = "SELECT  * FROM " + TABLE_NAME  + " WHERE " +   PayrollContract.ContractEntry._COLUMN_EVENTNAME + " LIKE " + "'" + columnItem + "%'";
		}else if (column.equals("Event Number")){
			selectQuery = "SELECT  * FROM " + TABLE_NAME  + " WHERE " +   PayrollContract.ContractEntry._COLUMN_EVENTNUM + " = " + "'" + columnItem + "'";
		} else{
			selectQuery = null;
		}

		//Log.i("MyData = ", selectQuery);

		if(selectQuery != null) {
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					String info = "";
					//index = 0 is the tables ID and is not used as a varaibale in the DailyInfoModel class
					for (int index = 1; index < 9; index++) {
						info = info + cursor.getString(index) + COLONS_DELIMITER;
					}
					DailyInfoModel dailyHours = new DailyInfoModel(info);
					workersHoursList.add(dailyHours);//adds daily employees record to list
				} while (cursor.moveToNext());
			}

			db.close();
		}

		return workersHoursList;
	}

}
