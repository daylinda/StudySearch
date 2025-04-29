package com.example.groupprojectapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupprojectapplication.model.News;
import com.example.groupprojectapplication.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
// @author Davina
public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {
    ArrayList<User> userArrayList;

    private ChatsAdapter.OnClickListener onClickListener;

    public class ChatsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView userName;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profileImage = itemView.findViewById(R.id.custom_profile_image);
            this.userName = itemView.findViewById(R.id.chat_item_username);

        }
    }

    public ChatsAdapter(ArrayList<User> arrayList) {
        this.userArrayList = arrayList;

        System.out.println(this.userArrayList.size());
    }

    @NonNull
    @Override
    public ChatsAdapter.ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.custom_chat_item, parent, false);

        // Return a new holder instance
        ChatsAdapter.ChatsViewHolder viewHolder = new ChatsAdapter.ChatsViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ChatsViewHolder holder, int position) {

        Picasso.get().load(userArrayList.get(position).getImageURL()).placeholder(R.drawable.profile_image).into(holder.profileImage);
        holder.userName.setText(userArrayList.get(position).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position);
                }
            }
        });
    }

    public void setOnClickListener(ChatsAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    @Override
    public int getItemCount() {
        if(userArrayList!=null){
            return userArrayList.size();
        }

        return 0;

    }

    public void addData(ArrayList<User>list){
        this.userArrayList = list;
        notifyDataSetChanged();
    }
}
