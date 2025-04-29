package com.example.groupprojectapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.groupprojectapplication.model.User;
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
public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference userRef;

    private ArrayAdapter<String> likedAdapter,createdAdapter;

    ArrayList<String> likedList= new ArrayList<>(),createdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());

        TextView userName = findViewById(R.id.profile_user_name);
        ListView likedTpoics = findViewById(R.id.liked_list);
        ListView createdTopics = findViewById(R.id.created_list);
        CircleImageView imageView = findViewById(R.id.profile_image);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("imageURL"))){
                    String userImage = dataSnapshot.child("imageURL").getValue().toString();
                    if(!userImage.equals("default")){
                        Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(imageView);
                    }


                }
                String username = dataSnapshot.child("username").getValue().toString();
                userName.setText(username);

                for (DataSnapshot ds:dataSnapshot.child("likedtopics").getChildren()
                     ) {
                    String liked =ds.getValue(String.class);
                    System.out.println("Liked topic:"+liked);
                    likedList.add(liked);
                    likedAdapter.notifyDataSetChanged();

                }

                for (DataSnapshot ds:dataSnapshot.child("createdtopics").getChildren()
                ) {
                    String created = ds.getValue(String.class);
                    System.out.println("Created"+ created);
                    createdList.add(ds.getValue(String.class));
                    createdAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        createdAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,createdList);
        createdTopics.setAdapter(createdAdapter);
        likedAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,likedList);
        likedTpoics.setAdapter(likedAdapter);
    }
}