package com.example.mypayrollactivity;
/*



Created by Robert Brewer on 11/30/2019.

The MySEPWorkHours app is an app that keeps tracks of a Stanford SEP workers shift hours by storing the information
on an AWS database and a local SQLite database on the mobile device. All data retrieved for query operations are retrieved from the local
SQLite database

1) Login Activity--Sign on page to access the AWS database using Parse Server. The user will be able to query local the SQLitedatabase, while offline
2) MainActivity---Activity interface where the user's experience interface with 3 tabs using a tabLayout.
                of several Fragments. The user also have the option os saving his log on information to prevent entring every time at app startup
      A) Tab 1: DataEntry - Allows the user to input his daily employment info
                DataEntryFragment Fragment:  User provided inputs containing the SEP workers shift information. A pop-up dialog
                box will appear, allowing the user to approve or make data changes, before sending the info the the server and local databaseto.
      B) Tab 2: Query Hours- Provides daily worker Info within a specified date range
                QueryFragmenyFragment, : Provides the user the ability to get daily work hour information within a specified date range
                SwipeAdapterFragment: Displays the daily work info using a custom SwipeAdapter class.
      C) Tab3: Query Fields- Allows the user to fetch data, depending the desired paramaters selected from the "Select Field" and "Select Item" menuss
               QueryFieldFragment = Allowthe user to select the query fields in which they want return data
               ListViewFragment- List the results from the queries


3) StarterApplication---Implement methods to access AWS Parse Server information
*/


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.InputStream;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
	EditText usernameEditText;
	EditText passwordEditText;
	TextView changeSignupModeTextView;
	CheckBox chkBoxSaveLogOn;
	Boolean signUpModeActive = false;
	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;
	Button signUpButton;
	InputStream is = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle("Login/Signin");
		changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);

		usernameEditText = findViewById(R.id.usernameEditText);
		passwordEditText = findViewById(R.id.passwordEditText);
		chkBoxSaveLogOn = findViewById(R.id.chkBoxSaveLogOn);



		mPreferences = getSharedPreferences("MyPreference", Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
		checkSharedPreference();

		ParseAnalytics.trackAppOpenedInBackground(getIntent());
	}

	public void signUp(View view) {
		if (usernameEditText.getText().toString().matches("") ||
				passwordEditText.getText().toString().matches("")) {
			Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();
		} else {
			if (signUpModeActive) {
				ParseUser user = new ParseUser();
				user.setUsername(usernameEditText.getText().toString());
				user.setPassword(passwordEditText.getText().toString());
				user.signUpInBackground(new SignUpCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {

							Intent intent = new Intent(getApplicationContext(), MainActivity.class);
							intent.putExtra("username",usernameEditText.getText().toString() );
							startActivity(intent);
							Toast.makeText(LoginActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
							usernameEditText.setText("");
							passwordEditText.setText("");

						}
					}
				});
			} else {
				ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
					@Override
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							Log.i("Signup", "Login successful");

							Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
							Bundle bundle = new Bundle();
							bundle.putBoolean("onlineStatus", true);
							bundle.putString("username", usernameEditText.getText().toString());
							Intent intent = new Intent(getApplicationContext(), MainActivity.class);
							intent.putExtras(bundle);
							if(chkBoxSaveLogOn.isChecked()){
								//set a checkbox when the application starts
								mEditor.putString(getString(R.string.chkbox), "True");
								mEditor.commit();

								//save the username preference
								mEditor.putString(getString(R.string.name), usernameEditText.getText().toString());
								mEditor.commit();

								//save the password
								mEditor.putString(getString(R.string.pWord),passwordEditText.getText().toString() );
								mEditor.commit();
							} else{
								//set a checkbox when the application starts
								mEditor.putString(getString(R.string.chkbox), "False");
								mEditor.commit();

								//save the username preference
								mEditor.putString(getString(R.string.name), "");
								mEditor.commit();

								//save the password
								mEditor.putString(getString(R.string.pWord),"");
								mEditor.commit();
							}

							startActivity(intent);

						} else {
							Toast.makeText(LoginActivity.this, "Check Server: " + e.getMessage(), Toast.LENGTH_SHORT).show();

							new AlertDialog.Builder(LoginActivity.this)
									.setIcon(android.R.drawable.ic_dialog_alert)
									.setTitle("USE APP OFFLINE?")
									.setMessage("Login or server issues. Do you want to use the app offline? Select Yes ONLY, if you have populated the app database " +
											"after the initial installation. " )
									//app opens up MainActivity2. The user is only able to do queries. Data can not be saved
									//to server or SQLite database
									.setPositiveButton("YES", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialogInterface, int i) {


											Bundle bundle = new Bundle();
											bundle.putBoolean("onlineStatus", false);
											bundle.putString("username", "OFFLINE");
											Intent intent = new Intent(getApplicationContext(), MainActivity.class);
											intent.putExtras(bundle);
											startActivity(intent);
										}
									})
									.setNegativeButton("NO", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialogInterface, int i) {//app returns to LoginActivity
											passwordEditText.setText("");
										}
									})
									.show();
							passwordEditText.setText("");
						}//end else
					}
				});
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.changeSignupModeTextView) {
			Button statusBtn = (Button) findViewById(R.id.statusBtn);
			if (signUpModeActive) {
				signUpModeActive = false;
				statusBtn.setText("Login");
				changeSignupModeTextView.setText("Or, Signup");
			} else {
				signUpModeActive = true;
				statusBtn.setText("Signup");
				changeSignupModeTextView.setText("Or, Login");
			}
		}
	}

	@Override
	public boolean onKey(View view, int i, KeyEvent keyEvent) {
		if( i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN){
			signUp(view);
		}
		return false;
	}

	/*
	    checkSharedPreference() checks the shared preferences and set them accordingly
	 */

	private void checkSharedPreference(){

		String chkbox = mPreferences.getString(getString(R.string.chkbox), "False");
		String name = mPreferences.getString(getString(R.string.name), "");
		String pWord = mPreferences.getString(getString(R.string.pWord), "");

		usernameEditText.setText(name);
		passwordEditText.setText(pWord);

		if(chkbox.equals("True")){
			chkBoxSaveLogOn.setChecked(true);
		} else{
			chkBoxSaveLogOn.setChecked(false);
		}

	}

}
