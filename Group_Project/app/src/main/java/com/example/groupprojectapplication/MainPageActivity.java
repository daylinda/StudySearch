package com.example.groupprojectapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.groupprojectapplication.fragments.ChatsFragment;
import com.example.groupprojectapplication.fragments.GroupsFragment;
import com.example.groupprojectapplication.fragments.NewsFragment;
import com.example.groupprojectapplication.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
// @author Davina
public class MainPageActivity extends AppCompatActivity {

    TextView username;
    TabLayout tabLayout;
    ViewPageAdapter viewPageAdapter;
    ViewPager viewPager;
    FirebaseUser firebaseUser;
    User user;
    DatabaseReference userRef;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        int i = R.id.item_menu_profile;

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        username = findViewById(R.id.text_home_username);

        //firebase references
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String titleText = "Welcome "+ user.getUsername();
                username.setText(titleText);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewPager = findViewById(R.id.home_viewpage);
        tabLayout = findViewById(R.id.home_tablayout);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new NewsFragment(),"News");
        viewPageAdapter.addFragment(new GroupsFragment(),"Groups");
        viewPageAdapter.addFragment(new ChatsFragment(),"Chats");
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return  true;
    }

    //used to show menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.item_menu_logout){
            FirebaseAuth.getInstance().signOut();
            Condition.getInstance().clearCondition();
            Intent startIntent = new Intent(MainPageActivity.this,MainActivity.class);
            startActivity(startIntent);
            return true;
        }else if(item.getItemId() == R.id.item_menu_profile){
            Intent intent = new Intent(MainPageActivity.this,ProfileActivity.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.item_menu_news){
            Intent intent = new Intent(MainPageActivity.this,AddNewsActivity.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.item_menu_request){
            Intent intent = new Intent(MainPageActivity.this,RequestsActivity.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.item_menu_search_help){
        Intent intent = new Intent(MainPageActivity.this,SearchHelp.class);
        startActivity(intent);
        return true;
        }
        return  false;
    }


}