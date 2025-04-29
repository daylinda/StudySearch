package com.example.groupprojectapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.groupprojectapplication.model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
// @author Davina
public class GroupChatActivity extends AppCompatActivity {

    private ImageButton SendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessage,textTitle;

    private String currentGroupKey,currentUserID,currentUserName,currentDate,currentTime,groupName;

    private FirebaseAuth mAuth;

    private DatabaseReference UsersRef,GroupNameRef,MessageRef,GroupMessageKeyRef;

    @Override
    protected void onStart() {
        super.onStart();

        MessageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    System.out.println("When child added:"+dataSnapshot.getKey());

                    DisplayMessages(dataSnapshot);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    System.out.println("When child changes:"+dataSnapshot.getKey());

                    DisplayMessages(dataSnapshot);
                }

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
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        //get intent extras
        currentGroupKey = getIntent().getExtras().get("groupKey").toString();
        groupName =getIntent().getExtras().get("groupName").toString();
        System.out.println("groupname::"+groupName);

        //firebase references
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupKey);
        MessageRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupKey).child("Messages");

        //set items from view
        SendMessageButton = (ImageButton)findViewById(R.id.send_message_button);
        userMessageInput = (EditText)findViewById(R.id.input_group_message);
        mScrollView = (ScrollView)findViewById(R.id.my_scroll_view);
        displayTextMessage = (TextView)findViewById(R.id.group_chat_text_display);
        textTitle = findViewById(R.id.text_group_title);
        textTitle.setText(groupName);

        //get username details
        getUserDetails();

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMessageInfoToDatabase();
                userMessageInput.setText("");
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void getUserDetails() {
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("username").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SaveMessageInfoToDatabase() {

        String message = userMessageInput.getText().toString();
        String messagekEY = MessageRef.push().getKey();

        if(TextUtils.isEmpty(message)){
            Toast.makeText(this, "Please write message first", Toast.LENGTH_SHORT).show();
        }else{
            //get the current date
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd,yyyy");
            currentDate = currentDateFormat.format(calForDate.getTime());

            //get current time
            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calForTime.getTime());

            //send message to firebase
            GroupMessageKeyRef = MessageRef.child(messagekEY);
            Message message1 = new Message(messagekEY,message,currentUserName,currentDate,currentTime);
            GroupMessageKeyRef.setValue(message1).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           Toast.makeText(GroupChatActivity.this,"Message sent",Toast.LENGTH_SHORT).show();
                       }
                }
            );
        }
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Message nextMessage =dataSnapshot.getValue(Message.class);
        System.out.println("Message::"+nextMessage.getMessage());
        displayTextMessage.append(nextMessage.getUserName() + ":\n" + nextMessage.getMessage() +"\n"+nextMessage.getTime()+"\t"+nextMessage.getDate()+"\n\n\n");
        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

}