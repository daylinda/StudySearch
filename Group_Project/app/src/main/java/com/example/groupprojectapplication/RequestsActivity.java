package com.example.groupprojectapplication;


import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupprojectapplication.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
// @author Davina
public class RequestsActivity extends AppCompatActivity {

    private RecyclerView myRequestsList;

    private DatabaseReference ChatRequestRef,UsersRef,ContactsRef;
    private FirebaseAuth mAuth;

    private String currentUserID;

    private String list_user_id;

    User listUser;

    ArrayList<String> requestSenderIds = new ArrayList<>();

    ArrayList<User> requestSenders = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        System.out.println("Inside RequestsActivity");

        //firebase references
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        System.out.println("Current User Id"+currentUserID);
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Requests").child(currentUserID);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        myRequestsList = (RecyclerView) findViewById(R.id.requests_list);

        RequestsAdapter requestsAdapter = new RequestsAdapter(requestSenders,this);

        ChatRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    requestSenders.clear();
                    for (DataSnapshot ds: snapshot.getChildren()
                         ) {
                        String requestSenderId = ds.getKey();
                        System.out.println(requestSenderId);
                        String requestType = ds.child("request_type").getValue(String.class);
                        System.out.println(requestType);
                        if(requestType.equals("received")){
                            System.out.println("received");
                            requestSenderIds.add(requestSenderId);
                            UsersRef.child(requestSenderId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    System.out.println("Check check");
                                    User newUser = snapshot.getValue(User.class);

                                    System.out.println("User added to list:"+newUser.getUsername());
                                    requestSenders.add(newUser);
                                    requestsAdapter.addData(requestSenders);
                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                }
                            });
                        }

                    }


                }
            }




            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });






        myRequestsList.setAdapter(requestsAdapter);
        myRequestsList.setLayoutManager(new LinearLayoutManager(this));


    }

    private void declineRequest() {
        ChatRequestRef.child(currentUserID).child(list_user_id)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            ChatRequestRef.child(list_user_id).child(currentUserID)
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                Toast.makeText(RequestsActivity.this, "Request declined: Request deleted", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                        }

                    }
                });
    }

    private void acceptRequest() {
        ContactsRef.child(currentUserID).child(list_user_id).child("Contact")
                .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            ContactsRef.child(list_user_id).child(currentUserID).child("Contact")
                                    .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(RequestsActivity.this, "Contacts Ref created for other user", Toast.LENGTH_SHORT).show();

                                                ChatRequestRef.child(currentUserID).child(list_user_id)
                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){

                                                                    ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()){

                                                                                        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference().child("PrivateChats");
                                                                                        String chatsMessageKey = chatsRef.push().getKey();
                                                                                        ContactsRef.child(currentUserID).child(list_user_id).child("ChatKey").setValue(chatsMessageKey);
                                                                                        ContactsRef.child(list_user_id).child(currentUserID).child("ChatKey").setValue(chatsMessageKey);
                                                                                        Intent intent = new Intent(RequestsActivity.this, ChatActivity.class);
                                                                                        intent.putExtra("ChatUser",listUser);
                                                                                        intent.putExtra("ChatKey",chatsMessageKey);
                                                                                        startActivity(intent);

                                                                                        Toast.makeText(RequestsActivity.this, "Request Accepted:Contact saved", Toast.LENGTH_SHORT).show();

                                                                                    }

                                                                                }
                                                                            });

                                                                }

                                                            }
                                                        });
                                            }

                                        }
                                    });
                        }

                    }
                });

    }

//    public static class RequestViewHolder extends RecyclerView.ViewHolder{
//
//        TextView userName,userStatus;
//        CircleImageView profileImage;
//        Button acceptButton, cancelButton;
//
//        public RequestViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.request_username);
//            profileImage = itemView.findViewById(R.id.request_profile_image);
//            acceptButton = itemView.findViewById(R.id.request_accept_button);
//            cancelButton =itemView.findViewById(R.id.request_decline_button);
//
//
//        }
//    }
}