package com.example.groupprojectapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// @author Davina
public class LoginActivity extends AppCompatActivity {

    EditText useremail;
    EditText password;

    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get user instance
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //set items from view
        useremail = (EditText) findViewById(R.id.text_login_email);
        password = (EditText) findViewById(R.id.text_login_password);
        submitButton = findViewById(R.id.btn_login_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = (String) useremail.getText().toString();
                String userPassword = (String) password.getText().toString();

                //check if user email and password is empty
                if(userEmail.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Empty email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Empty password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(LoginActivity.this, "login works.",
                                            Toast.LENGTH_SHORT).show();

                                    //move to main page activity
                                    Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                                    intent.putExtra("USER", user);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "login failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}