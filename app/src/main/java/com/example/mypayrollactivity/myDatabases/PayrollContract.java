package com.example.mypayrollactivity.myDatabases;

public class PayrollContract {

	private PayrollContract(){}

	public static class ContractEntry{

		// Contacts table name
		public static final String TABLE_NAME = "Daily_Payroll_Information";
		// Contacts Table Columns names
		public static final String _COLUMN_ID = "id";
		public static final String _COLUMN_DAY = "Day";
		public static final String _COLUMN_DATE = "Date";
		public static final String _COLUMN_EVENTNUM  = "EventNumber";
		public static final String _COLUMN_EVENTNAME = "EventName";
		public static final String _COLUMN_TIME = "WorkingHours";
		public static final String _COLUMN_RHOURS = "RHours";
		public static final String _COLUMN_OTHOURS = "OTHours";
		public static final String _COLUMN_NOTES = "Notes";
	}
}
