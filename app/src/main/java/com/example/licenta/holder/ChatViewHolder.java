package com.example.licenta.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;

public class ChatViewHolder extends RecyclerView.ViewHolder
{
   public TextView sentMessage;
   public TextView receivedMessage;
   public LinearLayout sentMessageLayout;
   public LinearLayout receivedMessageLayout;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        sentMessage =  itemView.findViewById(R.id.sent_message_text_view);
        receivedMessage =itemView.findViewById(R.id.received_message_text_view);
        sentMessageLayout = itemView.findViewById(R.id.sent_message_linear_layout);
        receivedMessageLayout = itemView.findViewById(R.id.received_message_linear_layout);
    }
}
