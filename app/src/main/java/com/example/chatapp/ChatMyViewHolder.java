package com.example.chatapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMyViewHolder extends RecyclerView.ViewHolder {
    CircleImageView firstUserProfile,secondUserProfile;
    TextView firstUserText,secondUserText;
    public ChatMyViewHolder(@NonNull View itemView) {
        super(itemView);

        firstUserProfile=itemView.findViewById(R.id.firstUserProfile);
        firstUserText=itemView.findViewById(R.id.firstUserText);
        secondUserProfile=itemView.findViewById(R.id.secondUserProfile);
        secondUserText=itemView.findViewById(R.id.secondUserText);
    }
}
