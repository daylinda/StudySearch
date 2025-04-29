package com.example.groupprojectapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.groupprojectapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
// @author Davina
public class MainActivity extends AppCompatActivity {

    Button login;
    Button register;
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(MainActivity.this,MainPageActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is the code which we used to slam data into firebase, left here for analysis
//        System.out.println("Starting ldm");
//        try{
//            BufferedReader bufferedReader;
//            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("localTopics.csv"), StandardCharsets.UTF_8));
//            LocalDataManager ldm = new LocalDataManager();
//            ldm.loadData(bufferedReader);
//            System.out.println("Finishing ldm");
//        }catch (Exception e){
//            System.out.println("error");
//            System.out.println(e.getMessage());
//        }



        //set view items
        login = findViewById(R.id.btn_main_login);
        register = findViewById(R.id.btn_main_register);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //move to login activity
                Intent loginActivity = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //move to register activity
                Intent registerActivity = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(registerActivity);
            }
        });




    }
}