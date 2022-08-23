package com.example.chatapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImage;
    TextView username,comment;
    public CommentViewHolder(@NonNull  View itemView) {
        super(itemView);
         profileImage=itemView.findViewById(R.id.profileImage_comment);
        username=itemView.findViewById(R.id.username_comment);
        comment=itemView.findViewById(R.id.comment_tv);
    }
}
