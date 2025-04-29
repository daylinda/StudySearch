package com.example.groupprojectapplication;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.groupprojectapplication.model.Group;
import com.example.groupprojectapplication.model.News;
import com.example.groupprojectapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


// @author Jack
public class LocalDataManager {
    User user;
    long maxId = -1;
    DatabaseReference reference,groupsRef,userRef,userGroupRef;

    ArrayList<ArrayList<String>> linelist;
    int counter = 0;
    boolean goNew=true;

    // This file is used to upload all the local data to the firebase. it is not used in the regualr running of the app
    public void loadData(BufferedReader bufferedReader){

        System.out.println("Inside Load Data");
        linelist = new ArrayList<>();

        try{
            reference = FirebaseDatabase.getInstance().getReference().child("News");
            groupsRef = FirebaseDatabase.getInstance().getReference().child("Groups");

            String line;
            int skipfirst = 0;
            while ((line = bufferedReader.readLine()) != null){
                if (skipfirst==0){
                    skipfirst=1;
                    continue;
                }

                String[] tokens = line.split(",");
                System.out.println(line);
                ArrayList<String> entry = new ArrayList<>();
                entry.add(tokens[0]);
                entry.add(tokens[1]);
                entry.add(tokens[2]);
                linelist.add(entry);


            }
            bufferedReader.close();
            user = new User(linelist.get(counter).get(2), "jack", "brown@gmail.com", "default");
            getUserDetails(linelist.get(counter).get(2));
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    private void createNews(String topicName, String time, String userId){
        System.out.println("Starting to create News");
        goNew=false;

        getMaxId();
        getUserDetails(userId);

        System.out.println(user);
        System.out.println(maxId);

        News news = new News(Long.toString(maxId),topicName,time, userId,"0");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        reference.child(Long.toString(maxId)).setValue(news).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    System.out.println("added News");
                    userRef.child("createdtopics").child(topicName).setValue(topicName);
                    createGroup(topicName);

                }
            }

        });


    }
    private void createGroup(String topicName){
        System.out.println("Starting Create Group");
        groupsRef.child(Long.toString(maxId)).child("id").setValue(Long.toString(maxId));
        groupsRef.child(Long.toString(maxId)).child("groupName").setValue(topicName).
            addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        System.out.println("created group");
                        addUserToGroup();
                    }
                }
            }
            );

    }
    private void addUserToGroup() {
        userGroupRef = groupsRef.child(Long.toString(maxId)).child("Users").child(user.getId());
        userGroupRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                counter++;
                System.out.println("Happy");
                goNew=true;
                getUserDetails(linelist.get(counter).get(2));
            }
        });

    }

    public void getMaxId() {
        System.out.println("Starting Get Max ID");
        reference = FirebaseDatabase.getInstance().getReference().child("News");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    System.out.println("Max id changed");
                    maxId = snapshot.getChildrenCount() + 1;
                    System.out.println(maxId);
                    if (goNew) {
                        createNews(linelist.get(counter).get(0), linelist.get(counter).get(1), linelist.get(counter).get(2));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Max id change cancled");

            }
        });
    }

    public void getUserDetails(String userId) {
        System.out.println("Starting get user details");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("User changed!");
                user.setId(snapshot.child("id").getValue(String.class));
                user.setUseremail(snapshot.child("useremail").getValue(String.class));
                user.setUsername(snapshot.child("username").getValue(String.class));
                getMaxId();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void main(String[] args) {


//        String input = "(DAY):Wed,Mon;(TIME)<200;(TIME)>150;(TAGS):barry,mark,john";
//        SearchTokeniser tokenizer = new SearchTokeniser(input);
//
//        // Print out the expression from the parser.
//        SearchParser parser = new SearchParser(tokenizer);
//        parser.parseSearch();
//        System.out.println("Parsing: " + Condition.getInstance().show());
//
//        ArrayList<News> filteredList = Condition.getInstance().Filter(root);
//        for (News t : filteredList)
//            System.out.println(t.show());
    }
}

