package com.younsukkoh.miscellaneous.mychat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // Used for saving current user
    private SharedPreferences mSp;
    // Current user logged in
    private String mUser;
    // List of all the messages
    private RecyclerView mRecyclerView;
    // Adapter for the list
    private FirebaseRecyclerAdapter mMessagesAdapter;
    // Input field for the message
    private EditText mMessageInput;
    // Button that will send the message from input
    private Button mSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Data node containing all the messages
        final DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference(Utility.MESSAGES);

        setUpSharedPreferences();
        setUpRecyclerView(messagesRef);
        setUpUI(messagesRef);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ma_logout:
                // Creates dialog to confirm log out
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Warning!")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Reset logged in user information
                                SharedPreferences.Editor editor = mSp.edit();
                                editor.putString(Utility.SP_USER, "");
                                editor.putBoolean(Utility.SP_LOGGED_IN, false);
                                editor.commit();

                                // Take the user back to login page after logging out
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);

                                Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;
            case R.id.menu_ma_clear:
                // Creates dialog to confirm deleting all previous conversation
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Warning!")
                        .setMessage("Are you sure you want to clear all previous conversation?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Delete data
                                DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference(Utility.MESSAGES);
                                messagesRef.removeValue();

                                Toast.makeText(MainActivity.this, "All Gone Now!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Set up shared preference used for retrieving user information
     */
    private void setUpSharedPreferences() {
        mSp = getSharedPreferences(Utility.SP, Context.MODE_PRIVATE);
        mUser = mSp.getString(Utility.SP_USER, "");
    }

    /**
     * Set up user interface data node containing all the messages
     * @param messagesRef
     */
    private void setUpUI(final DatabaseReference messagesRef) {
        // Put the user name on the top
        getSupportActionBar().setTitle(mUser);

        mMessageInput = (EditText) findViewById(R.id.ma_et_message);

        mSend = (Button) findViewById(R.id.ma_b_send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Needs to be outside of the listener, otherwise returns null
                final String input = mMessageInput.getText().toString();
                // Add listener for retrieving and adding new message
                messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long timeSent = new Date().getTime();
                        Message message = new Message(mUser, input, timeSent);
                        // Add a new message to the list
                        messagesRef.child(timeSent + "").setValue(message);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(Utility.DEBUG, databaseError.getMessage());
                    }
                });
                // After typing the message, input text will go blank
                mMessageInput.setText("");
            }
        });
    }

    /**
     * Set up recycler view for seeing all the messages
     * @param messagesRef
     */
    private void setUpRecyclerView(DatabaseReference messagesRef) {
        mRecyclerView = (RecyclerView) findViewById(R.id.ma_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessagesAdapter = new MessagesAdapter(Message.class, R.layout.message_view_holder, MessageViewHolder.class, messagesRef);
        mRecyclerView.setAdapter(mMessagesAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessagesAdapter.cleanup();
    }

}
