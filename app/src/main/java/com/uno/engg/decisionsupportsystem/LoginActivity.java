package com.uno.engg.decisionsupportsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.*;

import java.util.Locale;


public class LoginActivity extends Activity {
    Button btn_LoginIn = null;
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Parse.initialize(this, "oroHr8pv0NsXmYs4r5O8FzTkAcl6TSKpCQuiBOu0", "VkjZuCR5tZFqcRJHOhaITpVXVarsxpz8xIYP8MC1");
        mUserNameEditText = (EditText) findViewById(R.id.username);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        btn_LoginIn = (Button) findViewById(R.id.btn_login);
        cd = new ConnectionDetector(getApplicationContext());
        if(cd.isConnectingToInternet())
        {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null)
            {
                loginSuccessful();
            }
            else
            {
                btn_LoginIn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // get Internet status
                        isInternetPresent = cd.isConnectingToInternet();
                        // check for Internet status
                        if (isInternetPresent) {
                            attemptLogin();
                        } else {
                            showAlertDialog(LoginActivity.this, "No Internet Connection",
                                    "You don't have internet connection.", false);
                        }

                    }
                });
            }
        }
        else
        {
            showAlertDialog(LoginActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }
    }

    private void attemptLogin()
    {
        clearErrors();

        // Store values at the time of the login attempt.
        String username = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError("Password Empty");
            focusView = mPasswordEditText;
            cancel = true;
        }
        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUserNameEditText.setError("Username Empty");
            focusView = mUserNameEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            login(username.toLowerCase(Locale.getDefault()), password);
        }
    }
    private void login(String lowerCase, String password) {
        ParseUser.logInInBackground(lowerCase, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null)
                    loginSuccessful();
                else
                    loginUnSuccessful();
            }
        });

    }

    protected void loginSuccessful()
    {
        Intent in =  new Intent(LoginActivity.this,GraphsActivity.class);
        startActivity(in);
        finish();
    }
    protected void loginUnSuccessful()
    {
        Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        showAlertDialog(LoginActivity.this,"Login", "Username or Password is invalid.", false);
    }

    private void clearErrors(){
        mUserNameEditText.setError(null);
        mPasswordEditText.setError(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
