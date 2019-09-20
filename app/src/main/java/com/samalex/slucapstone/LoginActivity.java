package com.samalex.slucapstone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private String lastActivity;


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private static final String TAG = "EmailPassword";

    // UI references.
    //private AutoCompleteTextView mEmailView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private FirebaseAuth mAuth;
    private String UserID;
    private Intent intent;
    private String currentUserBool;
    private FirebaseUser currentUser;
    private String username;
    private Integer loginAttempts;
    private LinearLayout startCycleSpinnerLayoutView;
    private Spinner startCycleSpinnerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        // Set up the login form.
        intent = new Intent(LoginActivity.this, StartActivity.class);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        startCycleSpinnerLayoutView = findViewById(R.id.start_cycle_spinner_layout);
        startCycleSpinnerView = findViewById(R.id.start_cycle_spinner);

        currentUserBool = getIntent().getStringExtra("sign out");
        //Toast.makeText(LoginActivity.this, "this is the login activity",
        //Toast.LENGTH_SHORT).show();
        if (currentUserBool != null){
            if (currentUserBool.equals("signed out" )){
                setContentView(R.layout.activity_login);
                mAuth.signOut();
                currentUserBool = "signed in";
                //Toast.makeText(LoginActivity.this, "testing", Toast.LENGTH_SHORT).show();
            }
            else {
                onStart();
            }
        }

        mEmailView = (EditText) findViewById(R.id.email);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void storeUserID(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("user ID", string);
        mEditor.apply();
    }

    private String getScreen() {
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        String selectedScreen = mSharedPreferences.getString("user ID", "none");
        return selectedScreen;
    }

    private void storeLoginAttempts(Integer integer) {
        SharedPreferences mSharedPreferences = getSharedPreferences("LoginAttempts", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("login attempts", integer);
        mEditor.apply();
    }

    private Integer getLoginAttempts() {
        SharedPreferences mSharedPreferences = getSharedPreferences("LoginAttempts", MODE_PRIVATE);
        Integer loginAttempts = mSharedPreferences.getInt("login attempts",0);
        return loginAttempts;
    }

    private void attemptLogin() {
        //Toast.makeText(LoginActivity.this, "calling attemptLogin()", Toast.LENGTH_SHORT).show();
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString() + "@gmail.com";
        username = mEmailView.getText().toString();
        storeUserID(username);
        Log.e("username",username);
        //Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
        //String password = mPasswordView.getText().toString();
        String password = "hello123";
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        //mProgressView.setVisibility(View.VISIBLE);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

       /* if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            //mProgressView.setVisibility(View.GONE);
            progressDialog.dismiss();
            return;
        }*/

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //mProgressView.setVisibility(View.GONE);
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            currentUserBool = "signed in";
                            UserID = task.getResult().getUser().getUid();
                            storeUserID(username);
                            loginAttempts=getLoginAttempts();
//                            loginAttempts++;
                            loginAttempts=1;
                            storeLoginAttempts(loginAttempts);
                            Log.e("Login Attempts", loginAttempts+"");
                            Log.e("User ID Login", username);
                            intent.putExtra("User ID", username);
                            intent.putExtra("Sign in Boolean", currentUserBool);

                            storeUserGroupFromDBToSharedPref();

                            // Option to login to a specific cycle
                            String cycleStartNumStr = String.valueOf(startCycleSpinnerView.getSelectedItem());
                            int cycleStartNum = Integer.parseInt(cycleStartNumStr);
                            storeStartCycleNum(cycleStartNum);

                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private List<String> getUserListByGroup(DataSnapshot dataSnapshot, String s) {
        List<String> userList = new ArrayList<>();
        //iterates through the dataSnapshot
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String usersKey = ds.getKey().toString();
            if (usersKey.equals("Users")) {
                //gets information from database
                //makes sure that there is data at the given branch
                Map<String, Map<String, String>> controlJSON = (Map<String, Map<String, String>>) ds.child(s).getValue();
                if (controlJSON != null) {
                    userList = new ArrayList<>(controlJSON.keySet());
                }
            }
        }
        return userList;
    }

    private void storeGroup(String group) {
        SharedPreferences mSharedPreferences = getSharedPreferences("Group", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("Group", group);
        mEditor.apply();
    }

    private void storeStartCycleNum(int startCycleNum) {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("startCycleNum", startCycleNum);
        mEditor.apply();
    }

    // check user's group from database and store in SharedPreferences
    private void storeUserGroupFromDBToSharedPref() {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String group = "";
                List<String> controlUserList = getUserListByGroup(dataSnapshot, "Control Group");
                List<String> experimentalUserList = getUserListByGroup(dataSnapshot, "Experimental Group");

                for (int i = 0; i < experimentalUserList.size(); i++) {
                    if (experimentalUserList.get(i).equals(username)) {
                        group = "experimental";
                        break;
                    }
                }

                for (int i = 0; i < controlUserList.size(); i++) {
                    if (controlUserList.get(i).equals(username)) {
                        group = "control";
                        break;
                    }
                }
                storeGroup(group);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
/*
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
*/
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        //  addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_new_session:
                if (checked) {
                    startCycleSpinnerLayoutView.setVisibility(View.GONE);
                }
                break;
            case R.id.radio_old_session:
                if (checked) {
                    startCycleSpinnerLayoutView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    /*private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mEmailView.setAdapter(adapter);
    }*/


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        //Toast.makeText(LoginActivity.this, "testing onStart", Toast.LENGTH_SHORT).show();
        currentUser = mAuth.getInstance().getCurrentUser();

        if (currentUser != null){
            //currentUserBool = "signed in";
            UserID = currentUser.getUid();
            username = getScreen();
            //Toast.makeText(getApplicationContext(), UserID,
            //        Toast.LENGTH_SHORT).show();
            intent.putExtra("User ID", username);
            //intent.putExtra("Sign in Boolean", currentUser);
            Log.e("username2",username);
            storeUserID(username);
            startActivity(intent);
            finish();

        }
        else {
            //currentUserBool = "signed out";
            //Toast.makeText(getApplicationContext(), "User is signed out.",
            //        Toast.LENGTH_SHORT).show();
        }
    }



}