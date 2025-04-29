package com.example.groupprojectapplication;

import static com.example.groupprojectapplication.ListAVLTree.numberToTime;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.groupprojectapplication.fragments.GroupsFragment;
import com.example.groupprojectapplication.fragments.NewsFragment;
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
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    ArrayList<News> newsArrayList;
    Context context;
    private OnClickListener onClickListener;

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView topic;
        TextView time;

        TextView likes;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.topic = itemView.findViewById(R.id.item_topic);
            this.time = itemView.findViewById(R.id.item_time);
            this.likes = itemView.findViewById(R.id.item_likes);
        }


    }

    public ItemsAdapter(ArrayList<News> newsArrayList, Context context) {
        this.newsArrayList = newsArrayList;
        System.out.println(this.newsArrayList.size());
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.custom_item_layout, parent, false);

        // Return a new holder instance
        ItemsAdapter.ItemViewHolder viewHolder = new ItemsAdapter.ItemViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder holder, int position) {
        holder.topic.setText(newsArrayList.get(position).getTopic());

        holder.time.setText(new ConvertTimeDay().getDayAndTime( newsArrayList.get(position).getTime()));
        holder.likes.setText(newsArrayList.get(position).getLikes() == null?"0":newsArrayList.get(position).getLikes());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position);
                }
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    @Override
    public int getItemCount() {
        if(newsArrayList!=null){
            return newsArrayList.size();
        }

        return 0;

    }

    public void addData(ArrayList<News>list){
        this.newsArrayList = list;
        notifyDataSetChanged();
    }


}

