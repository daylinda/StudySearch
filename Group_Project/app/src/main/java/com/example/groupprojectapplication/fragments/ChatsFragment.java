package com.example.groupprojectapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.groupprojectapplication.ChatActivity;
import com.example.groupprojectapplication.ChatsAdapter;
import com.example.groupprojectapplication.ItemsAdapter;
import com.example.groupprojectapplication.NewsDetailsActivity;
import com.example.groupprojectapplication.R;
import com.example.groupprojectapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
// @author Davina
public class ChatsFragment extends Fragment {
    private View PrivateChatsView;
    private RecyclerView ChatsList;
    private DatabaseReference ChatsRef,UsersRef,ChatContactsRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    private ArrayList<User> chatContacts = new ArrayList<>();

    private ArrayList<String> chatMessageKey = new ArrayList<>();


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        PrivateChatsView = inflater.inflate(R.layout.fragment_chats, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        ChatContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //ChatsRef = FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserID);
        ChatsAdapter chatsAdapter = new ChatsAdapter(chatContacts);
        ChatsList = (RecyclerView)PrivateChatsView.findViewById(R.id.chat_list);

        ChatContactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()
                     ) {
                    String userId = ds.getKey();
                    UsersRef.child(userId).
                            addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    chatContacts.add(user);
                                    chatsAdapter.addData(chatContacts);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                } });

                    String messageKey = ds.child("ChatKey").getValue().toString();
                    chatMessageKey.add(messageKey);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        ChatsList.setAdapter(chatsAdapter);
        ChatsList.setLayoutManager(new LinearLayoutManager(getContext()));

        chatsAdapter.setOnClickListener(new ChatsAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                User chatUser = chatContacts.get(position);
                intent.putExtra("ChatUser",chatUser);
                intent.putExtra("ChatKey",chatMessageKey.get(position));
                startActivity(intent);
            }
        });

        return PrivateChatsView;
    }





    @Override
    public void onStart() {
        super.onStart();




    }
}