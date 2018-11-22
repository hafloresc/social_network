package com.example.speakliz.social_network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private ListView mMessageListView;
   // private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    private String Username;
    private FirebaseAuth Auth;
    private static final String anonimous="anonimo";
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mMessagesDatabaseReference;
    private String mUsername;
    private ChildEventListener mChildEvenlistener;
    private TextView prueba;
    ArrayList<String> messages = new ArrayList<String>();
    private ListView lista;
    private ArrayAdapter<String> adaptador1;



    RelativeLayout activity_login;
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.menu_sign_out)
//        {
//
//
////            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
////                @Override
////                public void onComplete(@NonNull Task<Void> task) {
////                    Snackbar.make(activity_login,"You have been signed out.", Snackbar.LENGTH_SHORT).show();
////                    finish();
////                }
////            });
//        }
//        return true;
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mFirebaseDatabse =FirebaseDatabase.getInstance();
        mMessagesDatabaseReference=mFirebaseDatabse.getReference().child("messages");
        mSendButton=(Button)findViewById(R.id.sendButton);
        mMessageEditText=(EditText)findViewById(R.id.messageEditText);
        mMessageListView=(ListView)findViewById(R.id.list_ms);
        prueba=(TextView)findViewById(R.id.textViewPrueba);



        Auth=FirebaseAuth.getInstance();

        Username=Auth.getCurrentUser().getEmail().toString();
        prueba.setText(Username);




        //Username=Auth.getCurrentUser().getEmail().toString();
        //prueba.setText(Username);



        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                //FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, null);
                String ms=mMessageEditText.getText().toString();
                messages.add(ms);
                adaptador1=new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1,messages);
                mMessageListView.setAdapter(adaptador1);
                prueba.setText(messages.toString());

                // Clear input box
                mMessageEditText.setText("");
                mMessagesDatabaseReference.push().setValue(messages);



            }
        });

        mChildEvenlistener =new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               //FriendlyMessage friendlyMessage= dataSnapshot.getValue(FriendlyMessage.class);
               //mMessageAdapter.add(friendlyMessage);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mMessagesDatabaseReference.addChildEventListener(mChildEvenlistener);


    }





}
