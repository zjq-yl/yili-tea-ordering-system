package com.example.yl_app.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.models.ChatMessage;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVE = 2;
    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isSend() ? TYPE_SEND : TYPE_RECEIVE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEND) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_send, parent, false);
            return new SendViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_receive, parent, false);
            return new ReceiveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof SendViewHolder) {
            ((SendViewHolder) holder).tvMessage.setText(message.getMessage());
        } else if (holder instanceof ReceiveViewHolder) {
            ((ReceiveViewHolder) holder).tvMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class SendViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        SendViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }
    }

    static class ReceiveViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        ReceiveViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }
    }
}