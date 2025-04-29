package com.example.groupprojectapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.groupprojectapplication.GroupChatActivity;
import com.example.groupprojectapplication.R;
import com.example.groupprojectapplication.model.Group;
import com.example.groupprojectapplication.model.News;
import com.example.groupprojectapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
// @author Davina
public class GroupsFragment extends Fragment {
    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    private ArrayList<Group> listGroups = new ArrayList<>();

    private DatabaseReference GroupRef,userRef;
    FirebaseUser firebaseUser;

    User user;
    Group group;

    Hashtable<String,String> groupNames;


    public GroupsFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_groups, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        groupNames = new Hashtable<String,String>();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        InitialiseFields();

        RetreiveAndDisplayGroups();

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String currentGroupName = adapterView.getItemAtPosition(position).toString();
               // String groupKey = groupNames.get(currentGroupName);
                Group group1 = listGroups.get(position);
                Intent groupChatIntent = new Intent(getContext(), GroupChatActivity.class);
                groupChatIntent.putExtra("groupKey",group1.getId());
                groupChatIntent.putExtra("groupName",group1.getGroupName());

                //groupChatIntent.putExtra("group",group1);

                startActivity(groupChatIntent);


            }
        });

        return groupFragmentView;
    }



    private void InitialiseFields() {

        list_view = (ListView) groupFragmentView.findViewById(R.id.groups_list);
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_groups);
        list_view.setAdapter(arrayAdapter);

    }

    private void RetreiveAndDisplayGroups() {

        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String groupKey = ds.getKey();
                    group = new Group();
                    String groupName = ds.child("groupName").getValue(String.class);
                    //System.out.println("Group name when got from database"+groupName);
                    group.setGroupName(groupName);
                    group.setId(ds.child("id").getValue(String.class));
                    Iterator iterator = ds.child("Users").getChildren().iterator();
                    ArrayList<User> users = new ArrayList<>();
                    while (iterator.hasNext()){
                        User nuser = ((DataSnapshot)iterator.next()).getValue(User.class);
                        //System.out.println("Inside iterator ::"+nuser.getUsername());
                        users.add(nuser);
                    }

                    for (User u:users
                         ) {
                        //System.out.println("Here:"+ u.getUsername());
                        if(u!=null && user!=null){
                            if(u.getId().equals(user.getId())){
                                //System.out.println("Inside if");
                                if(group!=null){
                                    //System.out.println(groupName);
                                }
                                if(!list_of_groups.contains(groupName)){
                                    groupNames.put(groupName,groupKey);
                                    listGroups.add(group);

                                    list_of_groups.add(groupName);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}