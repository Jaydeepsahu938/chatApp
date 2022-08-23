package com.example.chatapp;

import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImage;
    ImageView postImage,likeImage,commentImage,commentsend;
    TextView username,timeAgo,postDesc,likecounter,commentcounter;
    EditText inputComments;
    public static RecyclerView recyclerView;

    public MyViewHolder(@NonNull  View itemView) {
        super(itemView);
        profileImage=itemView.findViewById(R.id.profileImagePost);
        postImage=itemView.findViewById(R.id.postImage);
        username=itemView.findViewById(R.id.profileUsernamePost);
        timeAgo=itemView.findViewById(R.id.timeAgo);
        postDesc=itemView.findViewById(R.id.postDesc);
        likeImage=itemView.findViewById(R.id.likeImage);
        commentImage=itemView.findViewById(R.id.commentImage);
        likecounter=itemView.findViewById(R.id.likeCounter);
       commentcounter=itemView.findViewById(R.id.commentCounter);
       commentsend=itemView.findViewById(R.id.sendComment);
       inputComments=itemView.findViewById(R.id.inputComments);
       recyclerView=itemView.findViewById(R.id.recyclerViewscomment);

    }

    public void countlikes(String postKey, String uid, DatabaseReference likeRef) {
        likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    int totallikes= (int) snapshot.getChildrenCount();
                    likecounter.setText(totallikes+"");
                }
                else
                {
                    likecounter.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
               if(snapshot.child(uid).exists())
               {
                   likeImage.setColorFilter(Color.GREEN);
               }
                else
               {
                   likeImage.setColorFilter(Color.GRAY);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void countComments(String postKey, String uid, DatabaseReference commentRef) {
        commentRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    int totalComments= (int) snapshot.getChildrenCount();
                    commentcounter.setText(totalComments+"");
                }
                else
                {
                    commentcounter.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }
}
