package com.example.mypayrollactivity.myClasses;


import android.os.Parcel;
import android.os.Parcelable;

public class DailyInfoModel implements Parcelable {

	private String day; //day of the week
	private String date; //date
	private String eventNumber; //shift event number
	private String eventName; // shift event name
	private String time; //shift time period
	private double rhours; //regular hours computed for shift
	private double ohours; //overtime hours computed for the shift
	private String notes;
	public DailyInfoModel(){

	}
	public DailyInfoModel( String day, String date, String eventNumber, String eventName, String time,
	                       String notes, double rhours, double ohours){

		this.day = day;
		this.date = date;
		this.eventNumber = eventNumber;
		this.eventName = eventName;
		this.time = time;
		this.rhours = rhours;
		this.ohours = ohours;
		this.notes = notes;
	}

	public DailyInfoModel(String input){

		parseIntoVariable(input);
	}

	private void parseIntoVariable(String input){

		final String COLONS_DELIMITER = ":: ";
		String[] databaseInput = input.split(COLONS_DELIMITER);
		String stringNoSpaces;

		for (int i = 0; i < databaseInput.length; i++) {

			switch (i) {
				case 0:
					stringNoSpaces = databaseInput[i].replaceAll(" ","");
					this.day = stringNoSpaces ;
					break;
				case 1:
					stringNoSpaces = databaseInput[i].replaceAll(" ","");
					this.date = stringNoSpaces;
					break;
				case 2:
					//stringNoSpaces = databaseInput[i].replaceAll(" ","");
					//this.eventNumber = stringNoSpaces;
					this.eventNumber =  databaseInput[i];
					break;
				case 3:
					stringNoSpaces = databaseInput[i].replaceAll(" ","");
					this.eventName = stringNoSpaces; ;
					break;
				case 4:
					stringNoSpaces = databaseInput[i].replaceAll(" ","");
					this.time = stringNoSpaces;
					break;
				case 5:
					stringNoSpaces = databaseInput[i].replaceAll(" ","");
					this.rhours = Double.parseDouble(stringNoSpaces);
					break;
				case 6:
					stringNoSpaces = databaseInput[i].replaceAll(" ","");
					this.ohours = Double.parseDouble(stringNoSpaces);
					break;
				case 7:
					this.notes = databaseInput[i];
					break;
				default:

			}
		}
	}

	protected DailyInfoModel(Parcel in) {
		day = in.readString();
		date = in.readString();
		eventNumber = in.readString();
		eventName = in.readString();
		time = in.readString();
		rhours = in.readDouble();
		ohours  = in.readDouble();
		notes = in.readString();
	}

	public static final Creator<DailyInfoModel> CREATOR = new Creator<DailyInfoModel>() {
		@Override
		public DailyInfoModel createFromParcel(Parcel in) {
			return new DailyInfoModel(in);
		}

		@Override
		public DailyInfoModel[] newArray(int size) {
			return new DailyInfoModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(day);
		dest.writeString(date);
		dest.writeString(eventNumber);
		dest.writeString(eventName);
		dest.writeString(time);
		dest.writeDouble(rhours);
		dest.writeDouble(ohours);
		dest.writeString(notes);
	}

	public String getDay() {
		return day;
	}
	public String getDate() {
		return date;
	}
	public String getEventNumber() {
		return eventNumber;
	}
	public String getEventName() {
		return eventName;
	}
	public String getTime() {
		return time;
	}
	public double getRhours() {
		return rhours;
	}
	public double getOhours() {
		return ohours;
	}
	public String getNotes(){ return notes;}
	public void setDay(String day) {
		this.day = day;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setEventNumber(String eventNumber) {
		this.eventNumber = eventNumber;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setRhours(double rhours) {
		this.rhours = rhours;
	}
	public void setOhours(double ohours) {
		this.ohours = ohours;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
