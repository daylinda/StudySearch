package com.example.groupprojectapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// @author Davina
public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText useremail;
    EditText password;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    String userName,userEmail,userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth= FirebaseAuth.getInstance();

        username = (EditText) findViewById(R.id.text_register_username);
        useremail= (EditText) findViewById(R.id.text_register_email);
        password = (EditText) findViewById(R.id.text_register_password);

        Button submitButton = findViewById(R.id.button_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = (String) username.getText().toString();
                userEmail = (String) useremail.getText().toString();
                userPassword = (String) password.getText().toString();

                //check if the input is right
                if(userName.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Empty UserName", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userEmail.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Empty email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userPassword.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Empty password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(userPassword.length()<8){
                    Toast.makeText(RegisterActivity.this, "Password length must not be less than 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                String regex = "^(.+)@(.+)$";
                //Compile regular expression to get the pattern
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(userEmail);
                if(!matcher.matches()){
                    Toast.makeText(RegisterActivity.this, "Enter valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    setUser();

                                } else {
                                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                    Toast.makeText(RegisterActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                    System.out.println(e.getMessage());
                                }
                            }
                        });


            }
        });


    }

    private void setUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        String uID = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);

        reference.setValue("");


        HashMap<String,String> userMap = new HashMap<>();
        userMap.put("id",uID);
        userMap.put("username",userName);
        userMap.put("useremail",userEmail);
        userMap.put("imageURL","default");

        reference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User added",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });

    }
}