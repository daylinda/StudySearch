package com.example.groupprojectapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.groupprojectapplication.model.Group;
import com.example.groupprojectapplication.model.Message;
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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//@author Davina
public class AddNewsActivity extends AppCompatActivity {
    EditText topic;
    EditText time;

    Button submitBtn;

    Integer day_int;

    FirebaseUser firebaseUser;
    DatabaseReference reference,groupsRef,userRef,usersRef,messageRef;
    User user;

    long maxId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        topic = findViewById(R.id.topic_input);
        time = findViewById(R.id.time_input);
        submitBtn = findViewById(R.id.add_submit);

        //for days input drop down spinner
        Spinner dropdown = findViewById(R.id.spinner_day);
        String[] items = new String[]{"Monday", "Tuesday", "Wednesday","Thrusday","Friday","Saturday","Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day_int = i;
                System.out.println("for Monday"+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // firebase references
        reference = FirebaseDatabase.getInstance().getReference().child("News");
        groupsRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        //retrieve user details
        getUserDetails();

        //retrieve news ID
        getMaxId();

        //on click on btn, create news with maxId+1
        setSubmitBtn();






    }

    private void setSubmitBtn() {
        //on submit button clicked
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If the time is empty
                // return false
                if (time.getText().toString().isEmpty()) {
                    System.out.println("Empty time");
                    Toast.makeText(AddNewsActivity.this,"Enter time",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(topic.getText().toString().isEmpty()){
                    System.out.println("Empty topic");
                    Toast.makeText(AddNewsActivity.this,"Enter topic",Toast.LENGTH_SHORT).show();
                    return;
                }

                // Regex to check valid time in 24-hour format.
                String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
                // Compile the ReGex
                Pattern p = Pattern.compile(regex);

                // Pattern class contains matcher() method
                // to find matching between given time
                // and regular expression.
                Matcher m = p.matcher(time.getText().toString());

                if(!m.matches()){
                    System.out.println("Doesnt match");
                    Toast.makeText(AddNewsActivity.this,"Enter time in 24 hour format",Toast.LENGTH_SHORT).show();
                    return;
                }

                //increment the news ID
                maxId++;

                //create news object in firebase
                createNews();
            }
        });
    }

    private void createNews() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String sendtime = new ConvertTimeDay().convertToTime(time.getText().toString(),day_int.toString());
        //Create news object
        News news = new News(Long.toString(maxId),topic.getText().toString(),sendtime, userId,"0");
        reference.child(Long.toString(maxId)).setValue(news).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    userRef.child("createdtopics").child(topic.getText().toString()).setValue(topic.getText().toString());
                    createGroup();
                }
            }
        });
    }

    private void createGroup() {
        //group object is created using topic text set as group name
        Group group = new Group();
        group.setGroupName(topic.getText().toString());

        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        group.setUsers(users);

        System.out.println(user.getUsername());

        groupsRef.child(Long.toString(maxId)).child("id").setValue(Long.toString(maxId));

        groupsRef.child(Long.toString(maxId)).child("groupName").setValue(topic.getText().toString()).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              if (task.isSuccessful()){
                                                  //add user to group
                                                  addUserToGroup();

                                              }
                                          }
                                      }
                );
    }

    private void addUserToGroup() {
        userRef = groupsRef.child(Long.toString(maxId)).child("Users").child(user.getId());
        userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(AddNewsActivity.this,MainPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void getMaxId() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserDetails() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}