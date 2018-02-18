package com.spritegames.highlowgame.highlowgame.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spritegames.highlowgame.highlowgame.Data.Users;
import com.spritegames.highlowgame.highlowgame.R;

import java.util.HashMap;
import java.util.Map;

import static com.spritegames.highlowgame.highlowgame.Activities.MainGameActivity.userTestInt;
import static com.spritegames.highlowgame.highlowgame.Activities.MainGameActivity.userTestTier;

public class SignUpActivity extends AppCompatActivity
{
        private TextView back;
        private Button btn;
        private EditText emailText, passwordText;
        private FirebaseAuth mAuth;
        private Context context;
        private ProgressDialog progressDialog;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        btn = findViewById(R.id.submit);
        emailText = findViewById(R.id.email_signup);
        passwordText = findViewById(R.id.password_signup);
        context = getApplicationContext();
        progressDialog = new ProgressDialog(this);

        //database ref
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerUser()
    {
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();



        try
        {
            if(email.isEmpty() == false && password.isEmpty() == false)
            {
                progressDialog.show();
                progressDialog.setMessage("Creating account...");

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener
                        (this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
//                                    databaseReference.push().setValue(email);
//                                    databaseReference.push().setValue(password);

                                    String userid = mAuth.getCurrentUser().getUid();
                                    DatabaseReference currentUserDb = databaseReference.child(userid);

                                    Users users = new Users();

                                    currentUserDb.child("Turns ").setValue(userTestInt);
                                    currentUserDb.child("Tier ").setValue(userTestTier);

                                    emailText.setText("");
                                    passwordText.setText("");

                                    progressDialog.dismiss();
                                    Toast.makeText(context,"Account Created!",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else
                                {
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(context,"You are already registered!",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(context,"There was a problem!",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(context,"There was a problem!",Toast.LENGTH_SHORT).show();
                    }
                });

            }else if(email.isEmpty() == true && password.isEmpty() == true)
            {
                Toast.makeText(context,"Both fields must be completed.",Toast.LENGTH_SHORT).show();

            }
        }catch (Exception e)
        {

        }
    }//end of method
}
