package com.example.groupprojectapplication.fragments;

import static com.example.groupprojectapplication.ListAVLTree.toInOrderList;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.groupprojectapplication.Condition;
import com.example.groupprojectapplication.ItemsAdapter;

import com.example.groupprojectapplication.ListAVLTree;
import com.example.groupprojectapplication.LoginActivity;
import com.example.groupprojectapplication.MainPageActivity;

import com.example.groupprojectapplication.NewsDetailsActivity;

import com.example.groupprojectapplication.R;
import com.example.groupprojectapplication.RegisterActivity;
import com.example.groupprojectapplication.SearchParser;
import com.example.groupprojectapplication.SearchTokeniser;
import com.example.groupprojectapplication.model.News;
import com.example.groupprojectapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
// @author Jack
public class NewsFragment extends Fragment {

    RecyclerView itemsListView;
    ItemsAdapter customAdapter;
    RecyclerView.LayoutManager layoutManager;
    View NewsView;
    private DatabaseReference newsRef,userRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    User user;
    Button searchButton;
    SearchView search;
    ListAVLTree.node dbRoot = null;
    ArrayList<News> displayNewsList = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NewsView =inflater.inflate(R.layout.fragment_news, container, false);
        displayNewsList = new ArrayList<>();

        newsRef = FirebaseDatabase.getInstance().getReference().child("News");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());


        itemsListView = (RecyclerView)NewsView.findViewById(R.id.items_list);
        customAdapter = new ItemsAdapter(displayNewsList, NewsView.getContext());

        searchButton = NewsView.findViewById(R.id.id_search_button);
        search = NewsView.findViewById(R.id.news_search);
        String hint = "{(TIME) or (DAY) or (TAG)} {: or < or >} Value";
        search.setQueryHint(hint);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String searchString = (String) search.getQuery().toString();
                    userRef.child("pastSearches").child(String.valueOf(searchString.hashCode())).setValue(searchString);
                    SearchTokeniser tokenizer = new SearchTokeniser(searchString);
                    SearchParser parser = new SearchParser(tokenizer);
                    Condition.getInstance().clearCondition();
                    parser.parseSearch();
                    System.out.println(Condition.getInstance().show());
                    displayNewsList = Condition.getInstance().Filter(dbRoot);
                    if (displayNewsList.isEmpty()){
                        Toast.makeText(getActivity(), "No Results :(", Toast.LENGTH_SHORT).show();
                    }else
                        customAdapter.addData(displayNewsList);
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });

        layoutManager = new LinearLayoutManager(NewsView.getContext());
        itemsListView.setLayoutManager(layoutManager);
        itemsListView.setAdapter(customAdapter);
        user = new User(null, null, null, null);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    System.out.println(FirebaseAuth.getInstance().getUid());
                    user.setId(dataSnapshot.child("id").getValue(String.class));
                    user.setUseremail(dataSnapshot.child("useremail").getValue(String.class));
                    user.setUsername(dataSnapshot.child("username").getValue(String.class));
                    user.setImageURL(dataSnapshot.child("imageURL").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        newsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                News news = dataSnapshot.getValue(News.class);
                if(dataSnapshot.child("promoted").getValue()!=null)
                    news.setPromoted(dataSnapshot.child("promoted").getValue(Boolean.class));
                dbRoot = ListAVLTree.insert(dbRoot, news);

                displayNewsList = Condition.getInstance().Filter(dbRoot);
                customAdapter.addData(displayNewsList);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                News news = dataSnapshot.getValue(News.class);
                if(dataSnapshot.child("promoted").getValue()!=null)
                    news.setPromoted(dataSnapshot.child("promoted").getValue(Boolean.class));
                dbRoot = ListAVLTree.replaceNode(dbRoot, news);

                displayNewsList = Condition.getInstance().Filter(dbRoot);
                customAdapter.addData(displayNewsList);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                News news = dataSnapshot.getValue(News.class);
                dbRoot = ListAVLTree.deleteNode(dbRoot, news);
                if (Condition.getInstance().isNull()){
                    displayNewsList = new ArrayList<>();
                    displayNewsList = toInOrderList(dbRoot, displayNewsList);
                    customAdapter.addData(displayNewsList);
                }else{
                    displayNewsList = Condition.getInstance().Filter(dbRoot);
                    customAdapter.addData(displayNewsList);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        customAdapter.setOnClickListener(new ItemsAdapter.OnClickListener() {
           @Override
           public void onClick(int position) {
               Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
               intent.putExtra("currentNews",displayNewsList.get(position));
               startActivity(intent);
           }
       });

        // Inflate the layout for this fragment
        return NewsView;
    }



}