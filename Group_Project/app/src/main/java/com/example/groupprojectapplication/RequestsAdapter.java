package com.example.groupprojectapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupprojectapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
// @author Davina
public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder> {

    ArrayList<User> userArrayList;
    Context context;
    private RequestsAdapter.OnClickListener onClickListener;

    private DatabaseReference ChatRequestRef,UsersRef,ContactsRef;

    User listUser;

    public class RequestsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView userName;
        Button acceptBtn;
        Button declineBtn;

        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profileImage = itemView.findViewById(R.id.request_profile_image);
            this.userName = itemView.findViewById(R.id.request_username);
            this.acceptBtn = itemView.findViewById(R.id.request_accept_button);
            this.declineBtn = itemView.findViewById(R.id.request_decline_button);

        }
    }

    public RequestsAdapter(ArrayList<User> arrayList,Context context) {
        this.userArrayList = arrayList;
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        this.context = context;

        System.out.println("Inside Request adapter constructor");
        System.out.println(this.userArrayList.size());
    }

    @NonNull
    @Override
    public RequestsAdapter.RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.custom_request_item, parent, false);

        // Return a new holder instance
        RequestsAdapter.RequestsViewHolder viewHolder = new RequestsAdapter.RequestsViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.RequestsViewHolder holder, int position) {

        Picasso.get().load(userArrayList.get(position).getImageURL()).placeholder(R.drawable.profile_image).into(holder.profileImage);
        holder.userName.setText(userArrayList.get(position).getUsername());
        holder.acceptBtn.setVisibility(View.VISIBLE);
        holder.declineBtn.setVisibility(View.VISIBLE);

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Accept btn clicked");
                listUser = userArrayList.get(position);
                acceptRequest(listUser.getId(),position);

            }
        });

        holder.declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Decline button clicked");
                listUser = userArrayList.get(position);
                declineRequest(listUser.getId(),position);

            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position);
                }
            }
        });
    }

    public void setOnClickListener(RequestsAdapter.OnClickListener onClickListener) {
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
        System.out.println("Inside Request adapter add Data");

        this.userArrayList = list;
        System.out.println(userArrayList.size());
        notifyDataSetChanged();
    }

    private void acceptRequest(String list_user_id,int position) {
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ContactsRef.child(currentUserID).child(list_user_id).child("Contact")
                .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(context, "Request Accepted:Contact saved for logged in user", Toast.LENGTH_SHORT).show();
                            ContactsRef.child(list_user_id).child(currentUserID).child("Contact")
                                    .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(context, "Request Accepted:Contact saved for user "+ listUser.getUsername(), Toast.LENGTH_SHORT).show();

                                                ChatRequestRef.child(currentUserID).child(list_user_id)
                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(context, "Request Accepted:Request removed for logged in user", Toast.LENGTH_SHORT).show();

                                                                    ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()){
                                                                                        Toast.makeText(context, "Request Accepted:Request removed for user "+ listUser.getUsername(), Toast.LENGTH_SHORT).show();

                                                                                        //remove the request from array and show message
//                                                                                        userArrayList.remove(position);
//                                                                                        notifyDataSetChanged();
//                                                                                        Toast.makeText(context, "Request Accepted:Contact saved", Toast.LENGTH_SHORT).show();

                                                                                        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference().child("PrivateChats");
                                                                                        String chatsMessageKey = chatsRef.push().getKey();
                                                                                        ContactsRef.child(currentUserID).child(list_user_id).child("ChatKey").setValue(chatsMessageKey)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                           @Override
                                                                                                                           public void onComplete(@NonNull Task<Void> task) {
                                                                                                                               if(task.isSuccessful()){
                                                                                                                                   Toast.makeText(context, "Request Accepted:Chat Key added for logged in user ", Toast.LENGTH_SHORT).show();
                                                                                                                                   ContactsRef.child(list_user_id).child(currentUserID).child("ChatKey").setValue(chatsMessageKey).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                       @Override
                                                                                                                                       public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                           if (task.isSuccessful()){
                                                                                                                                               Toast.makeText(context, "Request Accepted:Chat key added for user "+ listUser.getUsername(), Toast.LENGTH_SHORT).show();
                                                                                                                                               Intent intent = new Intent(context, ChatActivity.class);
                                                                                                                                               intent.putExtra("ChatUser",listUser);
                                                                                                                                               intent.putExtra("ChatKey",chatsMessageKey);
                                                                                                                                               context.startActivity(intent);
                                                                                                                                               Toast.makeText(context, "Moving to Chat Activity", Toast.LENGTH_SHORT).show();
                                                                                                                                           }

                                                                                                                                       }
                                                                                                                                   });
                                                                                                                               }
                                                                                                                           }
                                                                                                                       }

                                                                                                );




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

    private void declineRequest(String list_user_id,int position) {
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ChatRequestRef.child(currentUserID).child(list_user_id)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            ChatRequestRef.child(list_user_id).child(currentUserID)
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                //remove the request from array and show message
                                                userArrayList.remove(position);
                                                notifyDataSetChanged();
                                                Toast.makeText(context, "Request declined: Request deleted", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                        }

                    }
                });
    }


}
