package com.younsukkoh.miscellaneous.mychat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Younsuk on 3/5/2017.
 */

public class LoginActivity extends AppCompatActivity{

    // Buttons for logging in
    private Button mAzra, mPeter, mYounsuk;
    // User who is logging in
    private String mUser;
    // Used for saving information
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSp = getSharedPreferences(Utility.SP, Context.MODE_PRIVATE);
        // Check if a user has already logged in
        boolean loggedIn = mSp.getBoolean(Utility.SP_LOGGED_IN, false);

        if (loggedIn) {
            // Check which user logged in
            mUser = mSp.getString(Utility.SP_USER, Utility.YOUNSUK);
            // Go to the chat as the user
            goToMainActivity(mUser);
        }

        setContentView(R.layout.login_activity);

        mAzra = (Button) findViewById(R.id.la_b_azra_ahmad);
        mAzra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(Utility.AZRA);
            }
        });

        mPeter = (Button) findViewById(R.id.la_b_sean_peterkin);
        mPeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(Utility.SEAN);
            }
        });

        mYounsuk = (Button) findViewById(R.id.la_b_younsuk_koh);
        mYounsuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(Utility.YOUNSUK);
            }
        });
    }

    /**
     * Save user information to skip later login and take the user to chat
     * @param user
     */
    private void login(final String user) {
        // Input field for entering password
        final EditText password = new EditText(LoginActivity.this);
        password.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        // Creates dialog to check the user for password
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Password")
                .setMessage("Enter Password")
                .setView(password)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String input = password.getText().toString();

                        Log.i(Utility.DEBUG, input + " " + Utility.PASSWORD);

                        if (input.equals(Utility.PASSWORD)) {
                            SharedPreferences.Editor editor = mSp.edit();
                            editor.putBoolean(Utility.SP_LOGGED_IN, true);
                            editor.putString(Utility.SP_USER, user);
                            editor.commit();

                            goToMainActivity(user);

                            Toast.makeText(LoginActivity.this, "Logged in as " + user, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    /**
     * Takes the user to chat
     * @param user
     */
    private void goToMainActivity(String user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
