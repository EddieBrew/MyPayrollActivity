package com.example.mypayrollactivity;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

import java.io.IOException;
import java.io.InputStream;


public class StarterApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		 try {
		 	//reads the parse server configuration details from a file into an inputstream
		 	InputStream is = getApplicationContext().getAssets().open("mysignonstuff.txt");

		 	int ch;
			StringBuilder sb = new StringBuilder();
			while((ch = is.read()) != -1) {//reads inputstream chars into a stringbuilder object
				sb.append((char) ch);
			}
		    String dataString = sb.toString(); // converts strinbuilderobject to a string
		    final String DELIMITER = "#";
		    String[] getMeIn = dataString.split(DELIMITER); // splits the files details into
	                                                     // string arrays

		    // Enable Local Datastore.
		    Parse.enableLocalDatastore(this);
		    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
				 .applicationId(getMeIn[0])
				 .clientKey(getMeIn[1])
				 .server(getMeIn[2])
				 .build()
			 );
		    ParseACL defaultACL = new ParseACL();
		    defaultACL.setPublicReadAccess(true);
	        defaultACL.setPublicWriteAccess(true);
		    ParseACL.setDefaultACL(defaultACL, true);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
