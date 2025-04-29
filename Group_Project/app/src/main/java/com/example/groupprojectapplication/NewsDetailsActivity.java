package com.example.groupprojectapplication;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupprojectapplication.model.News;
import com.example.groupprojectapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
// @author Davina
public class NewsDetailsActivity extends AppCompatActivity {
    TextView topic,time,day,userName,userEmail;

    ImageButton joinBtn,likeBtn,chatBtn;
    User user,currentUser;
    News newsExtra;

    ArrayList<String> likedTopics = new ArrayList<>();

    ArrayList<User> usersInGroup = new ArrayList<>();

    DatabaseReference groupRef,newsLikesRef,userRef,chatRequestRef,contactsRef;

    ArrayList<String> userContacts = new ArrayList<>();

    ArrayList<String> userRequests = new ArrayList<>();

    long likesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        // get intent extras
        News newsTemp = getIntent().getExtras().getSerializable("currentNews",News.class);
        newsExtra = new News(newsTemp.getId(),newsTemp.getTopic(),newsTemp.getTime(),newsTemp.getCreatedUserId(), newsTemp.getLikes());
        if(newsExtra.getLikes() == null || newsExtra.getLikes().isEmpty()){
            likesCount =0;
        }else {
            likesCount = Integer.parseInt(newsExtra.getLikes());
        }



        //printing out the extras
        System.out.println(newsExtra);
        System.out.println(newsExtra.getTopic());
        System.out.println(newsExtra.getTime());
        System.out.println(newsExtra.getLikes());

        //firebase references
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(newsExtra.getId());
        newsLikesRef = FirebaseDatabase.getInstance().getReference().child("News").child(newsExtra.getId()).child("likes");
        chatRequestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");

        //set view items
        topic = findViewById(R.id.topic_view_input);
        time = findViewById(R.id.time_view_input);
        day = findViewById(R.id.day_view_input);
        userName = findViewById(R.id.text_username);
        userEmail = findViewById(R.id.text_useremail);
        joinBtn = findViewById(R.id.group_join_btn);
        likeBtn = findViewById(R.id.like_btn);
        chatBtn = findViewById(R.id.chat_user_btn);


        getUserDetails();

        getUsersInGroups();




        //set values from database
        topic.setText(newsExtra.getTopic().toString());
        time.setText(new ConvertTimeDay().getTime(newsExtra.getTime()));
        day.setText(new ConvertTimeDay().getDay(newsExtra.getTime()));


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewsDetailsActivity.this, "join works.",
                        Toast.LENGTH_SHORT).show();
                if(currentUser.getId()!= newsExtra.getCreatedUserId()){
                    AddUserToGroup();
                }
            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewsDetailsActivity.this, "like works.",
                        Toast.LENGTH_SHORT).show();
                incrementLikes();
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(userContacts.contains(user.getId())){
                    Toast.makeText(NewsDetailsActivity.this, "chat works.",
                            Toast.LENGTH_SHORT).show();
                    movetoChatActivity();
                }else{

                    if(userRequests.contains(user.getId())){
                        Toast.makeText(NewsDetailsActivity.this, "Chat request already exists",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(NewsDetailsActivity.this, "Chat request sent",
                                Toast.LENGTH_SHORT).show();
                        SendChatRequest();
                    }

                }

            }
        });

    }

    private void getUserRequests() {
        chatRequestRef.child(currentUser.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()
                     ) {
                    userRequests.add(ds.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void movetoChatActivity() {
        contactsRef.child(currentUser.getId()).child(user.getId()).child("ChatKey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent chatIntent = new Intent(NewsDetailsActivity.this, ChatActivity.class);
                chatIntent.putExtra("ChatUser",user);
                chatIntent.putExtra("ChatKey",snapshot.getValue(String.class));
                startActivity(chatIntent);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUserContacts() {

        contactsRef.child(currentUser.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&& snapshot.hasChildren()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String contactId = ds.getKey().toString();
                        userContacts.add(contactId);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void incrementLikes() {

        if(!likedTopics.contains(newsExtra.getTopic())){
            likesCount++;
            newsLikesRef.setValue(likesCount+"");
            userRef.child(currentUser.getId()).child("likedtopics").child("topic").setValue(newsExtra.getTopic());
        }else{


            Toast.makeText(NewsDetailsActivity.this,"Already Liked",Toast.LENGTH_SHORT).show();
        }


    }

    private void getUsersInGroups() {
        groupRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersInGroup.clear();
                for (DataSnapshot ds: snapshot.getChildren()
                     ) {
                    User groupUser = ds.getValue(User.class);
                    usersInGroup.add(groupUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserDetails() {
        userRef.child(newsExtra.getCreatedUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    user = dataSnapshot.getValue(User.class);
                    System.out.println(user.getUsername());
                    System.out.println(user.getUseremail());
                    userName.setText(user.getUsername());
                    userEmail.setText(user.getUseremail());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currentUser = snapshot.getValue(User.class);
                        //if logged in user is same as user who created the study group then make buttons invisible
                        if(currentUser.getId().equals(newsExtra.getCreatedUserId())){
                            System.out.println("The current user = user who has created");
                            joinBtn.setActivated(false);
                            joinBtn.setVisibility(View.GONE);
                            likeBtn.setActivated(false);
                            likeBtn.setEnabled(false);
                            likeBtn.setVisibility(View.GONE);
                            chatBtn.setActivated(false);
                            chatBtn.setEnabled(false);
                            chatBtn.setVisibility(View.GONE);
                        }
                        //retrive contacts from user
                        getUserContacts();
                        getUserRequests();

                        DataSnapshot ds = snapshot.child("likedtopics");
                        if(ds.exists()){
                            for (DataSnapshot d:ds.getChildren()
                            ) {
                                String topicName = d.getValue().toString();
                                likedTopics.add(topicName);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    private void AddUserToGroup() {

        if(!usersInGroup.contains(currentUser)){

            groupRef.child("Users").child(currentUser.getId()).setValue(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(NewsDetailsActivity.this, "user added to list",
                            Toast.LENGTH_SHORT).show();
                    usersInGroup.add(currentUser);
                    movetoGroupActivity();
                }
            });
        }else {
            System.out.println("Check if user is in list");
            movetoGroupActivity();
        }
    }

    private void movetoGroupActivity() {
        Intent groupChatIntent = new Intent(NewsDetailsActivity.this, GroupChatActivity.class);
        groupChatIntent.putExtra("groupKey",newsExtra.getId());
        groupChatIntent.putExtra("groupName",newsExtra.getTopic());
        startActivity(groupChatIntent);
    }

    private void SendChatRequest() {
        chatRequestRef.child(currentUser.getId()).child(user.getId())
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            chatRequestRef.child(user.getId())
                                    .child(currentUser.getId())
                                    .child("request_type")
                                    .setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                chatBtn.setEnabled(false);
                                            }

                                        }
                                    });

                        }

                    }
                });
    }
}