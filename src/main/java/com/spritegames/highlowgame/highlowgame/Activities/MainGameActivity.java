package com.spritegames.highlowgame.highlowgame.Activities;

import android.app.Application;
import android.content.Intent;
import android.icu.lang.UCharacter;
import android.os.Debug;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.spritegames.highlowgame.highlowgame.Data.Users;
import com.spritegames.highlowgame.highlowgame.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainGameActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myDb;
    TextView computNum, turnstxt,textGuess,tierText;
    Random ran = new Random();
    Button sub;
    EditText Input;
    int x = ran.nextInt(10);
    String myInput;
    FirebaseUser currentUser;

    public static int userTestInt = 3;
    public static int userTestTier = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.myTheme);
        setContentView(R.layout.activity_main_game);

        mAuth = FirebaseAuth.getInstance();
        computNum = findViewById(R.id.computerNum);
        sub = findViewById(R.id.check);
        Input = findViewById(R.id.input);
        turnstxt = findViewById(R.id.turnNum);
        textGuess = findViewById(R.id.guessTxt);
        tierText = findViewById(R.id.tierNum);
        currentUser = mAuth.getCurrentUser();





        Users user = new Users();


        final String userid = mAuth.getCurrentUser().getUid();


        firebaseDatabase = FirebaseDatabase.getInstance();
        myDb = firebaseDatabase.getReference().child("Users");


        myDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                computNum.setText("Num " + x);
                turnstxt.setText("Turns " + Integer.toString(userTestInt));
                tierText.setText("Tier " + Integer.toString(userTestTier));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
              upDateScore();
            }
        });


        Input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0)
                {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                 Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(new Intent(MainGameActivity.this,LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        userTestInt = userTestInt;
        userTestTier = userTestTier;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onStart() {
        super.onStart();
//        computNum.setText("Num " + x);
//        turnstxt.setText("Turns " + Integer.toString(userTestInt));
//        tierText.setText("Tier " + Integer.toString(userTestTier));
    }


    private void upDateScore()
    {
        String userid = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = myDb.child(userid);

         myInput = Input.getText().toString().trim();
        Users user = new Users();

        try
        {
            if(Integer.parseInt(myInput) == x)
            {
                textGuess.setText("Awesome, you got it!");
                textGuess.getVisibility();
                turnstxt.setText("Turns " + Integer.toString(userTestInt));
                Input.setText("");
            }

             if(Integer.parseInt(myInput) < x)
            {
                userTestInt -= 1;
                textGuess.setText("Guess higher!");
                Input.setText("");

                currentUserDb.child("Turns").setValue(userTestInt);

                turnstxt.setText("Turns " + Integer.toString(userTestInt));


            }

             if(Integer.parseInt(myInput) > x)
            {
                userTestInt -= 1;
                textGuess.setText("Guess lower!");
                Input.setText("");

                currentUserDb.child("Turns").setValue(userTestInt);

                turnstxt.setText("Turns " + Integer.toString(userTestInt));

            }

            if(userTestInt == 0)
            {
                Input.setFocusable(false);
                textGuess.setText("Sorry, you are out of turns!");
                Input.setText("");
                turnstxt.setText("Turns " + Integer.toString(userTestInt));
            }

        }catch (NumberFormatException e)
        {

        }
    }

}//
