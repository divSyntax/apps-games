package com.spritegames.highlowgame.highlowgame.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spritegames.highlowgame.highlowgame.R;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class LoginActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthList;
    private FirebaseUser curreentUser;
    private Button loginBtn;
    private EditText emailTxt, passwordTxt;
    private Context context;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.log_in);
        emailTxt = findViewById(R.id.email);
        passwordTxt = findViewById(R.id.password);
        context = getApplicationContext();
        progressDialog = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(!TextUtils.isEmpty(emailTxt.getText().toString()) && !TextUtils.isEmpty(passwordTxt.getText().toString()))
                {
                    String email = emailTxt.getText().toString().trim();
                    String password = passwordTxt.getText().toString().trim();


                    loginUser(email,password);
                }
            }
        });


        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        //keep user signed in
        mAuthList = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    curreentUser = firebaseAuth.getCurrentUser();

                    if(curreentUser != null)
                    {
                        Toast.makeText(LoginActivity.this,"Signed in.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(new Intent(LoginActivity.this,MainGameActivity.class));
                        finish();

                    }else
                    {
                        Toast.makeText(LoginActivity.this,"Not signed in.",Toast.LENGTH_SHORT).show();
                    }
            }
        };
    }

    private void loginUser(String email, String password)
    {

         email = emailTxt.getText().toString().trim();
         password = passwordTxt.getText().toString().trim();

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        try
        {
            if(TextUtils.isEmpty(email) == false && TextUtils.isEmpty(password) == false) {


                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Logged in.", Toast.LENGTH_SHORT).show();

                            emailTxt.setText("");
                            passwordTxt.setText("");

                             Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(new Intent(LoginActivity.this,MainGameActivity.class));

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Login failed.", Toast.LENGTH_SHORT).show();
                       }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Email or password is incorrect.",Toast.LENGTH_SHORT).show();

                    }
                });
            } if(TextUtils.isEmpty(email) == true && TextUtils.isEmpty(password) == true)
            {
                progressDialog.dismiss();
                Toast.makeText(context,"Both fields must be completed.",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e)
        {

        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthList);


    }
}
