package com.example.groupprojectapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.groupprojectapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
// @author Jack
public class SearchHelp extends AppCompatActivity {
    ArrayList<String> pastSearches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_help);
        ListView itemsListView = (ListView) findViewById(R.id.search_help_past_search_list);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, pastSearches);

        itemsListView.setAdapter(adapter);


        DatabaseReference pastRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("pastSearches");
        pastRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    System.out.println(FirebaseAuth.getInstance().getUid());
                    pastSearches.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        pastSearches.add(snapshot.getValue(String.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}